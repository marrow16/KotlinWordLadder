import exceptions.PuzzleErrorException
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
}