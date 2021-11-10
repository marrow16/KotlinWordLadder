package words

import exceptions.BadWordException
import exceptions.DictionaryLoadErrorException
import exceptions.NoResourceForDictionaryException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.function.Consumer

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
                    "Word '" + word + "' (length = "
                            + word.length + ") cannot be loaded into " + wordLength + " letter word dictionary"
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
        val resource =
            Dictionary::class.java.getResource(wordLength.toString() + RESOURCE_NAME_SUFFIX)
                ?: throw NoResourceForDictionaryException(
                    "Dictionary resource for word length "
                            + wordLength + " does not exist"
                )
        try {
            Files.lines(Paths.get(resource.toURI()))
                .forEach { word: String -> this.addWord(word) }
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