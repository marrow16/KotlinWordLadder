package solving

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import words.Dictionary
import java.util.ArrayList

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

    @Test
    fun sortsCorrectlyByLength() {
        val solution1 = Solution(dictionary!!["cat"]!!, dictionary!!["cot"]!!, dictionary!!["cog"]!!)
        val solution2 = Solution(dictionary!!["cat"]!!, dictionary!!["bat"]!!)
        val solutions: MutableList<Solution> = ArrayList()
        solutions.add(solution1)
        solutions.add(solution2)
        val sorted = solutions.sorted()
        assertEquals(solution2, sorted[0])
        assertEquals(solution1, sorted[1])
    }

    @Test
    fun sortsCorrectlyByWords() {
        val solution1 = Solution(dictionary!!["cat"]!!, dictionary!!["cot"]!!)
        val solution2 = Solution(dictionary!!["cat"]!!, dictionary!!["bat"]!!)
        val solution3 = Solution(dictionary!!["bat"]!!, dictionary!!["bar"]!!)
        val solutions: MutableList<Solution> = ArrayList()
        solutions.add(solution1)
        solutions.add(solution2)
        solutions.add(solution3)
        val sorted = solutions.sorted()
        assertEquals(solution3, sorted[0])
        assertEquals(solution2, sorted[1])
        assertEquals(solution1, sorted[2])
    }

    @Test
    fun sortsCorrectlyByLengthAndWords() {
        val solutionA = Solution(dictionary!!["cat"]!!, dictionary!!["cot"]!!, dictionary!!["cog"]!!)
        val solutionB = Solution(dictionary!!["cat"]!!, dictionary!!["cor"]!!, dictionary!!["bar"]!!)
        val solution1 = Solution(dictionary!!["cat"]!!, dictionary!!["cot"]!!)
        val solution2 = Solution(dictionary!!["cat"]!!, dictionary!!["bat"]!!)
        val solution3 = Solution(dictionary!!["bat"]!!, dictionary!!["bar"]!!)
        val solutions: MutableList<Solution> = ArrayList()
        solutions.add(solutionA)
        solutions.add(solutionB)
        solutions.add(solution1)
        solutions.add(solution2)
        solutions.add(solution3)
        val sorted = solutions.sorted()
        assertEquals(solution3, sorted[0])
        assertEquals(solution2, sorted[1])
        assertEquals(solution1, sorted[2])
        assertEquals(solutionB, sorted[3])
        assertEquals(solutionA, sorted[4])
    }
}