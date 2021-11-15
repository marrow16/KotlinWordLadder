import exceptions.BadWordException
import exceptions.PuzzleErrorException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import words.Dictionary

class PuzzleTests {
    @Test
    fun minimumLadderForCatToDog() {
        val dictionary = Dictionary.Factory.forWordLength(3)
        val puzzle = Puzzle(dictionary["cat"]!!, dictionary["dog"]!!)
        val minimumLadderLength: Int? = puzzle.calculateMinimumLadderLength()
        assertNotNull(minimumLadderLength)
        assertEquals(4, minimumLadderLength)
    }

    @Test
    fun minimumLadderForColdToWarm() {
        val dictionary = Dictionary.Factory.forWordLength(4)
        val puzzle = Puzzle(dictionary["cold"]!!, dictionary["warm"]!!)
        val minimumLadderLength: Int? = puzzle.calculateMinimumLadderLength()
        assertNotNull(minimumLadderLength)
        assertEquals(5, minimumLadderLength)
    }

    @Test
    fun minimumLadderForKataToJava() {
        val dictionary = Dictionary.Factory.forWordLength(4)
        val puzzle = Puzzle(dictionary["kata"]!!, dictionary["java"]!!)
        val minimumLadderLength: Int? = puzzle.calculateMinimumLadderLength()
        assertNotNull(minimumLadderLength)
        assertEquals(3, minimumLadderLength)
    }

    @Test
    fun cannotSolveLlamaToArtsy() {
        val dictionary = Dictionary.Factory.forWordLength(5)
        val puzzle = Puzzle(dictionary["llama"]!!, dictionary["artsy"]!!)
        val minimumLadderLength: Int? = puzzle.calculateMinimumLadderLength()
        assertNull(minimumLadderLength)

        // do it again using short-cut method
        assertFalse(puzzle.isSolvable())
    }

    @Test
    fun constructorFailsWithIslandWords() {
        val dictionary = Dictionary.Factory.forWordLength(3)
        assertThrows(
            PuzzleErrorException::class.java
        ) { Puzzle(dictionary["iwi"]!!, dictionary["cat"]!!) }

        assertThrows(
            PuzzleErrorException::class.java
        ) { Puzzle(dictionary["cat"]!!, dictionary["iwi"]!!) }

        val puzzle = Puzzle(dictionary["cat"]!!, dictionary["cat"]!!)
        assertTrue(puzzle.isSolvable())
    }

    @Test
    fun constructorFailsWithDifferentLengthWords() {
        val dictionary2 = Dictionary.Factory.forWordLength(2)
        val dictionary3 = Dictionary.Factory.forWordLength(3)
        assertThrows(
            PuzzleErrorException::class.java
        ) { Puzzle(dictionary2["of"]!!, dictionary3["cat"]!!) }
    }
}