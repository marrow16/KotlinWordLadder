package words

import exceptions.BadWordException
import exceptions.DictionaryLoadErrorException
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

private const val RESOURCE_NAME_PREFIX = "/dictionary-"
private const val RESOURCE_NAME_SUFFIX = "-letter-words.txt"

class Dictionary(private var wordLength: Int) {
    val words: MutableMap<String, Word> = HashMap()

    init {
        loadWordsFromResources(WordLinkageBuilder())
    }

    private fun addWord(addWord: String, linkageBuilder: WordLinkageBuilder) {
        if (addWord.isNotEmpty()) {
            if (addWord.length != wordLength) {
                throw BadWordException(
                    "Word '$addWord' (length = ${addWord.length}) cannot be loaded into $wordLength letter word dictionary"
                )
            }
            val word = Word(addWord)
            words[word.actualWord] = word
            linkageBuilder.link(word)
        }
    }

    private fun loadWordsFromResources(linkageBuilder: WordLinkageBuilder) {
        try {
            this::class.java.getResourceAsStream("$RESOURCE_NAME_PREFIX$wordLength$RESOURCE_NAME_SUFFIX").use { inputStream: InputStream? ->
                BufferedReader(InputStreamReader(inputStream!!)).use { br: BufferedReader ->
                    var line: String?
                    while (br.readLine().also { line = it } != null) {
                        this.addWord(line?:"", linkageBuilder)
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

    private class WordLinkageBuilder {
        private val variations: MutableMap<String, MutableList<Word>> = HashMap()

        fun link(word: Word) {
            word.variationPatterns.forEach { variation ->
                val links = variations.computeIfAbsent(variation) { ArrayList() }
                links.forEach { linkedWord ->
                    // add the new word as linked to the existing words...
                    linkedWord.linked.add(word)
                    // add the existing word as linked to the new word...
                    word.linked.add(linkedWord)
                }
                // add the new word to the list of words of this pattern...
                links.add(word)
            }
        }
    }

    /**
     * Dictionary.Cache is a cache of loaded dictionaries
     * (use the Dictionary constructor directly if you don't want to hold onto loaded dictionaries)
     */
    object Cache {
        private val CACHE: MutableMap<Int, Dictionary> = HashMap()

        fun fromWord(word: String): Dictionary =
            forWordLength(word.length)

        fun forWordLength(wordLength: Int): Dictionary =
            CACHE.computeIfAbsent(wordLength) { integer: Int? -> Dictionary(wordLength) }
    }
}