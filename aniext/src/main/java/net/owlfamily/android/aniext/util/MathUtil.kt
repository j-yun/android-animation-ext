package net.owlfamily.android.aniext.util

import java.util.*
import kotlin.math.abs

/**
 * Created by jyun
 */
object MathUtil {
    private var random = Random(Date().time)
    private const val LOG_TAG = "MathUtil"

    fun setRandom(random: Random) {
        MathUtil.random = random
    }

    fun random(min: Int, max: Int): Int {
        //Random random = new Random();
        //random.setSeed(new java.util.Date().getTime());
        //Log.d(LOG_TAG, "Random " + min + " ~ " + max + " = " + randomNum);
        return random.nextInt(max - min + 1) + min
    }

    fun isOdd(number: Int): Boolean {
        if (number == 0) return false
        return abs(number % 2) == 1
    }

    fun lerp(start: Float, end: Float, progress: Float): Float {
        return start + progress * (end - start)
    }

    val currentLastSecondNumber: Int
        get() {
            val now = Date()
            val nowString = (now.time / 1000).toString()
            return Integer.valueOf(nowString.substring(nowString.length - 1, nowString.length))
        }

    val currentLastMilliSecondNumber: Int
        get() {
            val now = Date()
            val nowString = now.time.toString()
            return Integer.valueOf(nowString.substring(nowString.length - 1, nowString.length))
        }

    fun appendKComma(number: String): String {
        val strB = StringBuilder()
        var three = 0
        for (i in number.length downTo 1) {
            val charStr = number.substring(i - 1, i)
            three++
            if (three == 4 && i > 0) {
                //strB.append(",");
                strB.insert(0, ",")
                three = 1
            }
            strB.insert(0, charStr)
            //strB.append(charStr);
        }
        return strB.toString()
    }
}
