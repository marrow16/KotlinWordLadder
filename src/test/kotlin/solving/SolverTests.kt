package solving

import Puzzle
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import words.Dictionary
import java.util.*

class SolverTests {
    @Test
    fun solveCatToDog() {
        val dictionary = Dictionary.Cache.forWordLength(3)
        val puzzle = Puzzle(dictionary["cat"]!!, dictionary["dog"]!!)
        val solver = Solver(puzzle)
        val solutions: List<Solution> = solver.solve(4)
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
    fun solveColdToWarmAndWarmToCold() {
        val dictionary = Dictionary.Cache.forWordLength(4)
        var puzzle = Puzzle(dictionary["cold"]!!, dictionary["warm"]!!)
        var solver = Solver(puzzle)
        var solutions = solver.solve(5)
        assertEquals(7, solutions.size)
        val explored = solver.exploredCount

        // now do it the other way around..
        puzzle = Puzzle(dictionary["warm"]!!, dictionary["cold"]!!)
        solver = Solver(puzzle)
        solutions = solver.solve(5)
        assertEquals(7, solutions.size)
        assertEquals(explored, solver.exploredCount)
    }

    @Test
    fun solveKataToJava() {
        val dictionary = Dictionary.Cache.forWordLength(4)
        val puzzle = Puzzle(dictionary["kata"]!!, dictionary["java"]!!)
        val solver = Solver(puzzle)
        val solutions = solver.solve(3)
        assertEquals(1, solutions.size)
        val solution = solutions[0]
        assertEquals("KATA", solution[0].toString())
        assertEquals("KAVA", solution[1].toString())
        assertEquals("JAVA", solution[2].toString())
    }

    @Test
    fun sameWordIsSolvable() {
        val dictionary = Dictionary.Cache.forWordLength(3)
        val puzzle = Puzzle(dictionary["cat"]!!, dictionary["cat"]!!)
        val solver = Solver(puzzle)
        val solutions = solver.solve(1)
        assertEquals(1, solutions.size)
        assertEquals(0, solver.exploredCount)
    }

    @Test
    fun oneLetterDifferenceIsSolvable() {
        val dictionary = Dictionary.Cache.forWordLength(3)
        val puzzle = Puzzle(dictionary["cat"]!!, dictionary["cot"]!!)
        val solver = Solver(puzzle)
        val solutions = solver.solve(2)
        assertEquals(1, solutions.size)
        assertEquals(0, solver.exploredCount)
    }

    @Test
    fun twoLettersDifferenceIsSolvable() {
        val dictionary = Dictionary.Cache.forWordLength(3)
        val puzzle = Puzzle(dictionary["cat"]!!, dictionary["bar"]!!)
        val solver = Solver(puzzle)
        val solutions = solver.solve(3)
        assertEquals(2, solutions.size)
        assertEquals(0, solver.exploredCount)
    }

    @Test
    fun everythingUnsolvableWithBadMaxLadderLength() {
        val dictionary = Dictionary.Cache.forWordLength(3)
        val puzzle = Puzzle(dictionary["cat"]!!, dictionary["dog"]!!)
        val solver = Solver(puzzle)
        val solutions = solver.solve(0)
        assertEquals(0, solutions.size)
        assertEquals(0, solver.exploredCount)
    }

    @Test
    fun shortCircuitOnOneLetterDifference() {
        val dictionary = Dictionary.Cache.forWordLength(3)
        val puzzle = Puzzle(dictionary["cat"]!!, dictionary["cot"]!!)
        val solver = Solver(puzzle)
        val solutions = solver.solve(3)
        assertEquals(3, solutions.size)
        assertEquals(0, solver.exploredCount)

        assertEquals(2, solutions[0].size)
        assertEquals(3, solutions[1].size)
        assertEquals(3, solutions[2].size)
    }
}