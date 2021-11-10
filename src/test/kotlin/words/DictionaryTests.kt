package words

import exceptions.NoResourceForDictionaryException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DictionaryTests {
    private val VALID_DICTIONARY_LENGTHS = intArrayOf(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
    private val EXPECTED_DICTIONARY_SIZES: Map<Int, Int> = mapOf(
        Pair(2, 127),
        Pair(3, 1347),
        Pair(4, 5638),
        Pair(5, 12972),
        Pair(6, 23033),
        Pair(7, 34342),
        Pair(8, 42150),
        Pair(9, 42933),
        Pair(10, 37235),
        Pair(11, 29027),
        Pair(12, 21025),
        Pair(13, 14345),
        Pair(14, 9397),
        Pair(15, 5925)
    )

    @Test
    fun canLoadDictionaries() {
        for (wordLength in VALID_DICTIONARY_LENGTHS) {
            val dictionary = Dictionary(wordLength)
            assertNotNull(dictionary)
            assertFalse(dictionary.isEmpty())
            assertEquals(EXPECTED_DICTIONARY_SIZES[wordLength], dictionary.size)
        }
    }

    @Test
    fun invalidLengthDictionaryFailsToLoad() {
        assertThrows(
            NoResourceForDictionaryException::class.java,
            { Dictionary(VALID_DICTIONARY_LENGTHS[0] - 1) })
    }

    @Test
    fun invalidLengthDictionaryFailsToLoad2() {
        assertThrows(
            NoResourceForDictionaryException::class.java,
            { Dictionary(VALID_DICTIONARY_LENGTHS[VALID_DICTIONARY_LENGTHS.size - 1] + 1) })
    }

    @Test
    fun canLoadDictionariesFromFactory() {
        for (wordLength in VALID_DICTIONARY_LENGTHS) {
            val dictionary = Dictionary.Factory.forWordLength(wordLength)
            assertNotNull(dictionary)
            assertFalse(dictionary.isEmpty())
        }
    }

    @Test
    fun dictionaryWordHasVariants() {
        val dictionary = Dictionary.Factory.forWordLength(3)
        val word: Word? = dictionary["cat"]
        assertNotNull(word)
        val wordVariants: List<Word> = word!!.linkedWords
        assertFalse(wordVariants.isEmpty())
        // and check the word itself is not in its list of variants...
        assertFalse(wordVariants.contains(word))
    }

    @Test
    fun dictionaryWordIsIslandWord() {
        val dictionary = Dictionary.Factory.forWordLength(3)
        val word: Word? = dictionary["IWI"]
        assertNotNull(word)
        val wordVariants: List<Word> = word!!.linkedWords
        assertTrue(wordVariants.isEmpty())
        assertTrue(word.isIslandWord)
    }

    @Test
    fun differencesBetweenLinkedWords() {
        val dictionary = Dictionary.Factory.forWordLength(3)
        val word: Word? = dictionary["cat"]
        assertNotNull(word)
        assertFalse(word!!.linkedWords.isEmpty())
        for (linkedWord in word.linkedWords) {
            assertEquals(1, word.differences(linkedWord))
        }
    }

    @Test
    fun wordsAreInterLinked() {
        val dictionary = Dictionary.Factory.forWordLength(3)
        val word: Word? = dictionary["cat"]
        assertNotNull(word)
        assertFalse(word!!.linkedWords.isEmpty())
        for (linkedWord in word.linkedWords) {
            assertTrue(linkedWord.linkedWords.contains(word))
        }
    }
}