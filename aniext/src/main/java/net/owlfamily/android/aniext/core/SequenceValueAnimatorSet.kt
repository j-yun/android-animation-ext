package net.owlfamily.android.aniext.core

import android.animation.ValueAnimator
import java.util.*

/**
 * Created by jyun
 */
@Suppress("ConvertSecondaryConstructorToPrimary")
class SequenceValueAnimatorSet : ValueAnimatorSet {
    companion object {
        private val LOG_TAG = SequenceValueAnimatorSet::class.java.simpleName
    }

    inner class ValueAnimatorSetPair {
        var timeRatio = 0f
        var valueAnimatorSet: ValueAnimatorSet? = null
    }

    private var animationSets: List<ValueAnimatorSet>
    private var animationPairs: MutableList<ValueAnimatorSetPair>
    var currentAnimator: ValueAnimatorSet? = null

    constructor(animationSets: List<ValueAnimatorSet>):super(arrayOf()){
        this.animationSets = ArrayList(animationSets)
        animationPairs = ArrayList()

        var totalDuration = 0.0
        for (valueAnimatorSet in animationSets) {
            val value = valueAnimatorSet.duration.toDouble()
            totalDuration += value
            val valueAnimatorSetPair = ValueAnimatorSetPair()
            valueAnimatorSetPair.valueAnimatorSet = valueAnimatorSet
            valueAnimatorSetPair.timeRatio = 0.0f
            animationPairs.add(valueAnimatorSetPair)
        }
        super.duration = totalDuration.toLong()
        for (valueAnimatorSetPair in animationPairs) {
            val value = valueAnimatorSetPair.valueAnimatorSet!!.duration.toDouble()
            valueAnimatorSetPair.timeRatio = (value / totalDuration).toFloat()
        }
        if (animationPairs.size > 0) {
            currentAnimator = animationPairs[0].valueAnimatorSet
        }
    }

    private fun refreshAnimationTimes() {
        val totalDuration = duration
        for (valueAnimatorSetPair in animationPairs) {
            val timeRatio = valueAnimatorSetPair.timeRatio
            valueAnimatorSetPair.valueAnimatorSet!!.duration =
                (totalDuration * timeRatio).toLong()
        }
    }

    override var duration: Long
        get() = super.duration
        set(duration) {
            super.duration = duration
            refreshAnimationTimes()
        }

    override fun start() {
        super.start()
        if (currentAnimator != null) {
            currentAnimator!!.start()
        }
    }

    override fun reverse() {
        super.reverse()
        if (currentAnimator != null) {
            currentAnimator!!.reverse()
        }
    }

    override fun cancel() {
        super.cancel()
        if (currentAnimator != null) {
            currentAnimator!!.cancel()
        }
    }

    override fun cancelWithReset() {
        super.cancelWithReset()
        if (currentAnimator != null) {
            currentAnimator!!.cancelWithReset()
        }
    }

    override fun resume() {
        super.resume()
        if (currentAnimator != null) {
            currentAnimator!!.resume()
        }
    }

    override fun pause() {
        super.pause()
        if (currentAnimator != null) {
            currentAnimator!!.pause()
        }
    }

    override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
        super.onAnimationUpdate(valueAnimator)
        val value = valueAnimator.animatedValue as Float
        val duration = duration
        val currentDuration = (duration.toDouble() * value).toLong()
        var totalDuration: Long = 0
        for (valueAnimatorSetPair in animationPairs) {
            val animationDur = valueAnimatorSetPair.valueAnimatorSet!!.duration
            totalDuration += animationDur
            if (currentDuration < totalDuration) {
                if (currentAnimator != null && currentAnimator != valueAnimatorSetPair.valueAnimatorSet && currentAnimator!!.isRunning) {
                    currentAnimator!!.cancelWithReset()
                }
                currentAnimator = valueAnimatorSetPair.valueAnimatorSet
                if (!currentAnimator!!.isRunning) {
                    currentAnimator!!.start()
                }
                break
            }
        }
    }
}
