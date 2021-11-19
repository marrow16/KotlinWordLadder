package words

import exceptions.BadWordException

private const val VARIATION_CHAR = '_'

class Word(actualWord: String) {
    private val actualWord = actualWord.uppercase()
    private val wordChars = this.actualWord.toCharArray()
    private val hash = this.actualWord.hashCode()
    internal val linked: MutableList<Word> = ArrayList()

    init {
        if (wordChars.contains(VARIATION_CHAR)) {
            throw BadWordException("Word cannot contain '$VARIATION_CHAR'")
        }
    }

    val variationPatterns: List<String> get() = List(wordChars.size) { i ->
        val chars = wordChars.clone()
        chars[i] = VARIATION_CHAR
        String(chars)
    }

    val length: Int get() = actualWord.length

    val isIslandWord: Boolean get() = linked.isEmpty()

    val linkedWords: List<Word> get() = this.linked

    fun differences(other: Word): Int {
        var result = 0
        for (ch in wordChars.indices) {
            result += if (wordChars[ch] != other.wordChars[ch]) 1 else 0
        }
        return result
    }

    override fun toString(): String = this.actualWord

    override fun hashCode(): Int = this.hash

    override fun equals(other: Any?): Boolean
        = (other is Word)
                && other.actualWord == this.actualWord
}