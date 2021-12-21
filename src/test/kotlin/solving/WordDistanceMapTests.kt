package solving

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import words.Dictionary
import words.Word
import kotlin.test.assertEquals

class WordDistanceMapTests {
    @Test
    fun islandWordHasLimitedMap() {
        val dictionary = Dictionary.Cache.forWordLength(3)
        val word: Word = dictionary["iwi"]!!
        val wordDistanceMap = WordDistanceMap(word, null)

        assertEquals(1, wordDistanceMap.distances.size)
        assertTrue(word in wordDistanceMap.distances)
    }

    @Test
    fun catMap() {
        val dictionary = Dictionary.Cache.forWordLength(3)
        val word: Word = dictionary["cat"]!!
        val wordDistanceMap = WordDistanceMap(word, null)

        assertEquals(1346, wordDistanceMap.distances.size)
        assertTrue(word in wordDistanceMap.distances)
        assertEquals(1, wordDistanceMap.distances[word])

        val endWord: Word = dictionary["dog"]!!
        assertTrue(endWord in wordDistanceMap.distances)

        assertTrue(wordDistanceMap.reachable(endWord, 5))
        assertTrue(wordDistanceMap.reachable(endWord, 4))
        assertFalse(wordDistanceMap.reachable(endWord, 3))
        assertFalse(wordDistanceMap.reachable(endWord, 2))
        assertFalse(wordDistanceMap.reachable(endWord, 1))
        assertFalse(wordDistanceMap.reachable(endWord, 0))
        assertFalse(wordDistanceMap.reachable(endWord, -1))
        assertFalse(wordDistanceMap.reachable(endWord, -2))
    }

    @Test
    fun catMapLimited() {
        val dictionary = Dictionary.Cache.forWordLength(3)
        val word: Word = dictionary["cat"]!!
        var wordDistanceMap = WordDistanceMap(word, 4)

        assertEquals(1086, wordDistanceMap.distances.size)
        assertTrue(word in wordDistanceMap.distances)
        assertEquals(1, wordDistanceMap.distances[word])

        val endWord: Word = dictionary["dog"]!!
        assertTrue(endWord in wordDistanceMap.distances)

        assertTrue(wordDistanceMap.reachable(endWord, 5))
        assertTrue(wordDistanceMap.reachable(endWord, 4))
        assertFalse(wordDistanceMap.reachable(endWord, 3))
        assertFalse(wordDistanceMap.reachable(endWord, 2))

        // limit further...
        wordDistanceMap = WordDistanceMap(word, 3)
        assertEquals(345, wordDistanceMap.distances.size)
        assertFalse(endWord in wordDistanceMap.distances)
    }
}