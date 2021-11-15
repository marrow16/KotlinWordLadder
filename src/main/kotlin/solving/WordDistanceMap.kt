package solving

import words.Word
import java.util.*

class WordDistanceMap(word: Word) {
    private val distances: MutableMap<Word, Int> = HashMap<Word, Int>()
    private var maximumLadderLength = 0

    init {
        distances[word] = 1
        val queue: Queue<Word> = ArrayDeque()
        queue.add(word)
        while (!queue.isEmpty()) {
            val nextWord: Word = queue.remove()
            nextWord.linkedWords.stream()
                .filter { linkedWord -> !distances.containsKey(linkedWord) }
                .forEach { linkedWord ->
                    queue.add(linkedWord)
                    distances.computeIfAbsent(linkedWord) { w: Word? ->
                        1 + distances[nextWord]!!
                    }
                }
        }
    }

    operator fun get(toWord: Word): Int? = distances[toWord]

    internal fun setMaximumLadderLength(maximumLadderLength: Int) {
        this.maximumLadderLength = maximumLadderLength
    }

    internal fun reachable(word: Word): Boolean {
        val distance = distances.getOrDefault(word, -1)
        return (distance != -1
                && distance <= maximumLadderLength)
    }

    internal fun reachable(word: Word, existingSize: Int): Boolean {
        val distance = distances.getOrDefault(word, -1)
        return (distance != -1
                && distance + existingSize <= maximumLadderLength)
    }


}