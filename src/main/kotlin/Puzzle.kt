import exceptions.PuzzleErrorException
import solving.WordDistanceMap
import words.Word

class Puzzle(startWord: Word, finalWord: Word) {
    val startWord: Word
    val finalWord: Word

    init {
        if (startWord.isIslandWord) {
            throw PuzzleErrorException("Start word '$startWord' is an island word (varying any character does not create another valid word)")
        }
        if (finalWord.length != startWord.length) {
            throw PuzzleErrorException("End word '$finalWord' (length ${finalWord.length}) must match start word length (${startWord.length})")
        } else if (finalWord.isIslandWord) {
            throw PuzzleErrorException("End word '$finalWord' is an island word (varying any character does not create another valid word)")
        }
        this.startWord = startWord
        this.finalWord = finalWord
    }

    fun calculateMinimumLadderLength(): Int? {
        var start: Word = startWord
        var end: Word = finalWord
        // check for short-circuits...
        when (val differences: Int = start - end) {
            0, 1 -> return differences + 1
            2 -> {
                val startLinkedWords: MutableSet<Word> = HashSet(start.linkedWords)
                startLinkedWords.retainAll(end.linkedWords)
                if (startLinkedWords.isNotEmpty()) {
                    return 3
                }
            }
        }
        if (start.linkedWords.size > end.linkedWords.size) {
            // swap start and end word...
            end = startWord
            start = finalWord
        }
        return WordDistanceMap(start, null)[end]
    }

    fun isSolvable(): Boolean = calculateMinimumLadderLength() != null
}