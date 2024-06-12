package com.jacagen.organizer

import kotlinx.serialization.Serializable

@Serializable
data class Tree<T>(val root: Node<T>) {
    operator fun get(id: Guid): Node<T> = search(id, mutableListOf(root))

    fun depthFirst() = depthFirstYield(root)

    private fun depthFirstYield(node: Node<T>, level: Int = 0): Sequence<Pair<Node<T>, Int>> = sequence {
        yield(Pair(node, level))
        node.children.forEach { yieldAll(depthFirstYield(it, level + 1)) }
    }

    private tailrec fun search(id: Guid, toSearch: MutableList<Node<T>> = mutableListOf()): Node<T> {
        if (toSearch.isEmpty()) TODO()
        else {
            val first = toSearch.removeFirst()
            if (first.id == id) return first
            else {
                toSearch.addAll(first.children)
                return search(id, toSearch)
            }
        }
    }
}

fun <T> newTree(root: T) = Tree(Node(newGuid(), root, parent = null, children = mutableListOf()))

@Serializable
data class Node<T>(
    val id: Guid,
    val payload: T,
    val parent: Node<T>?,
    val children: MutableList<Node<T>> = mutableListOf()
)


