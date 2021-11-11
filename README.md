# KotlinWordLadder

Kotlin [Word Ladder](https://en.wikipedia.org/wiki/Word_ladder) solver

### Running

In IDE, run `main.kt`

Or use the built jar (see `built/kotlinWordLadder.jar`) and run from terminal...
```shell
$ java -jar kotlinWordLadder.jar

WordLadder> Enter start word: cat
WordLadder> Enter final word: dog
WordLadder> Maximum ladder length? [1-20, or return]: 
            No answer - assuming auto calc of minimum ladder length
  Took 53.31ms to load dictionary
  Took 15.36ms to determine minimum ladder length of 4
  Took 16.63ms to find 4 solutions (explored 38 solutions)
WordLadder> List solutions? (Enter 'n' for no, 'y' or return for next 10, 'all' for all or how many): 
  1/4  [CAT, CAG, COG, DOG]
  2/4  [CAT, CAG, DAG, DOG]
  3/4  [CAT, COT, COG, DOG]
  4/4  [CAT, COT, DOT, DOG]

WordLadder> Run again? [y/n]: n
```
Or from terminal with start and final word args...
```shell
$ java -jar kotlinWordLadder.jar cat dog

Took 91.78ms to load dictionary
Took 16.09ms to determine minimum ladder length of 4
Took 20.31ms to find 4 solutions (explored 38 solutions)
[CAT, CAG, COG, DOG]
[CAT, CAG, DAG, DOG]
[CAT, COT, COG, DOG]
[CAT, COT, DOT, DOG]
```