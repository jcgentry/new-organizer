package com.jacagen.organizer

import kotlinx.serialization.Serializable

@Serializable
sealed interface Operation<R> {
    fun apply(tree: Tree<Task>): R
}

@Serializable
data class AddNode(val parent: Guid, val newNode: Guid, val label: String): Operation<Node<Task>> {
    override fun apply(tree: Tree<Task>): Node<Task> {
        val parentNode = tree[{ t -> t.id == parent }]
        val newNode = Node(Task(newNode, label), parentNode, tree = tree)
        parentNode.children.add(newNode)
        return newNode
    }
}
