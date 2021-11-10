package solving

import words.Word
import java.util.*

class Solution: Comparable<Solution> {
    private var ladder: List<Word>

    constructor(candidateSolution: CandidateSolution, reversed: Boolean) {
        ladder = if (reversed) {
            val copy: MutableList<Word> = ArrayList(candidateSolution.ladder)
            copy.reverse()
            Collections.unmodifiableList(copy)
        } else {
            Collections.unmodifiableList(candidateSolution.ladder)
        }
    }

    constructor(vararg words: Word) {
        ladder = words.asList()
    }

    val size: Int get() = ladder.size

    operator fun get(index: Int): Word = ladder[index]

    override fun toString(): String = ladder.toString()

    override fun compareTo(other: Solution): Int {
        val sizeCompare = ladder.size.compareTo(other.ladder.size)
        if (sizeCompare == 0) {
            var wordCompare = 0
            var w = 0
            while (w < ladder.size - 1 && wordCompare == 0) {
                wordCompare = ladder[w].toString().compareTo(other.ladder[w].toString())
                w++
            }
            return wordCompare
        }
        return sizeCompare
    }
}