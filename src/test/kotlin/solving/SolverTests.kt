package solving

import Puzzle
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import words.Dictionary
import java.util.*
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class SolverTests {
    @Test
    fun solveCatToDog() {
        val options = Options(4)
        val dictionary = Dictionary.Factory.forWordLength(3)
        val puzzle = Puzzle(dictionary["cat"]!!, dictionary["dog"]!!)
        val solver = Solver(puzzle, options)
        val solutions: List<Solution> = solver.solve()
        assertEquals(4, solutions.size)
        val midWords: MutableMap<Int, MutableSet<String>> = HashMap()
        for (solution in solutions) {
            assertEquals(4, solution.size)
            assertEquals("CAT", solution[0].toString())
            assertEquals("DOG", solution[3].toString())
            midWords.computeIfAbsent(
                1
            ) { integer: Int? -> HashSet() }.add(solution[1].toString())
            midWords.computeIfAbsent(
                2
            ) { integer: Int? -> HashSet() }.add(solution[2].toString())
        }
        assertEquals(2, midWords[1]!!.size)
        assertTrue(midWords[1]!!.contains("CAG"))
        assertTrue(midWords[1]!!.contains("COT"))
        assertEquals(3, midWords[2]!!.size)
        assertTrue(midWords[2]!!.contains("DOT"))
        assertTrue(midWords[2]!!.contains("COG"))
        assertTrue(midWords[2]!!.contains("DAG"))
    }

    @Test
    fun minimumLadderForCatToDog() {
        val options = Options()
        val dictionary = Dictionary.Factory.forWordLength(3)
        val puzzle = Puzzle(dictionary["cat"]!!, dictionary["dog"]!!)
        val solver = Solver(puzzle, options)
        val minimumLadderLength: Int? = solver.calculateMinimumLadderLength()
        assertNotNull(minimumLadderLength)
        assertEquals(4, minimumLadderLength)
    }

    @Test
    fun minimumLadderForColdToWarm() {
        val options = Options()
        val dictionary = Dictionary.Factory.forWordLength(4)
        val puzzle = Puzzle(dictionary["cold"]!!, dictionary["warm"]!!)
        val solver = Solver(puzzle, options)
        val minimumLadderLength: Int? = solver.calculateMinimumLadderLength()
        assertNotNull(minimumLadderLength)
        assertEquals(5, minimumLadderLength)
    }

    @Test
    fun solveColdToWarmAndWarmToCold() {
        val options = Options(5)
        val dictionary = Dictionary.Factory.forWordLength(4)
        var puzzle = Puzzle(dictionary["cold"]!!, dictionary["warm"]!!)
        var solver = Solver(puzzle, options)
        var solutions = solver.solve()
        assertEquals(7, solutions.size)
        val explored = solver.getExploredCount()

        // now do it the other way around..
        puzzle = Puzzle(dictionary["warm"]!!, dictionary["cold"]!!)
        solver = Solver(puzzle, options)
        solutions = solver.solve()
        assertEquals(7, solutions.size)
        assertEquals(explored, solver.getExploredCount())
    }

    @Test
    fun solveKataToJava() {
        val options = Options(3)
        val dictionary = Dictionary.Factory.forWordLength(4)
        val puzzle = Puzzle(dictionary["kata"]!!, dictionary["java"]!!)
        val solver = Solver(puzzle, options)
        val solutions = solver.solve()
        assertEquals(1, solutions.size)
        val solution = solutions[0]
        assertEquals("KATA", solution[0].toString())
        assertEquals("KAVA", solution[1].toString())
        assertEquals("JAVA", solution[2].toString())
    }

    @Test
    fun minimumLadderForKataToJava() {
        val options = Options()
        val dictionary = Dictionary.Factory.forWordLength(4)
        val puzzle = Puzzle(dictionary["kata"]!!, dictionary["java"]!!)
        val solver = Solver(puzzle, options)
        val minimumLadderLength: Int? = solver.calculateMinimumLadderLength()
        assertNotNull(minimumLadderLength)
        assertEquals(3, minimumLadderLength)
    }

    @Test
    fun cannotSolveLlamaToArtsy() {
        val options = Options()
        val dictionary = Dictionary.Factory.forWordLength(5)
        val puzzle = Puzzle(dictionary["llama"]!!, dictionary["artsy"]!!)
        val solver = Solver(puzzle, options)
        val minimumLadderLength: Int? = solver.calculateMinimumLadderLength()
        assertNull(minimumLadderLength)

        // do it again using short-cut method
        assertFalse(solver.isSolvable())
    }

    @Test
    fun sameWordIsSolvable() {
        val options = Options()
        val dictionary = Dictionary.Factory.forWordLength(3)
        val puzzle = Puzzle(dictionary["cat"]!!, dictionary["cat"]!!)
        val solver = Solver(puzzle, options)
        val solutions = solver.solve()
        assertEquals(1, solutions.size)
        assertEquals(0, solver.getExploredCount())
    }

    @Test
    fun oneLetterDifferenceIsSolvable() {
        val options = Options(2)
        val dictionary = Dictionary.Factory.forWordLength(3)
        val puzzle = Puzzle(dictionary["cat"]!!, dictionary["cot"]!!)
        val solver = Solver(puzzle, options)
        val solutions = solver.solve()
        assertEquals(1, solutions.size)
        assertEquals(0, solver.getExploredCount())
    }

    @Test
    fun twoLettersDifferenceIsSolvable() {
        val options = Options(3)
        val dictionary = Dictionary.Factory.forWordLength(3)
        val puzzle = Puzzle(dictionary["cat"]!!, dictionary["bar"]!!)
        val solver = Solver(puzzle, options)
        val solutions = solver.solve()
        assertEquals(2, solutions.size)
        assertEquals(0, solver.getExploredCount())
    }

    @Test
    fun shortCircuitsOnGetMaxLadderLength() {
        val options = Options()
        val dictionary = Dictionary.Factory.forWordLength(3)
        var puzzle = Puzzle(dictionary["cat"]!!, dictionary["bar"]!!)
        var solver = Solver(puzzle, options)
        var maximumLadderLength: Int? = solver.calculateMinimumLadderLength()
        assertNotNull(maximumLadderLength)
        assertEquals(3, maximumLadderLength)

        puzzle = Puzzle(dictionary["cat"]!!, dictionary["bat"]!!)
        solver = Solver(puzzle, options)
        maximumLadderLength = solver.calculateMinimumLadderLength()
        assertNotNull(maximumLadderLength)
        assertEquals(2, maximumLadderLength)
        puzzle = Puzzle(dictionary["cat"]!!, dictionary["cat"]!!)
        solver = Solver(puzzle, options)
        maximumLadderLength = solver.calculateMinimumLadderLength()
        assertNotNull(maximumLadderLength)
        assertEquals(1, maximumLadderLength)
    }

    @Test
    fun everythingUnsolvableWithBadMaxLadderLength() {
        val options = Options(0)
        val dictionary = Dictionary.Factory.forWordLength(3)
        val puzzle = Puzzle(dictionary["cat"]!!, dictionary["dog"]!!)
        val solver = Solver(puzzle, options)
        val solutions = solver.solve()
        assertEquals(0, solutions.size)
        assertEquals(0, solver.getExploredCount())
    }

    @Test
    fun shortCircuitOnOneLetterDifference() {
        val options = Options(3)
        val dictionary = Dictionary.Factory.forWordLength(3)
        val puzzle = Puzzle(dictionary["cat"]!!, dictionary["cot"]!!)
        val solver = Solver(puzzle, options)
        val solutions = solver.solve()
        assertEquals(3, solutions.size)
        assertEquals(0, solver.getExploredCount())

        assertEquals(2, solutions[0].size)
        assertEquals(3, solutions[1].size)
        assertEquals(3, solutions[2].size)
    }
}