package com.jacagen.organizer.component

import com.jacagen.organizer.Node
import io.kvision.core.Component
import io.kvision.core.Outline
import io.kvision.core.onEvent
import io.kvision.html.p
import io.kvision.panel.HPanel
import io.kvision.panel.VPanel
import io.kvision.panel.hPanel
import io.kvision.panel.vPanel

interface OutlineHelper<T, C : Component> {
    fun createComponent(payload: T): C
    fun newNodeRequestor(parent: Node<T>, position: Int?): Node<T>
}

class OutlineNode<T, C : Component>(
    private val node: Node<T>,
    private val outlineHelper: OutlineHelper<T, C>,
) : VPanel() {
    init {
        add(outlineHelper.createComponent(node.payload))
        hPanel {
            spacer(1)
            vPanel()
        }
        registerHandlers()
    }

    fun addChild(node: Node<T>, index: Int): OutlineNode<T, C> {
        val childHPanel = getChildren()[1].unsafeCast<HPanel>()
        val childVPanel = childHPanel.getChildren()[1].unsafeCast<VPanel>()
        val newOutlineNode = OutlineNode(node, outlineHelper)
        childVPanel.add(index, newOutlineNode)
        return newOutlineNode
    }

    private fun registerHandlers() {
        (getChildren()[0] as TaskComponent).onEvent {
            keydown = { e ->
                when (e.key) {
                    "Enter" -> {
                        val target = e.currentTarget!! as TaskComponent
                        val myOutlineNode = target.parent as OutlineNode<T, C>
                        val parentVPanel = myOutlineNode.parent as VPanel
                        val myIndex = parentVPanel.getChildren().indexOf(myOutlineNode)
                        val newNode = outlineHelper.newNodeRequestor(node.parent!!, myIndex + 1)
                        val parentHPanel = parentVPanel.parent as HPanel
                        val parentOutlineNode = parentHPanel.parent as OutlineNode<T, C>
                        parentOutlineNode.addChild(newNode, myIndex + 1)
                    }
                }
            }
        }
    }
}







