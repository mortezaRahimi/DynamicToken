package com.myket.farahani.dynamictoken

import org.junit.Test

import org.junit.Assert.*
import java.util.function.Predicate

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(3, calculateMaxProfit(arrayListOf(100, 180, 260, 310, 40, 535, 695)))
    }
}

fun calculateMaxProfit(points: ArrayList<Int>): Int {

    var tempList = points
    var maxList = arrayListOf<Int>()

    for (i in tempList) {
        for (j in tempList.subList(1, tempList.size)) {
            if (i < j && tempList.indexOf(j) > tempList.indexOf(i)) {
                maxList.add(j - i)
            }
        }
    }

    return maxList.max()
}