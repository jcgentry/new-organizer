package com.jacagen.organizer

import com.jacagen.organizer.component.TaskComponent
import com.jacagen.organizer.component.outline
import io.kvision.*
import io.kvision.core.onClickLaunch
import io.kvision.html.button
import io.kvision.panel.root

fun testTree(): Tree<Task> {
    val tree = newTree(Task(newGuid(), "\$ROOT")) { Task(newGuid(), "") }
    AddNode(tree.root!!.payload.id, newGuid(), label = "Entertain regularly").apply(tree)
    val t = AddNode(tree.root!!.payload.id, newGuid(), label = "Chris's birthday").apply(tree)
    AddNode(t.payload.id, newGuid(), label = "Figure out plans for Chris's birthday").apply(tree)
    return tree
}

class App : Application() {
    val tree = testTree()

    override fun start() {
        root("kvapp") {
           val outline = outline(tree,
                containerFun = { t ->
                    TaskComponent(t)
                },
           )
            button("Show outline").onClickLaunch {
                console.log(outline.tree.toString())
            }
            button("Add test tree").onClickLaunch {  }

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
