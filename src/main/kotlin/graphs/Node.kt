package graphs

data class Node<T>(val name: String, var item: T? = null) {
    private var degree: Int = 0

    fun incDegree() {
        degree++
    }

    override fun toString(): String = name
}
