package com.jacagen.organizer

import com.jacagen.organizer.component.TaskComponent
import com.jacagen.organizer.component.outline
import io.kvision.*
import io.kvision.core.onClickLaunch
import io.kvision.html.button
import io.kvision.panel.root

fun testTree(): Tree<Task> {
    val tree = newTree(Task("\$ROOT")) { Task("") }
    AddNode(tree.root!!.id, Task("Entertain regularly")).apply(tree)
    val t = AddNode(tree.root!!.id, Task("Chris's birthday")).apply(tree)
    AddNode(t.id, Task("Figure out plans for Chris's birthday")).apply(tree)
    return tree
}

class App : Application() {
    private val tree = testTree()

    override fun start(state: Map<String, Any>) {
        root("kvapp") {
           val outline = outline(tree,
                containerFun = { t ->
                    TaskComponent(t)
                },
           )
            button("Show outline").onClickLaunch {
                console.log(outline.tree.toString())
            }

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
