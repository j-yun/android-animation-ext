package net.owlfamily.android.aniext.customized

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.Animation
import net.owlfamily.android.aniext.core.ValueAnimatorSet


/**
 * 깜빡깜빡 하는 Flash Animation 설정값들이 미리 설정된
 * 애니메이션 객체
 */
class FlashAnimator(
    targetView: View,
    duration: Int,
    repeat: Boolean,
    startAlpha: Float,
    endAlpha: Float,
    animators: Array<ValueAnimator> = arrayOf()
) : ValueAnimatorSet(animators) {

    companion object {
        private const val LOG_TAG = "FlashAnimator"
    }

    init {
        val alphaAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        alphaAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            targetView.alpha = value
        }
        this.animators.add(alphaAnimator)
        this.duration = duration.toLong()
        if (repeat) {
            setRepeatCount(Animation.INFINITE)
            setRepeatMode(Animation.REVERSE)
        }
    }

    override fun onAnimationStart(animator: Animator) {
        super.onAnimationStart(animator)
    }

    override fun onAnimationCancel(animator: Animator) {
        super.onAnimationCancel(animator)
    }

    override fun onAnimationEnd(animator: Animator) {
        super.onAnimationEnd(animator)
    }

    override fun onAnimationRepeat(animator: Animator) {
        super.onAnimationRepeat(animator)
    }

    override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
        super.onAnimationUpdate(valueAnimator)
    }
}
