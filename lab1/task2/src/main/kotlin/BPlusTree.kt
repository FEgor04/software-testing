class BPlusTreeNode<K : Comparable<K>, V>(var isLeaf: Boolean = true) {
    var keys: MutableList<K> = mutableListOf()
    var values: MutableList<V?> = mutableListOf()
    var children: MutableList<BPlusTreeNode<K, V>> = mutableListOf()
    var next: BPlusTreeNode<K, V>? = null // Next pointer for leaf node linking
}

class BPlusTree<K : Comparable<K>, V>(private val degree: Int) {
    private var root: BPlusTreeNode<K, V> = BPlusTreeNode()

    fun search(key: K): V? {
        val leaf = findLeafNode(key)
        val index = leaf.keys.indexOf(key)
        return if (index != -1) leaf.values[index] else null
    }

    fun insert(key: K, value: V) {
        val leaf = findLeafNode(key)
        insertIntoLeaf(leaf, key, value)
        if (leaf.keys.size >= degree) {
            splitLeaf(leaf)
        }
    }

    private fun findLeafNode(key: K): BPlusTreeNode<K, V> {
        var node = root
        while (!node.isLeaf) {
            var i = 0
            while (i < node.keys.size && key >= node.keys[i]) {
                i++
            }
            node = node.children[i]
        }
        return node
    }

    private fun insertIntoLeaf(leaf: BPlusTreeNode<K, V>, key: K, value: V) {
        val index = leaf.keys.indexOfFirst { it >= key }
        if (index >= 0 && index < leaf.keys.size && leaf.keys[index] == key) {
            leaf.values[index] = value
        } else {
            leaf.keys.add(if (index == -1) leaf.keys.size else index, key)
            leaf.values.add(if (index == -1) leaf.values.size else index, value)
        }
    }

    private fun splitLeaf(leaf: BPlusTreeNode<K, V>) {
        val newLeaf = BPlusTreeNode<K, V>(isLeaf = true)
        val mid = leaf.keys.size / 2
        newLeaf.keys.addAll(leaf.keys.subList(mid, leaf.keys.size))
        newLeaf.values.addAll(leaf.values.subList(mid, leaf.values.size))
        leaf.keys = leaf.keys.subList(0, mid).toMutableList()
        leaf.values = leaf.values.subList(0, mid).toMutableList()

        newLeaf.next = leaf.next
        leaf.next = newLeaf

        if (leaf === root) {
            val newRoot = BPlusTreeNode<K, V>(isLeaf = false)
            newRoot.keys.add(newLeaf.keys[0])
            newRoot.children.add(leaf)
            newRoot.children.add(newLeaf)
            root = newRoot
        } else {
            insertIntoParent(leaf, newLeaf.keys[0], newLeaf)
        }
    }

    private fun insertIntoParent(left: BPlusTreeNode<K, V>, key: K, right: BPlusTreeNode<K, V>) {
        var parent = findParent(root, left) ?: return
        val index = parent.children.indexOf(left)
        parent.keys.add(index, key)
        parent.children.add(index + 1, right)

        if (parent.keys.size >= degree) {
            splitInternal(parent)
        }
    }

    private fun findParent(node: BPlusTreeNode<K, V>, child: BPlusTreeNode<K, V>): BPlusTreeNode<K, V>? {
        if (node.isLeaf) return null
        for (c in node.children) {
            if (c == child) return node
            val parent = findParent(c, child)
            if (parent != null) return parent
        }
        return null
    }

    private fun splitInternal(node: BPlusTreeNode<K, V>) {
        val newInternal = BPlusTreeNode<K, V>(isLeaf = false)
        val mid = node.keys.size / 2
        val parentKey = node.keys[mid]

        newInternal.keys.addAll(node.keys.subList(mid + 1, node.keys.size))
        newInternal.children.addAll(node.children.subList(mid + 1, node.children.size))
        node.keys = node.keys.subList(0, mid).toMutableList()
        node.children = node.children.subList(0, mid + 1).toMutableList()

        if (node === root) {
            val newRoot = BPlusTreeNode<K, V>(isLeaf = false)
            newRoot.keys.add(parentKey)
            newRoot.children.add(node)
            newRoot.children.add(newInternal)
            root = newRoot
        } else {
            insertIntoParent(node, parentKey, newInternal)
        }
    }

    fun display() {
        var level = mutableListOf(root)
        while (level.isNotEmpty()) {
            val nextLevel = mutableListOf<BPlusTreeNode<K, V>>()
            for (node in level) {
                print("[${node.keys}] ")
                if (!node.isLeaf) nextLevel.addAll(node.children)
            }
            println()
            level = nextLevel
        }
    }
}
