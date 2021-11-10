import solving.Options
import solving.Solution
import solving.Solver
import words.Dictionary
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.atomic.AtomicLong

private const val APP_NAME = "WordLadder"
private const val PROMPT = "$APP_NAME> "
private const val TOOK = "  Took "
private const val USE_TERMINAL_COLOURS = true
private const val TERMINAL_COLOUR_RED = "\u001b[31m"
private const val TERMINAL_COLOUR_GREEN = "\u001b[32m"
private const val TERMINAL_COLOUR_BLACK = "\u001b[0m"

private const val DEFAULT_LIMIT = 10
private const val NANOS_IN_MILLI = 1000000.0
private const val MAX_MILLIS = 1000.0

private const val MINIMUM_WORD_LENGTH = 2
private const val MAXIMUM_WORD_LENGTH = 15
private const val MINIMUM_LADDER_LENGTH = 1
private const val MAXIMUM_LADDER_LENGTH = 20

val FORMAT_SECONDS = DecimalFormat("#,##0.000")
val FORMAT_MILLIS = DecimalFormat("#0.00")
val FORMAT_COUNT = DecimalFormat("#,##0")

class Interactive(args: Array<String>) {
    private var startWord: String? = null
    private var finalWord: String? = null
    private var maximumLadderLength: Int? = null
    private var onStep: Step = Step.GET_START_WORD
    private var dictionary: Dictionary? = null
    private var dictionaryLoadTime: Double? = null

    init {
        processStepsFromArgs(args)
        run()
    }

    private fun run() {
        var again = true
        while (again) {
            while (onStep != Step.DONE) {
                processStepInputs()
            }

            solve()

            println()
            print(PROMPT + "Run again? [y/n]: ")
            val input = readLine()?: ""
            println()
            again = input != "n"
            onStep = Step.GET_START_WORD
        }
    }

    private fun solve() {
        println(TOOK + green(tookMs(dictionaryLoadTime!!)) + " to load dictionary")
        val options = Options()
        val puzzle = Puzzle(dictionary!![startWord!!]!!, dictionary!![finalWord!!]!!)
        val solver = Solver(puzzle, options)
        if (maximumLadderLength?: -1 == -1) {
            val startTime: Double = System.nanoTime().toDouble()
            maximumLadderLength = solver.calculateMinimumLadderLength()
            val took: Double = System.nanoTime().minus(startTime)
            if (maximumLadderLength == null) {
                println(red("Cannot solve '$startWord' to '$finalWord'"))
                return
            }
            println(TOOK + green(tookMs(took)) + " to determine minimum ladder length of " + green(maximumLadderLength!!))
            options.maximumLadderLength = maximumLadderLength!!
        }
        options.maximumLadderLength = maximumLadderLength!!
        val startTime: Double = System.nanoTime().toDouble()
        val solutions = solver.solve()
        val took: Double = System.nanoTime().minus(startTime)
        if (solutions.isEmpty()) {
            println(red(TOOK + tookMs(took)
                    + " to find no solutions (explored " + FORMAT_COUNT.format(solver.getExploredCount()) + " solutions)"))
        } else {
            println(TOOK + green(tookMs(took))
                    + " to find " + green(FORMAT_COUNT.format(solutions.size)) + " solutions"
                    + " (explored " + green(FORMAT_COUNT.format(solver.getExploredCount())) + " solutions)" )
            displaySolutions(solutions)
        }
    }

    private fun displaySolutions(solutions: List<Solution>) {
        val sortedSolutions = solutions.sorted()
        val pageStart = AtomicLong(0)
        while (pageStart.get() < sortedSolutions.size) {
            print(PROMPT + "List" + (if (pageStart.get() == 0L) "" else " more")
                    + " solutions? (Enter 'n' for no, 'y' or return for next 10, 'all' for all or how many): ")
            val input: String = readLine()?: ""
            if (("n" == input)) {
                break
            }
            var limit: Long = DEFAULT_LIMIT.toLong()
            if (("all" == input)) {
                limit = sortedSolutions.size.toLong()
            } else if (input.isNotEmpty() && "y" != input) {
                try {
                    limit = input.toLong()
                } catch (e: NumberFormatException) {
                    // ignore
                }
            }
            sortedSolutions.stream()
                .skip(pageStart.get())
                .limit(limit)
                .forEach { solution: Solution -> println("  " + pageStart.incrementAndGet()
                        + "/" + sortedSolutions.size + "  " + green(solution))
                }
        }
    }

