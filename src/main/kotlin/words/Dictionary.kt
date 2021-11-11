package words

import exceptions.BadWordException
import exceptions.DictionaryLoadErrorException
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.function.Consumer

private const val RESOURCE_NAME_PREFIX = "/dictionary-"
private const val RESOURCE_NAME_SUFFIX = "-letter-words.txt"

class Dictionary(private var wordLength: Int) {
    val words: MutableMap<String, Word> = HashMap()

    init {
        loadWordsFromResources()
        buildWordVariations()
    }

    private fun addWord(word: String) {
        if (word.isNotEmpty()) {
            if (word.length != wordLength) {
                throw BadWordException(
                    "Word '$word' (length = ${word.length}) cannot be loaded into $wordLength letter word dictionary"
                )
            }
            val upperWord = word.uppercase()
            words[upperWord] = Word(upperWord)
        }
    }

    private fun buildWordVariations() {
        val variations: MutableMap<String, MutableList<Word>> = HashMap()
        words.values
            .forEach(Consumer { word: Word ->
                word.variationPatterns
                    .forEach { variationPattern ->
                        variations.computeIfAbsent(
                            variationPattern
                        ) { s: String? -> ArrayList() }
                            .add(word)
                    }
            })
        variations.values
            .forEach(Consumer<List<Word>> { wordVariants: List<Word> ->
                wordVariants.forEach(
                    Consumer { word: Word -> word.addLinkedWords(wordVariants) })
            })
    }

    private fun loadWordsFromResources() {
        try {
            this::class.java.getResourceAsStream("$RESOURCE_NAME_PREFIX$wordLength$RESOURCE_NAME_SUFFIX").use { inputStream: InputStream? ->
                BufferedReader(InputStreamReader(inputStream!!)).use { br: BufferedReader ->
                    var line: String?
                    while (br.readLine().also { line = it } != null) {
                        this.addWord(line?:"")
                    }
                }
            }
        } catch (e: Exception) {
            throw DictionaryLoadErrorException("Error loading $wordLength letter word dictionary")
        }
    }

    val size: Int get() = words.size

    fun isEmpty(): Boolean {
        return words.isEmpty()
    }

    operator fun get(word: String): Word? = words[word.uppercase()]

    operator fun contains(word: String): Boolean = words.containsKey(word.uppercase())

    object Factory {
        private val CACHE: MutableMap<Int, Dictionary> = HashMap()

        fun fromWord(word: String): Dictionary =
            forWordLength(word.length)

        fun forWordLength(wordLength: Int): Dictionary =
            CACHE.computeIfAbsent(wordLength) { integer: Int? -> Dictionary(wordLength) }
    }
}