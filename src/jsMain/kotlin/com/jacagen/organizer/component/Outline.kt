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

class OutlineNode<T, C : Component>(
    private val node: Node<T>,
    private val outlineHelper: OutlineHelper<T, C>,
) : VPanel() {

    /*
        <OutlineNode: VPanel>
            <hPanel>
                <spacer n>
                <TaskComponent>
            <hPanel>
            <hPanel>
                <spacer 1>
                <OutlineNode>
     */
    private var outlineParent: OutlineNode<T, C>? = null


    private val nodeComponent = outlineHelper.createComponent(node.payload)
    private val mainHPanel: HPanel
    private val childrenVPanel: VPanel

    init {
        mainHPanel = hPanel {
            add(nodeComponent)
        }
        childrenVPanel = vPanel {
            for (c in node.children) {
                hPanel {
                    spacer(1)
                    val child = OutlineNode(c, outlineHelper))
                    child.outlineParent = this@OutlineNode
                    add(child)
                }
            }
        }
        registerHandlers()
    }

    override fun focus() {
        nodeComponent.focus()
    }

    fun addChild(position: Int, child: Node<T>, outlineHelper: OutlineHelper<T, C>): OutlineNode<T, C> {
        val newNode = OutlineNode(child, this, outlineHelper)
        val hPanel = HPanel()
        hPanel.add(Spacer(1))
        hPanel.add(newNode)
        add(position, hPanel)
        return newNode
    }

    fun myIndex(): Int {
        console.log("Outline parent is $outlineParent")
        val outlineParent = outlineParent!!
        if (outlineParent is OutlineNode)
            console.log("Outline parent is outline node")
        val siblings = outlineParent.getChildren().subList(1, outlineParent.getChildren().size)
        console.log("Siblings are ${siblings}")
        for (s in siblings) {
            if (s is OutlineNode<*, *>) {
                console.log("Sibling is OutlineNode")
            }
            console.log("Sibling is ${js("typeof s")}")
        }
        val idx = siblings.indexOf(this)
        console.log("Index of ${node.payload} in ${outlineParent.node} is $idx")
        return idx
    }

    private fun registerHandlers() {
        mainHPanel.onEvent {   // TODO onEventLaunch?
            keydown = { e ->
                when (e.key) {
                    "Enter" -> {
                        val newIndex = myIndex() + 1
                        val newNode = outlineHelper.newNodeRequestor(node.parent!!, newIndex)
                        outlineParent!!.addChild(newIndex, newNode, outlineHelper)
                    }
                }
            }
        }
    }

}







