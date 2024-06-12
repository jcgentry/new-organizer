package com.jacagen.organizer

import kotlinx.serialization.Serializable

@Serializable
sealed interface Operation<T, R> {
    fun apply(tree: Tree<T>): R
}

@Serializable
data class AddNode<T>(val parent: Guid, val payload: T): Operation<T, Node<T>> {
    override fun apply(tree: Tree<T>): Node<T> {
        val parentNode = tree[parent]
        val newNode = Node(newGuid(), payload, parentNode)
        parentNode.children.add(newNode)
        return newNode
    }
}