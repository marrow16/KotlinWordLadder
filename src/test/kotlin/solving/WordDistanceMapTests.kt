package solving

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import words.Dictionary
import words.Word
import kotlin.test.assertEquals
import kotlin.test.assertFails

class WordDistanceMapTests {
    @Test
    fun islandWordHasLimitedMap() {
        val dictionary = Dictionary.Factory.forWordLength(3)
        val word: Word = dictionary["iwi"]!!
        val wordDistanceMap = WordDistanceMap(word)

        assertEquals(1, wordDistanceMap.distances.size)
        assertTrue(word in wordDistanceMap.distances)
    }

    @Test
    fun catMap() {
        val dictionary = Dictionary.Factory.forWordLength(3)
        val word: Word = dictionary["cat"]!!
        val wordDistanceMap = WordDistanceMap(word)

        assertEquals(1346, wordDistanceMap.distances.size)
        assertTrue(word in wordDistanceMap.distances)
        assertEquals(1, wordDistanceMap.distances[word])

        val endWord: Word = dictionary["dog"]!!
        assertTrue(endWord in wordDistanceMap.distances)

        assertTrue(wordDistanceMap.reachable(endWord, 5))
        assertTrue(wordDistanceMap.reachable(endWord, 4))
        assertFalse(wordDistanceMap.reachable(endWord, 3))
        assertFalse(wordDistanceMap.reachable(endWord, 2))
    }
}