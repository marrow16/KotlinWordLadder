package solving

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import words.Dictionary

class SolutionTests {
    private var dictionary: Dictionary? = null

    @BeforeEach
    fun before() {
        dictionary = Dictionary.Factory.forWordLength(3)
    }

    @Test
    fun constructors() {
        val solution = Solution(dictionary!!["cat"]!!, dictionary!!["bat"]!!)
        Assertions.assertNotNull(solution)
    }

    @Test
    fun checkSize() {
        val solution = Solution(dictionary!!["cat"]!!, dictionary!!["bat"]!!)
        assertEquals(2, solution.size)
    }

    @Test
    fun checkToString() {
        val solution = Solution(dictionary!!["cat"]!!, dictionary!!["bat"]!!)
        assertEquals("[CAT, BAT]", solution.toString())
    }
}