package com.example.cardscanner

import com.example.cardscanner.util.validateString
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

        val list = listOf("12", "14", "16", "18", "20")

        val max = list.maxOfOrNull { it.substring(0, 2).toInt() }

        println(max)
    }

    @Test
    fun setValuesForVisionText() {

        val datesList = ArrayList<String>()

        var currentDate2 = ""

        val beginDate = "12/24"
        val endDate = "06/28"
        val testDate2 = "06/29"
        val testDate4 = "06/28"

        datesList.add(beginDate)
        datesList.add(endDate)

        (0..4).forEach { number ->

            datesList.add(beginDate)
            datesList.add(endDate)
            datesList.add(testDate2)
            datesList.add(testDate4)

            val maxDate = datesList.maxOfOrNull {
                datesList.get(number).substring(datesList.get(number).length - 2, datesList.get(number).length).toInt()
            }

            println(maxDate)

            val currentDate = datesList.find {
                it.substring(it.length - 2, it.length)
                    .toInt() == maxDate
            }

            currentDate2 = currentDate.validateString()

        }

        println(currentDate2)
    }
}