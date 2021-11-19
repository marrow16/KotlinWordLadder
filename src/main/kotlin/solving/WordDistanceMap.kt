package solving

import words.Word
import java.util.*

class WordDistanceMap(word: Word) {
    internal val distances: MutableMap<Word, Int> = HashMap<Word, Int>()

    init {
        distances[word] = 1
        val queue: Queue<Word> = ArrayDeque()
        queue.add(word)
        while (!queue.isEmpty()) {
            val nextWord: Word = queue.remove()
            val distance = distances.getOrDefault(nextWord, 0) + 1
            nextWord.linkedWords.stream()
                .filter { linkedWord -> !distances.containsKey(linkedWord) }
                .forEach { linkedWord ->
                    queue.add(linkedWord)
                    distances.computeIfAbsent(linkedWord) { distance }
                }
        }
    }

    operator fun get(toWord: Word): Int? = distances[toWord]

    internal fun reachable(word: Word, maximumLadderLength: Int): Boolean {
        return (distances[word]?.compareTo(maximumLadderLength) ?: 1) < 1
    }

    internal fun reachable(word: Word, maximumLadderLength: Int, currentLadderLength: Int): Boolean {
        return (distances[word]?.compareTo(maximumLadderLength - currentLadderLength) ?: 1) < 1
    }
}