package solving

import Puzzle
import words.Word
import java.util.*
import java.util.concurrent.atomic.AtomicLong

class Solver(private val puzzle: Puzzle) {
    private val explored = AtomicLong()
    private val solutions: MutableList<Solution> = ArrayList()
    private var beginWord: Word = puzzle.startWord
    private var endWord: Word = puzzle.finalWord
    private var reversed = false

    private var maximumLadderLength: Int = -1
    private lateinit var endDistances: WordDistanceMap

    fun solve(maxLadderLength: Int): List<Solution> {
        explored.set(0)
        solutions.clear()
        maximumLadderLength = maxLadderLength
        if (maximumLadderLength < 1) {
            // won't find any solutions with ladder of length 0!...
            return solutions
        }
        beginWord = puzzle.startWord
        endWord = puzzle.finalWord
        reversed = false
        // check for short-circuits...
        when (beginWord.differences(endWord)) {
            0 -> {
                // same word - so there's only one solution...
                solutions.add(Solution(beginWord))
                return solutions
            }
            1 -> {
                // the two words are only one letter different...
                solutions.add(Solution(beginWord, endWord))
                when (maximumLadderLength) {
                    2 ->
                        // maximum ladder is 2 so we already have the only answer...
                        return solutions
                    3 -> {
                        shortCircuitLadderLength3()
                        return solutions
                    }
                }
            }
            2 -> if (maximumLadderLength == 3) {
                shortCircuitLadderLength3()
                return solutions
            }
        }
        // begin with the word that has the least number of linked words...
        // (this reduces the number of pointless solution candidates explored!)
        reversed = beginWord.linkedWords.size > endWord.linkedWords.size
        if (reversed) {
            beginWord = puzzle.finalWord
            endWord = puzzle.startWord
        }
        endDistances = WordDistanceMap(endWord)
        endDistances.setMaximumLadderLength(maximumLadderLength)
        beginWord.linkedWords
            .parallelStream()
            .filter{ linkedWord -> endDistances.reachable(linkedWord)}
            .map { linkedWord -> CandidateSolution(this, beginWord, linkedWord) }
            .forEach(this::solve)
        return solutions
    }

    private fun solve(candidate: CandidateSolution) {
        val lastWord: Word = candidate.ladder.last()
        if (lastWord == endWord) {
            foundSolution(candidate)
        } else if (candidate.ladder.size < maximumLadderLength) {
            lastWord.linkedWords
                .parallelStream()
                .filter { linkedWord -> !candidate.seenWords.contains(linkedWord) }
                .filter { linkedWord -> endDistances.reachable(linkedWord, candidate.ladder.size) }
                .map { linkedWord -> CandidateSolution(candidate, linkedWord) }
                .forEach(this::solve)
        }
    }

    private fun shortCircuitLadderLength3() {
        // we can determine solutions by convergence of the two linked word sets...
        val startLinkedWords: MutableSet<Word> = HashSet(beginWord.linkedWords)
        startLinkedWords.retainAll(endWord.linkedWords)
        for (intermediateWord in startLinkedWords) {
            solutions.add(Solution(beginWord, intermediateWord, endWord))
        }
    }

    @Synchronized
    private fun foundSolution(candidate: CandidateSolution) {
        val solution = Solution(candidate, reversed)
        solutions.add(solution)
    }

    @Synchronized
    fun incrementExplored() {
        explored.incrementAndGet()
    }

    val exploredCount: Long get() = explored.get()
}