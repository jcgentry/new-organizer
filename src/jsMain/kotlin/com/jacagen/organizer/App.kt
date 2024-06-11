package com.jacagen.organizer

import com.jacagen.organizer.component.TaskComponent
import com.jacagen.organizer.component.outline
import io.kvision.*
import io.kvision.core.KVScope
import io.kvision.core.onClickLaunch
import io.kvision.form.formPanel
import io.kvision.form.text.TomTypeaheadRemote
import io.kvision.form.text.text
import io.kvision.form.text.tomTypeaheadRemote
import io.kvision.form.text.tomTypeaheadRemoteInput
import io.kvision.html.*
import io.kvision.panel.*
import io.kvision.remote.getService
import io.kvision.remote.getServiceManager
import io.kvision.state.bind
import io.kvision.state.bindEach
import io.kvision.state.observableListOf
import io.kvision.state.sub
import io.kvision.utils.perc
import io.kvision.utils.px
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.uuid.UUID


actual fun newGuid(): Guid = UUID().toString()

fun testTree(): Tree<Task> {
    val tree = newTree(Task("\$ROOT"))
    AddNode(tree.root.id, Task("Entertain regularly")).apply(tree)
    val t = AddNode(tree.root.id, Task("Chris's birthday")).apply(tree)
    AddNode(t.id, Task("Figure out plans for Chris's birthday")).apply(tree)
    return tree
}


class App : Application() {
    val tree = testTree()

    override fun start(state: Map<String, Any>) {
        console.log("Starting")
        root("kvapp") {
            outline(tree) { t -> TaskComponent(t) }
        }
    }
}

val AppScope = CoroutineScope(Dispatchers.Default + SupervisorJob())    // ### Needed?

class OldApp : Application() {
    val characterService = getService<ICharacterService>()

    val characters = observableListOf<MovieCharacter>()

    val starTrek = characters.sub { it.filter { it.title == "Star Trek" } }
    val starWars = characters.sub { it.filter { it.title == "Star Wars" } }
    val other = characters.sub { it.filter { it.title != "Star Trek" && it.title != "Star Wars" } }

    override fun start(state: Map<String, Any>) {
        root("kvapp") {
            vPanel {
                padding = 10.px
                width = 100.perc
                tabPanel {
                    tab("Star Trek", "fas fa-hand-spock") {
                        ul().bindEach(starTrek) {
                            li(it.name)
                        }
                    }
                    tab("Star Wars", "fas fa-jedi") {
                        ul().bindEach(starWars) {
                            li(it.name)
                        }
                    }
                    tab("Other", "fas fa-spider") {
                        ul().bindEach(other) {
                            li("${it.name} (${it.title})")
                        }
                    }
                }
                val form = formPanel<MovieCharacter> {
                    fieldsetPanel(legend = "Movie character") {
                        text(label = "Name:").bind(MovieCharacter::name, required = true)
                        tomTypeaheadRemote(getServiceManager(), ICharacterService::movies, label = "Movie title").bind(
                            MovieCharacter::title,
                            required = true
                        )
                    }
                }
                button("Add character", "fas fa-plus").onClickLaunch {
                    val character = form.getData()
                    characters += character
                    characterService.add(character)
                    form.clearData()
                    form.focus()
                }
            }
        }
        KVScope.launch {
            val chars = characterService.getCharacters()
            characters.addAll(chars)
        }
    }
}

fun main() {
    startApplication(
        ::App,
        module.hot,
        BootstrapModule,
        BootstrapCssModule,
        CoreModule,
        FontAwesomeModule,
        TomSelectModule,
    )
}
