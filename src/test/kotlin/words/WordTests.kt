package words

import exceptions.BadWordException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

class WordTests {
    @Test
    fun canCreateWord() {
        val word = Word("cat")
        assertEquals("CAT", word.toString())
    }

    @Test
    fun failsToCreateWordWithReservedChar() {
        assertThrows(
            BadWordException::class.java
        ) { Word("c_t") }
    }

    @Test
    fun variationPatternsAreCorrect() {
        val word = Word("cat")
        val variants: List<String> = word.variationPatterns
        assertEquals(3, variants.size)
        assertEquals("_AT", variants[0])
        assertEquals("C_T", variants[1])
        assertEquals("CA_", variants[2])
    }

    @Test
    fun differencesAreCorrect() {
        val cat = Word("cat")
        val cot = Word("cot")
        val dog = Word("dog")
        assertEquals(0, cat - cat)
        assertEquals(0, cot - cot)
        assertEquals(0, dog - dog)
        assertEquals(1, cat - cot)
        assertEquals(1, cot - cat)
        assertEquals(2, cot - dog)
        assertEquals(2, dog - cot)
        assertEquals(3, cat - dog)
        assertEquals(3, dog - cat)
    }

    @Test
    fun equalityCheck() {
        val word1 = Word("cat")
        val word2 = Word("CAT")
        assertTrue(word1.equals(word2))
        val word3 = Word("dog")
        assertFalse(word1.equals(word3))
        assertFalse(word1.equals(null))
        assertFalse(word1.equals(Any()))
    }
}