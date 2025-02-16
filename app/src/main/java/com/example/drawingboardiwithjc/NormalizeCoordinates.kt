import kotlin.math.sqrt
import com.example.drawingboardiwithjc.DrawingView
import kotlin.math.round
import kotlin.math.roundToInt

fun computeCG(points: List<Pair<Double, Double>>): Pair<Double, Double> {
    if (points.isEmpty()) return Pair(0.0, 0.0)
    val sumX = points.sumOf { it.first }
    val sumY = points.sumOf { it.second }
    val count = points.size.toDouble()
    return Pair(round(sumX / count * 100) / 100, round(sumY / count * 100) / 100)
}

fun calculateDistance(p1: Pair<Double, Double>, p2: Pair<Double, Double>): Double {
    return round(sqrt((p2.first - p1.first) * (p2.first - p1.first) + (p2.second - p1.second) * (p2.second - p1.second)) * 100) / 100
}

/*Below Code output format is a Single list.*/
fun processSingleList(points: List<Pair<Int, Int>>): List<Double> {
    val filteredPoints = points.map { Pair(it.first.toDouble(), it.second.toDouble()) }
    val mainCG = computeCG(filteredPoints)

    val (CGx, CGy) = mainCG
    val quadrants = mutableMapOf("Q1" to mutableListOf<Pair<Double, Double>>(),
        "Q2" to mutableListOf(),
        "Q3" to mutableListOf(),
        "Q4" to mutableListOf())

    for ((x, y) in filteredPoints) {
        when {
            x >= CGx && y >= CGy -> quadrants["Q1"]?.add(Pair(x, y))
            x < CGx && y >= CGy -> quadrants["Q2"]?.add(Pair(x, y))
            x < CGx && y < CGy -> quadrants["Q3"]?.add(Pair(x, y))
            else -> quadrants["Q4"]?.add(Pair(x, y))
        }
    }

    val quadrantCGs = quadrants.mapValues { if (it.value.isNotEmpty()) computeCG(it.value) else mainCG }
    val quadrantCounts = quadrants.mapValues { it.value.size }

    val distancesMainToQuadrants = listOf("Q1", "Q2", "Q3", "Q4").map { calculateDistance(mainCG, quadrantCGs[it]!!) }
    val distancesMainToPoints = filteredPoints.map { calculateDistance(mainCG, it) }
    val firstPoint = filteredPoints.firstOrNull() ?: Pair(0.0, 0.0)
    val distancesFirstToAll = filteredPoints.map { calculateDistance(firstPoint, it) }

    return listOf(
        mainCG.first, mainCG.second,
        quadrantCGs["Q1"]!!.first, quadrantCGs["Q1"]!!.second,
        quadrantCGs["Q2"]!!.first, quadrantCGs["Q2"]!!.second,
        quadrantCGs["Q3"]!!.first, quadrantCGs["Q3"]!!.second,
        quadrantCGs["Q4"]!!.first, quadrantCGs["Q4"]!!.second,
        quadrantCounts["Q1"]!!.toDouble(), quadrantCounts["Q2"]!!.toDouble(),
        quadrantCounts["Q3"]!!.toDouble(), quadrantCounts["Q4"]!!.toDouble(),
        *distancesMainToQuadrants.toTypedArray(),
        *distancesMainToPoints.toTypedArray(),
        *distancesFirstToAll.toTypedArray()
    )
}

// Function to normalize list to exactly 120 points using linear interpolation
fun normalizeTo120Points(points: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
    val n = points.size
    val normalizedList = mutableListOf<Pair<Int, Int>>()

    for (i in 0 until 120) {
        val index = i * (n - 1).toDouble() / 119 // Compute proportional index
        val lowerIndex = index.toInt()
        val upperIndex = (lowerIndex + 1).coerceAtMost(n - 1)
        val fraction = index - lowerIndex

        val x = ((1 - fraction) * points[lowerIndex].first + fraction * points[upperIndex].first).roundToInt()
        val y = ((1 - fraction) * points[lowerIndex].second + fraction * points[upperIndex].second).roundToInt()

        normalizedList.add(Pair(x, y))
    }

    return normalizedList
}

// Function to process and ensure list has exactly 120 points
fun processList(nestedList: List<List<Triple<Int, Int, Int>>>): List<Pair<Int, Int>> {
    val flatList = nestedList.flatten()
    val pairList = convertToPairList(flatList)

    return when {
        pairList.size == 120 -> pairList
        pairList.size < 120 -> pairList /*throw IllegalArgumentException("Error: Points are less than 120, please redraw.")*/
        else -> normalizeTo120Points(pairList) // Normalize if greater than 120
    }
}

// Function to convert List<Triple<Int, Int, Int>> to List<Pair<Int, Int>>
fun convertToPairList(tripleList: List<Triple<Int, Int, Int>>): List<Pair<Int, Int>> {
    return tripleList.map { Pair(it.first, it.second) }
}

// Function to fetch data from DrawingView and process it
fun getProcessedFeatures(drawingView: DrawingView): List<Double> {
    val allCoordinates = drawingView.getAllCoordinates()
    val cleanCoordinates = processList(allCoordinates)
    println("Actual Input List : $cleanCoordinates")
    println("Length of the Input List : ${cleanCoordinates.size}" )
    val listOutput : List<Double> = processSingleList(cleanCoordinates)
    println("Actual Calculated Features : $listOutput")
    println("Length of the List : ${listOutput.size}" )
    return listOutput
}
