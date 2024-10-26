package graphs

import java.lang.IllegalArgumentException

typealias Path<T> = Set<Node<T>>

abstract class Graph<T>(private val nodes: Set<Node<T>>, private val edges: Set<Edge<T>>) {
    abstract val adj: Map<Node<T>, List<Node<T>>>

    fun getNodes(): Set<Node<T>> = nodes

    fun getEdges(): Set<Edge<T>> = edges

    fun isSubGraphOf(sup: Graph<T>): Boolean = ((getNodes() - sup.getNodes()) union (getEdges() - sup.getEdges())).isEmpty()

    abstract fun dfs(start: Node<T>): Path<T>

    abstract fun bfs(start: Node<T>): Path<T>
}

class UG<T>(private val nodes: Set<Node<T>>, private val edges: Set<Edge<T>>) : Graph<T>(nodes,edges) {

    override val adj: Map<Node<T>, List<Node<T>>> = nodes.associateWith { node -> edges.filter { it.getN1() == node }.map { it.getN2() } }

    override fun dfs(start: Node<T>): Path<T> = dfsHelper(start)

    fun dfsHelper(start: Node<T>, visited: MutableSet<Node<T>> = mutableSetOf<Node<T>>()): Path<T> {
        if (start !in nodes) { throw IllegalArgumentException("start node not in graph") } else {
            if (visited.size == getNodes().size) { return emptySet<Node<T>>() } else {
                visited.add(start)
                println(visited)
                for (node in adj[start]!!) {
                    if (visited.size == nodes.size) {
                        break
                    }
                    if (node !in visited) {
                        visited.addAll(dfsHelper(node, (visited)))
                    }
                }
                return visited
            }
        }
    }
    override fun bfs(start: Node<T>): Path<T> {
        if (start !in nodes) { throw IllegalArgumentException("start node is not in graph") } else {
            val visited = mutableSetOf<Node<T>>(start)
            val queue: ArrayDeque<Node<T>> = ArrayDeque<Node<T>>()
            val ad = adj
            queue.add(start)
            while (queue.isNotEmpty() && visited.size < getNodes().size) {
                var front = queue.first()
                for (node in ad[front]!!) {
                    if (node !in visited) {
                        println(node)
                        visited.add(node)
                        queue.addLast(node)
                    }
                }
                queue.removeFirst()
            }
            println(visited)
            return visited.toSet()
        }
    }

}

fun main() {
    val nodes: List<Node<Int>> = (1..4).zip('a'..'d') { fst, snd -> Node(snd.toString(), fst) }
    val edges: Set<Edge<Int>> = (nodes.flatMap { node -> nodes.map { Edge(it, node) } }).toSet()
    val edges2: Set<Edge<Int>> = (edges.filter { it.getN1() != it.getN2() }).toSet()
    // println(edges)

    
    fun twoColour(graph: UG<Int>): UG<Int> {
        var col = 0
        val start = graph.getNodes().first()
        if (start !in nodes) { throw IllegalArgumentException("start node is not in graph") } else {
            val visited = mutableSetOf<Node<Int>>(start)
            val queue: ArrayDeque<Node<Int>> = ArrayDeque<Node<Int>>()
            val ad = graph.adj
            queue.add(start)
            while (queue.isNotEmpty() && visited.size < graph.getNodes().size) {
                var front = queue.first()
                front.item = col
                for (node in ad[front]!!) {
                    if (node !in visited) {
                        visited.add(node)
                        queue.addLast(node)
                    }
                }
                col = 1 - col
                queue.removeFirst()
            }
            println(visited)
        }


    }
}
