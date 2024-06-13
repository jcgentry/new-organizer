package com.jacagen.organizer

import kotlinx.serialization.Serializable

@Serializable
data class Tree<T>(var root: Node<T>? = null, val creator: () -> T) {
    operator fun get(id: Guid): Node<T> = search(id, mutableListOf(root!!))

    fun depthFirst() = depthFirstYield(root!!)

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

fun <T> newTree(root: T, creator: () -> T): Tree<T> {
    val tree = Tree(creator = creator)
    val node = Node(
        newGuid(),
        root,
        parent = null,
        children = mutableListOf(),
        tree,
    )
    tree.root = node
    return tree
}

@Serializable
data class Node<T>(
    val id: Guid,
    val payload: T,
    val parent: Node<T>?,
    val children: MutableList<Node<T>> = mutableListOf(),
    val tree: Tree<T>,
) {
    fun newNodeAfter(): Node<T>{
        val newElement = tree.creator()
        val newNode = Node(newGuid(), newElement, parent, tree = tree)
        val myIndex = parent!!.children.indexOf(this)
        parent.children.add(myIndex + 1, newNode)
        return newNode
    }
}

