package com.jacagen.organizer

import kotlinx.serialization.Serializable

@Serializable
data class Tree<T>(var root: Node<T>? = null, val log: (String) -> Unit) {
    fun getNode(f: (T) -> Boolean): Node<T> {
        return search(f, mutableListOf(root!!))
    }

    fun depthFirst() = depthFirstYield(root!!)

    private fun depthFirstYield(node: Node<T>, level: Int = 0): Sequence<Pair<Node<T>, Int>> = sequence {
        log("Yielding level $level, $node")
        yield(Pair(node, level))
        log("${node.children.size} children")
        node.children.forEach { yieldAll(depthFirstYield(it, level + 1)) }
    }

    private tailrec fun search(f: (T) -> Boolean, toSearch: MutableList<Node<T>> = mutableListOf()): Node<T> {
        if (toSearch.isEmpty())
            throw IllegalStateException("Cannot find requested node")
        else {
            val first = toSearch.removeFirst()
            if (f(first.payload)) return first
            else {
                toSearch.addAll(first.children)
                return search(f, toSearch)
            }
        }
    }

    override fun toString(): String {
        return depthFirst()
            .map { (n, i) -> "\t".repeat(i) + n }
            .joinToString("\n")
    }
}

@Serializable
data class Node<T>(
    val payload: T,
    val parent: Node<T>?,
    val children: MutableList<Node<T>> = mutableListOf(),
    val tree: Tree<T>,
) {
    override fun toString() = payload.toString()
}