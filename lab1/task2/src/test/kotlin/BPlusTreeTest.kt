import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BPlusTreeTest {
    @Test
    fun testInsertAndSearch() {
        val tree = BPlusTree<Int, String>(3)
        tree.insert(10, "Ten")
        tree.insert(20, "Twenty")
        tree.insert(30, "Thirty")
        tree.insert(40, "Forty")
        tree.insert(50, "Fifty")

        assertEquals("Ten", tree.search(10))
        assertEquals("Twenty", tree.search(20))
        assertEquals("Thirty", tree.search(30))
        assertEquals("Forty", tree.search(40))
        assertEquals("Fifty", tree.search(50))
        assertNull(tree.search(60))
    }

    @Test
    fun testInsertDuplicateKey() {
        val tree = BPlusTree<Int, String>(3)
        tree.insert(10, "Ten")
        tree.insert(10, "Updated Ten")

        assertEquals("Updated Ten", tree.search(10))
    }

    @Test
    fun testTreeStructureAfterInsertion() {
        val tree = BPlusTree<Int, String>(3)
        tree.insert(10, "Ten")
        tree.insert(20, "Twenty")
        tree.insert(5, "Five")
        tree.insert(6, "Six")
        tree.insert(15, "Fifteen")

        tree.display() // For debugging
        assertNotNull(tree.search(5))
        assertNotNull(tree.search(6))
        assertNotNull(tree.search(10))
        assertNotNull(tree.search(15))
        assertNotNull(tree.search(20))
    }
    
    // Edge case: Empty tree operations
    @Test
    fun testEmptyTree() {
        val tree = BPlusTree<Int, String>(3)
        
        // Search in empty tree
        assertNull(tree.search(10))
        
        // Display empty tree (shouldn't throw exceptions)
        tree.display()
    }
    
    // Edge case: Tree with minimum valid order (degree = 2)
    @Test
    fun testMinimumOrderTree() {
        val tree = BPlusTree<Int, String>(2)
        
        // Insert multiple elements to force splitting
        for (i in 1..10) {
            tree.insert(i, "Value$i")
        }
        
        // Check all values are accessible
        for (i in 1..10) {
            assertEquals("Value$i", tree.search(i))
        }
    }
    
    // Edge case: Single element tree
    @Test
    fun testSingleElementTree() {
        val tree = BPlusTree<Int, String>(3)
        
        tree.insert(5, "Five")
        
        assertEquals("Five", tree.search(5))
        assertNull(tree.search(6))
    }
    
    // Negative case: Null values
    @Test
    fun testInsertNullValue() {
        val tree = BPlusTree<Int, String?>(3)
        
        tree.insert(10, null)
        
        assertNull(tree.search(10))
        
        // Update null value to non-null
        tree.insert(10, "Ten")
        assertEquals("Ten", tree.search(10))
        
        // Update non-null to null
        tree.insert(10, null)
        assertNull(tree.search(10))
    }
    
    // Corner case: Force multiple splits and rebalancing
    @Test
    fun testForceSplitsAndRebalancing() {
        val tree = BPlusTree<Int, String>(3)
        
        // Insert elements in increasing order to force splits
        for (i in 1..20) {
            tree.insert(i, "Value$i")
        }
        
        // Verify all elements are still accessible
        for (i in 1..20) {
            assertEquals("Value$i", tree.search(i))
        }
        
        // Insert elements in reverse order to test different balancing scenarios
        for (i in 40 downTo 21) {
            tree.insert(i, "Value$i")
        }
        
        // Verify all elements are still accessible
        for (i in 1..40) {
            assertEquals("Value$i", tree.search(i))
        }
    }
    
    // Corner case: Insert elements in random order
    @Test
    fun testRandomOrderInsertion() {
        val tree = BPlusTree<Int, String>(3)
        val numbers = listOf(42, 13, 7, 50, 25, 38, 16, 29, 4, 33)
        
        for (num in numbers) {
            tree.insert(num, "Value$num")
        }
        
        // Verify all elements are accessible
        for (num in numbers) {
            assertEquals("Value$num", tree.search(num))
        }
    }
    
    // Edge case: Very large number of insertions
    @Test
    fun testLargeNumberOfInsertions() {
        val tree = BPlusTree<Int, String>(4)
        
        // Insert a large number of elements
        for (i in 1..1000) {
            tree.insert(i, "Value$i")
        }
        
        // Check a sample of values
        for (i in listOf(1, 50, 100, 500, 750, 999, 1000)) {
            assertEquals("Value$i", tree.search(i))
        }
        
        // Check non-existent values
        assertNull(tree.search(0))
        assertNull(tree.search(1001))
    }
    
    // Corner case: Handling of min/max integer values
    @Test
    fun testMinMaxIntegerValues() {
        val tree = BPlusTree<Int, String>(3)
        
        tree.insert(Int.MIN_VALUE, "Min Value")
        tree.insert(Int.MAX_VALUE, "Max Value")
        tree.insert(0, "Zero")
        
        assertEquals("Min Value", tree.search(Int.MIN_VALUE))
        assertEquals("Max Value", tree.search(Int.MAX_VALUE))
        assertEquals("Zero", tree.search(0))
    }
    
    // Negative case: String keys with special comparisons
    @Test
    fun testStringKeys() {
        val tree = BPlusTree<String, Int>(3)
        
        tree.insert("", 1)
        tree.insert("a", 2)
        tree.insert("A", 3)
        tree.insert("z", 4)
        tree.insert("   ", 5)  // Spaces
        
        assertEquals(1, tree.search(""))
        assertEquals(2, tree.search("a"))
        assertEquals(3, tree.search("A"))
        assertEquals(4, tree.search("z"))
        assertEquals(5, tree.search("   "))
        assertNull(tree.search("b"))
    }
}
