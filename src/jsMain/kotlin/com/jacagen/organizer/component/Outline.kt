package com.jacagen.organizer.component

import com.jacagen.organizer.Node
import com.jacagen.organizer.Task
import com.jacagen.organizer.Tree
import io.kvision.core.*
import io.kvision.form.FormPanel
import io.kvision.form.formPanel
import io.kvision.form.text.Text
import io.kvision.form.text.text
import io.kvision.html.*
import io.kvision.panel.HPanel
import io.kvision.panel.VPanel
import io.kvision.panel.hPanel
import io.kvision.panel.vPanel
import io.kvision.table.Row
import io.kvision.utils.auto
import io.kvision.utils.ch
import io.kvision.utils.px
import kotlin.text.Typography.nbsp

class Outline<T : Any>(tree: Tree<T>, containerFun: (T) -> Container) : VPanel() {
    init {
        for ((node, level) in tree.depthFirst()) {
            val row = OutlineRow(level, node, containerFun)
            add(row)
        }
    }
}

fun <T : Any> Container.outline(tree: Tree<T>, containerFun: (T) -> Container) {
    val o = Outline(tree, containerFun)
    add(o)
}

class OutlineRow<T>(val indent: Int, val node: Node<T>, containerFun: (T) -> Container) : HPanel() {
    init {
        hPanel {
            spacer(indent)
            val nodeContainer = containerFun(node.payload)
            add(nodeContainer)
        }
        onClickLaunch {
            //console.log(findChild<HPanel>().findChild<TaskComponent>().findChild<ResizableText>().getChildren())
            findChild<HPanel>().findChild<TaskComponent>().findChild<ResizableText>().readonly = false
        }
    }

}


inline fun <reified T : Any> Container.findChild(): T = getChildren().first { it is T } as T
//
//class Outline<T : Any>(root: T, label: (T) -> String, children: (T) -> List<T>) : Table() {
//    var enabledRow: Text? = null
//
//    init {
//        apply {
//            for ((indent, thisNode) in flattenNode(root, children)) {
//                row(indent, thisNode, label, { console.log("Outline affected") })
//            }
//        }
//    }
//
//    private fun row(indent: Int, node: T, label: (T) -> String, onEnter: () -> Unit) {
//        val row = OutlineRow(indent, node, label, onEnter)
//        add(row)
//    }
//}
//
//
//
//
//private fun <T> flattenNode(node: T, children: (T) -> List<T>, level: Int = 0): Sequence<Pair<Int, T>> = sequence {
//    yield(Pair(level, node))
//    for (c in children(node)) {
//        yieldAll(flattenNode(c, children, level + 1))     // Too much recursion?
//    }
//}
//
