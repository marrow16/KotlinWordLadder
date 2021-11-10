package solving

const val DEFAULT_MAXIMUM_LADDER_LENGTH = 6

class Options() {
    var maximumLadderLength = DEFAULT_MAXIMUM_LADDER_LENGTH

    constructor(maximumLadderLength: Int) : this() {
        this.maximumLadderLength = maximumLadderLength
    }
}