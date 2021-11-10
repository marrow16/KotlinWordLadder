package solving

import words.Word
import java.util.HashSet

class CandidateSolution {
    private var solver: Solver
    val seenWords: MutableSet<Word> = HashSet<Word>()
    val ladder: MutableList<Word> = ArrayList()

    constructor(solver: Solver, startWord: Word, nextWord: Word) {
        this.solver = solver
        addWord(startWord)
        addWord(nextWord)
        solver.incrementExplored()
    }

    constructor(ancestor: CandidateSolution, nextWord: Word) {
        solver = ancestor.solver
        seenWords.addAll(ancestor.seenWords)
        ladder.addAll(ancestor.ladder)
        addWord(nextWord)
        solver.incrementExplored()
    }

    private fun addWord(word: Word) {
        seenWords.add(word)
        ladder.add(word)
    }
}