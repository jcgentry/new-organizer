package com.jacagen.organizer.component

import com.jacagen.organizer.Node
import io.kvision.core.Component
import io.kvision.core.onEvent
import io.kvision.panel.HPanel
import io.kvision.panel.VPanel
import io.kvision.panel.hPanel
import io.kvision.panel.vPanel

interface OutlineHelper<T, C : Component> {
    fun createComponent(payload: T): C
    fun newNodeRequestor(parent: Node<T>, position: Int?): Node<T>
}

@Suppress("UNCHECKED_CAST")
class OutlineNode<T, C : Component>(
    private val node: Node<T>,
    outlineHelper: OutlineHelper<T, C>,
    private val isRoot: Boolean = false,
) : VPanel() {
    init {
        val newComponent = outlineHelper.createComponent(node.payload)
        add(newComponent)
        hPanel {
            spacer(1)
            vPanel()
        }
        registerHandlers(outlineHelper)
    }

    override fun focus() {
        getChildren()[0].focus()
    }

    fun myChildList(): VPanel {
        val indentedChildList = getChildren()[1] as HPanel
        val childList = indentedChildList.getChildren()[1] as VPanel
        return childList
    }

    fun parentOutlineNode(): OutlineNode<T, C> {
        val containingChildList = parent as VPanel
        val containingIndentedChildList = containingChildList.parent as HPanel
        return containingIndentedChildList.parent as OutlineNode<T, C>
    }

    fun containingChildList(): VPanel = parent as VPanel

    fun addChild(node: Node<T>, index: Int, outlineHelper: OutlineHelper<T, C>): OutlineNode<T, C> {
        val newOutlineNode = OutlineNode(node, outlineHelper)
        myChildList().add(index, newOutlineNode)
        newOutlineNode.focus()
        return newOutlineNode
    }

    private fun registerHandlers(outlineHelper: OutlineHelper<T, C>) {
        val myTaskComponent = getChildren()[0] as TaskComponent
        myTaskComponent.onEvent {
            keydown = { e ->
                when (e.key) {
                    "Enter" -> {
                        val containingOutlineNode = myTaskComponent.parent as OutlineNode<T, C>
                        if (containingOutlineNode.isRoot) {/* I'm the top node--add a child to me */
                            val childCount = myChildList().getChildren().size
                            val newNode = outlineHelper.newNodeRequestor(node, childCount)
                            containingOutlineNode.addChild(newNode, childCount, outlineHelper)
                        } else {/* I'm an inner node--add a sibling */
                            val containingChildList = containingOutlineNode.parent as VPanel
                            val myIndex = containingChildList.getChildren().indexOf(containingOutlineNode)
                            val newNode = outlineHelper.newNodeRequestor(node.parent!!, myIndex + 1)
                            parentOutlineNode().addChild(newNode, myIndex + 1, outlineHelper)
                        }
                    }
                }
            }
        }
    }
}







