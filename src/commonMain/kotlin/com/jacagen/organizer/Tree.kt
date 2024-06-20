package com.jacagen.organizer

import kotlinx.serialization.Serializable

@Serializable
data class Tree<T>(var root: Node<T>? = null, val creator: () -> T) {
    operator fun get(f: (T)-> Boolean): Node<T> = search(f, mutableListOf(root!!))

    fun depthFirst() = depthFirstYield(root!!)

    private fun depthFirstYield(node: Node<T>, level: Int = 0): Sequence<Pair<Node<T>, Int>> = sequence {
        yield(Pair(node, level))
        node.children.forEach { yieldAll(depthFirstYield(it, level + 1)) }
    }

    private tailrec fun search(f: (T) -> Boolean, toSearch: MutableList<Node<T>> = mutableListOf()): Node<T> {
        if (toSearch.isEmpty()) TODO()
        else {
            val first = toSearch.removeFirst()
            if (f(first.payload)) return first
            else {
                toSearch.addAll(first.children)
                return search(f, toSearch)
            }
        }
    }

    override fun toString(): String = depthFirst().map { (n, i) ->
        "\t".repeat(i) + n
    }
        .joinToString("\n")
}

fun <T> newTree(root: T, creator: () -> T): Tree<T> {
    val tree = Tree(creator = creator)
    val node = Node(
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
    val payload: T,
    val parent: Node<T>?,
    val children: MutableList<Node<T>> = mutableListOf(),
    val tree: Tree<T>,
) {
    fun newNodeAfter(): Node<T> {
        val newElement = tree.creator()
        val newNode = Node(newElement, parent, tree = tree)
        val myIndex = parent!!.children.indexOf(this)
        parent.children.add(myIndex + 1, newNode)
        return newNode
    }

    override fun toString() = payload.toString()
}