    private fun processStepInputs() {
        print(onStep.prompt)
        val input = readLine()?: ""
        if (when (onStep) {
                Step.GET_START_WORD -> setStartWord(input)
                Step.GET_FINAL_WORD -> setFinalWord(input)
                Step.GET_MAXIMUM_LADDER_LENGTH -> setMaximumLadderLength(input)
                Step.DONE -> true
            }) {
            onStep = onStep.nextStep()
        }
    }

    private fun processStepsFromArgs(args: Array<String>) {
        if (args.isNotEmpty()) {
            println(Step.GET_START_WORD.prompt + green(args[0]))
            if (setStartWord(args[0])) {
                onStep = onStep.nextStep()
            }
        }
        if (args.size > 1 && onStep == Step.GET_FINAL_WORD) {
            println(Step.GET_FINAL_WORD.prompt + green(args[1]))
            if (setFinalWord(args[1])) {
                onStep = onStep.nextStep()
            }
        }
        if (args.size > 2 && onStep == Step.GET_MAXIMUM_LADDER_LENGTH) {
            println(Step.GET_MAXIMUM_LADDER_LENGTH.prompt + green(args[2]))
            if (setMaximumLadderLength(args[2])) {
                onStep = onStep.nextStep()
            }
        }
    }

    private fun loadDictionary(word: String): Boolean {
        val startTime:Double = System.nanoTime().toDouble()
        dictionary = try { Dictionary.Factory.fromWord(word) }
            catch (e: Exception) { null }
        if (dictionary == null) {
            println(red("              Failed to load dictionary!"))
            return false
        }
        dictionaryLoadTime = System.nanoTime() - startTime
        return true
    }

    private fun setStartWord(word: String): Boolean {
        if (word.length < MINIMUM_WORD_LENGTH || word.length > MAXIMUM_WORD_LENGTH) {
            println(red("            Please enter a word with between $MINIMUM_WORD_LENGTH and $MAXIMUM_WORD_LENGTH characters!"))
            return false
        }
        if (loadDictionary(word)) {
            if (word !in dictionary!!) {
                println(red("            Word '$word' does not exist!"))
                return false
            }
            startWord = word
            return true
        }
        return false
    }

    private fun setFinalWord(word: String): Boolean {
        if (word.length != startWord!!.length) {
            println(red("            Final word length must match start word length!"))
            return false
        }
        if (word !in dictionary!!) {
            println(red("            Word '$word' does not exist!"))
            return false
        }
        finalWord = word
        return true
    }

    private fun setMaximumLadderLength(input: String): Boolean {
        if (input.isEmpty()) {
            println(green("            No answer - assuming auto calc of minimum ladder length"))
            maximumLadderLength = -1
            return true
        }
        maximumLadderLength = try {
            Integer.parseInt(input)
        } catch (e: Exception) { null }
        if (maximumLadderLength == null
            || maximumLadderLength!! < MINIMUM_LADDER_LENGTH || maximumLadderLength!! > MAXIMUM_LADDER_LENGTH) {
            println(red("            Please enter a number between $MINIMUM_LADDER_LENGTH and $MAXIMUM_LADDER_LENGTH"))
            return false
        }
        return true
    }

    private enum class Step(val order: Int, val prompt: String?) {
        GET_START_WORD(0, "${PROMPT}Enter start word: "),
        GET_FINAL_WORD(1, "${PROMPT}Enter final word: "),
        GET_MAXIMUM_LADDER_LENGTH(2, "${PROMPT}Maximum ladder length? [$MINIMUM_LADDER_LENGTH-$MAXIMUM_LADDER_LENGTH, or return]: "),
        DONE(3, null);

        fun nextStep(): Step {
            if (this == DONE) {
                return this
            }
            for (step: Step in values()) {
                if (step.order > order) {
                    return step
                }
            }
            return DONE
        }
    }
}

private fun red(message: Any): String {
    return if (USE_TERMINAL_COLOURS) {
        TERMINAL_COLOUR_RED + message + TERMINAL_COLOUR_BLACK
    } else Objects.toString(message)
}

private fun green(message: Any): String {
    return if (USE_TERMINAL_COLOURS) {
        TERMINAL_COLOUR_GREEN + message + TERMINAL_COLOUR_BLACK
    } else Objects.toString(message)
}

private fun tookMs(nanos: Double): String {
    val millis: Double = nanos / NANOS_IN_MILLI
    return if (millis >= MAX_MILLIS) {
        FORMAT_SECONDS.format(millis / MAX_MILLIS) + "sec"
    } else FORMAT_MILLIS.format(millis) + "ms"
}
