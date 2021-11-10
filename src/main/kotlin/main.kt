import solving.Options
import solving.Solver
import solving.WordDistanceMap
import words.Dictionary
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size >= 2) {
        solve(args)
    } else {
        Interactive(args)
    }
}

private fun solve(args: Array<String>) {
    try {
        val first = args[0]
        val second = args[1]
        if (first.length != second.length) {
            throw Exception("Words supplied as args must be same length")
        }
        val loadStartTime = System.nanoTime()
        val dictionary = Dictionary.Factory.fromWord(first)
        val loadEndTime = System.nanoTime()
        val firstWord = dictionary[first] ?: throw Exception("Word '$first' does not exist in dictionary")
        val secondWord = dictionary[second] ?: throw Exception("Word '$second' does not exist in dictionary")
        val loadTime: Double = loadEndTime.toDouble() - loadStartTime
        println("Dictionary load time: " + FORMAT_MILLIS.format(loadTime / 1000000) + "ms")
        var maxLadderLength: Int = -1
        if (args.size >= 3) {
            maxLadderLength = Integer.parseInt(args[2])
            if (maxLadderLength < 1 || maxLadderLength > 20) {
                throw Exception("Maximum ladder length (3rd arg) must be between 1 and 20")
            }
        } else {
            val calcStartTime = System.nanoTime()
            val distanceMap = WordDistanceMap(firstWord)
            maxLadderLength = distanceMap[secondWord]
                ?: throw Exception("Cannot solve '$firstWord` to `$secondWord'")
            val calcEndTime = System.nanoTime()
            val calcTime: Double = calcEndTime.toDouble() - calcStartTime
            println("Took " + FORMAT_MILLIS.format(calcTime / 1000000) + "ms to determine minimum ladder length of " + maxLadderLength)
        }
        val solver = Solver(Puzzle(firstWord, secondWord), Options(maxLadderLength))
        val solveStartTime = System.nanoTime()
        val solutions = solver.solve()
        val solveEndTime = System.nanoTime()
        val solveTime: Double = solveEndTime.toDouble() - solveStartTime
        println("Took "
                + FORMAT_MILLIS.format(solveTime / 1000000)
                + "ms to find " + FORMAT_COUNT.format(solutions.size) + " solutions"
                + " (explored " + FORMAT_COUNT.format(solver.getExploredCount()) + " solutions)")
        val sortedSolutions = solutions.sorted()
        sortedSolutions.forEach { solution -> println(solution) }
    } catch (e: Exception) {
        System.err.println(e.javaClass.simpleName + ": " + e.message)
        exitProcess(1)
    }
}