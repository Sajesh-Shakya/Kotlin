package graphs

data class Edge<T>(private val n1: Node<T>, private val n2: Node<T>, private var weight: Int = 0) {
    init {
        n1.incDegree()
        n2.incDegree()
    }
    fun getN1(): Node<T> = n1

    fun getN2(): Node<T> = n2

    fun getWieght() = weight

    fun updateWeight(newWeight: Int) {
        weight = newWeight
    }
}
