package net.owlfamily.android.aniext.customized

import android.animation.Animator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import net.owlfamily.android.aniext.util.MathUtil
import net.owlfamily.android.aniext.core.ValueAnimatorSet
import net.owlfamily.android.aniext.core.AnimatorListenerAdapter

/**
 * Created by jyun
 *
 *  custom animation presets
 */
@Suppress("MemberVisibilityCanBePrivate")
object CustomAnimations {
    private const val LOG_TAG = "CustomAnimations"

    /**
     * 떠있는 효과
     * @param targetView
     * @return
     */
    fun makeAirAnimation(targetView: View, isDown:Boolean = true): ValueAnimatorSet {
        val yPos = targetView.y
        val height = targetView.height.toFloat()
        val yDiff = if(isDown) (height * 0.5f) else { -(height * 0.5f) }
        val yPositionAnimator = ValueAnimator.ofFloat(yPos, yPos + yDiff, yPos)
        yPositionAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.y = value
        }
        val valueAnimatorSet = ValueAnimatorSet(arrayOf(yPositionAnimator))
        valueAnimatorSet.interpolator = DecelerateInterpolator()
        return valueAnimatorSet
    }

    fun makeFlipFadeOutAnimation(targetView: View): ValueAnimatorSet {
        val yScaleAnimator = ValueAnimator.ofFloat(1.0f, -1.0f)
        yScaleAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.scaleY = value
            targetView.scaleY = value
        }
        val alphaAnimator = ValueAnimator.ofFloat(1.0f, 0.0f)
        alphaAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.alpha = value
            targetView.alpha = value
        }
        val valueAnimatorSet =
            ValueAnimatorSet(arrayOf(yScaleAnimator, alphaAnimator))
        valueAnimatorSet.interpolator = DecelerateInterpolator()
        return valueAnimatorSet
    }

    /**
     * 점프하면서 동시에 떨리는 효과
     * @param targetView
     * @return
     */
    fun makeJumpingShakeAnimation(targetView: View): ValueAnimatorSet {
        val maxRotation = 10.0f
        val rotateAnimator = ValueAnimator.ofFloat(
            maxRotation * 0.5f, -maxRotation * 0.5f, maxRotation, -maxRotation,
            maxRotation * 0.5f, -maxRotation * 0.5f, maxRotation, -maxRotation,
            maxRotation * 0.25f, -maxRotation * 0.25f, 0f
        )
        rotateAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.rotation = value
        }
        val engYPos = targetView.y
        val engHeight =
            targetView.height.toFloat() //copy view 는 높이가 측정되지 않았기 때문에 original view 의 것을 사용한다.
        val engYPositionAnimator =
            ValueAnimator.ofFloat(engYPos, engYPos - engHeight * 0.5f, engYPos)
        engYPositionAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.y = value
        }
        val valueAnimatorSet =
            ValueAnimatorSet(arrayOf(rotateAnimator, engYPositionAnimator))
        valueAnimatorSet.interpolator = DecelerateInterpolator()
        return valueAnimatorSet
    }

    /**
     * scale 변경되면서 fadeout
     *
     * @param targetView
     * @param startScale 시작 scale
     * @param endScale 종료 scale
     * @return
     */
    fun makePopExitAnimation(targetView: View, startScale: Float, endScale: Float): ValueAnimatorSet {
        val scaleXAnimator = ValueAnimator.ofFloat(startScale, endScale)
        scaleXAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.scaleX = value
        }
        val alphaAnimator = ValueAnimator.ofFloat(1.0f, 0.0f)
        alphaAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.alpha = value
        }
        val scaleYAnimator = ValueAnimator.ofFloat(startScale, endScale)
        scaleYAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.scaleY = value
        }
        val valueAnimatorSet =
            ValueAnimatorSet(arrayOf(alphaAnimator, scaleXAnimator, scaleYAnimator))
        valueAnimatorSet.interpolator = DecelerateInterpolator()
        return valueAnimatorSet
    }

    /** 떨림 효과  */
    fun makeVibrateAnimation(targetView: View, startScaleX: Float, startScaleY: Float, restoreToStartScale: Boolean, vibratePower: Float, vibrateCount: Int): ValueAnimatorSet {
        val xAnimationArr: FloatArray
        val yAnimationArr: FloatArray
        var arrCount = 0
        if (restoreToStartScale) {
            arrCount = vibrateCount + 2
            xAnimationArr = FloatArray(arrCount)
            yAnimationArr = FloatArray(arrCount)
        } else {
            arrCount = vibrateCount + 1
            xAnimationArr = FloatArray(arrCount)
            yAnimationArr = FloatArray(arrCount)
        }
        xAnimationArr[0] = startScaleX
        yAnimationArr[0] = startScaleY
        var isOdd = true
        for (i in 1 until arrCount) {
            if (isOdd) {
                xAnimationArr[i] = startScaleX - vibratePower
                yAnimationArr[i] = startScaleY - vibratePower
                isOdd = false
            } else {
                xAnimationArr[i] = startScaleX + vibratePower
                yAnimationArr[i] = startScaleY + vibratePower
                isOdd = true
            }
        }
        if (restoreToStartScale) {
            xAnimationArr[xAnimationArr.size - 1] = startScaleX
            yAnimationArr[yAnimationArr.size - 1] = startScaleY
        }
        val scaleXAnimator = ValueAnimator.ofFloat(*xAnimationArr)
        scaleXAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.scaleX = value
        }
        val scaleYAnimator = ValueAnimator.ofFloat(*yAnimationArr)
        scaleYAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.scaleY = value
        }
        //valueAnimatorSet.setInterpolator(new DecelerateInterpolator());
        return ValueAnimatorSet(arrayOf(scaleXAnimator, scaleYAnimator))
    }

    /**
     * startScale -> midScale -> endScale -> bouncePower 수치로 흔들리는 효과
     */
    fun makeJellyPopEnteringAnimation(targetView: View, startScale: Float, midScale: Float, endScale: Float, bouncePower: Float, startAlpha: Float, endAlpha: Float): ValueAnimatorSet {
        val alphaAnimator = ValueAnimator.ofFloat(startAlpha, endAlpha)
        alphaAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.alpha = value
        }
        val scaleXAnimator = ValueAnimator.ofFloat(
            startScale,
            midScale + bouncePower, midScale - bouncePower,
            midScale + bouncePower * 0.5f, midScale - bouncePower * 0.5f,
            midScale + bouncePower * 0.25f, midScale - bouncePower * 0.25f,
            endScale
        )
        scaleXAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.scaleX = value
        }
        val scaleYAnimator = ValueAnimator.ofFloat(
            startScale,
            midScale + bouncePower, midScale - bouncePower,
            midScale + bouncePower * 0.5f, midScale - bouncePower * 0.5f,
            midScale + bouncePower * 0.25f, midScale - bouncePower * 0.25f,
            endScale
        )
        scaleYAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.scaleY = value
        }
        val valueAnimatorSet =
            ValueAnimatorSet(arrayOf(alphaAnimator, scaleXAnimator, scaleYAnimator))
        valueAnimatorSet.interpolator = DecelerateInterpolator()
        return valueAnimatorSet
    }

    /**
     * makeJellyPopEnteringAnimation 의 축소버전. 잔류 떨림이 없음.
     */
    fun makeWeakJellyPopEnteringAnimation(
        targetView: View,
        startScale: Float,
        midScale: Float,
        endScale: Float,
        startAlpha: Float,
        endAlpha: Float
    ): ValueAnimatorSet {
        val alphaAnimator = ValueAnimator.ofFloat(startAlpha, endAlpha)
        alphaAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.alpha = value
        }
        val scaleXAnimator = ValueAnimator.ofFloat(
            startScale,
            midScale,
            endScale
        )
        scaleXAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.scaleX = value
        }
        val scaleYAnimator = ValueAnimator.ofFloat(
            startScale,
            midScale,
            endScale
        )
        scaleYAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.scaleY = value
        }
        val valueAnimatorSet = ValueAnimatorSet(arrayOf(alphaAnimator, scaleXAnimator, scaleYAnimator))
        valueAnimatorSet.interpolator = DecelerateInterpolator()
        return valueAnimatorSet
    }

    /**
     * 젤리처럼 흐물흐물거리는 효과. repeat 할시에 유용.
     */
    fun makeJellyWaveAnimation(
        targetView: View,
        startScaleX: Float,
        startScaleY: Float,
        wavePowerX: Float,
        wavePowerY: Float
    ): ValueAnimatorSet {
        //ValueAnimator scaleXAnimator = ValueAnimator.ofFloat(1.0f,1.0f,1.0f,1.025f,1.0f);
        val scaleXAnimator = ValueAnimator.ofFloat(
            startScaleX,
            startScaleX,
            startScaleX,
            startScaleX + wavePowerX,
            startScaleX
        )
        scaleXAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.scaleX = value
        }
        //ValueAnimator scaleYAnimator = ValueAnimator.ofFloat(1.0f,1.025f,1.05f,1.025f,1.0f);
        val scaleYAnimator = ValueAnimator.ofFloat(
            startScaleY,
            startScaleY + wavePowerY * 0.5f,
            startScaleY + wavePowerY,
            startScaleY + wavePowerY * 0.5f,
            startScaleY
        )
        scaleYAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.scaleY = value
        }
        val waveValueAnimatorSet = ValueAnimatorSet(arrayOf(scaleXAnimator, scaleYAnimator))
        waveValueAnimatorSet.interpolator = DecelerateInterpolator()
        return waveValueAnimatorSet
    }

    /**
     * Up to Down 애니메이션
     * @param targetView
     * @return
     */
    fun makeUpToDownFadeInAnimation(targetView: View): ValueAnimatorSet {
        val engYPos = targetView.y
        val engHeight = targetView.height.toFloat()
        val engYPositionAnimator =
            ValueAnimator.ofFloat(engYPos - engHeight * 0.5f, engYPos + engHeight * 0.3f, engYPos)
        engYPositionAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.y = value
        }
        val alphaAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        alphaAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.alpha = value
        }
        val valueAnimatorSet = ValueAnimatorSet(arrayOf(engYPositionAnimator, alphaAnimator))
        valueAnimatorSet.interpolator = DecelerateInterpolator()
        return valueAnimatorSet
    }

    fun makeDownToUpAnimation(
        targetView: View,
        performAfterParentLayout: Boolean,
        setTransparentBeforeStart: Boolean,
        animationDuration: Long
    ): ValueAnimatorSet {
        if (setTransparentBeforeStart) {
            targetView.alpha = 0.0f
        }
        val engYPositionAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        engYPositionAnimator.addUpdateListener(object : AnimatorUpdateListener {
            var startY: Float? = null
            var endY: Float? = null
            var onLayoutChangeAdded = false
            var alphaRestored = false
            override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
                val parentView = targetView.parent as View ?: return
                if (startY == null || endY == null) {
                    if (performAfterParentLayout) {
                        if (!onLayoutChangeAdded) {
                            onLayoutChangeAdded = true
                            parentView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                                override fun onLayoutChange(view: View, i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int) {
                                    Log.d(LOG_TAG, "OnLayoutChange : " + i + "," + i1 + "," + i2 + "," + i3 + "_" + i4 + "," + i5 + "," + i6 + "," + i7)
                                    startY = targetView.y + targetView.height
                                    endY = targetView.y
                                    view.removeOnLayoutChangeListener(this)
                                }
                            })
                            //parentView의 onLayoutChangeListener를 무조건 호출하도록 할 수 있다.
                            targetView.requestLayout()
                        }
                    } else {
                        startY = targetView.y + targetView.height
                        endY = targetView.y
                    }
                }
                if (startY == null || endY == null) return

                //Log.d(LOG_TAG,"Down To Up : " + startY + "," + endY);
                if (setTransparentBeforeStart && !alphaRestored) {
                    alphaRestored = true
                    targetView.alpha = 1.0f
                }
                val value = valueAnimator.animatedValue as Float
                val newY: Float = MathUtil.lerp(startY!!, endY!!, value)
                targetView.y = newY
            }
        })
        val valueAnimatorSet =
            ValueAnimatorSet(arrayOf(engYPositionAnimator))
        valueAnimatorSet.interpolator = DecelerateInterpolator()
        valueAnimatorSet.duration = animationDuration
        return valueAnimatorSet
    }

    /**
     * Right to Left 애니메이션
     * @param targetView
     * @return
     */
    fun makeRightToLeftAnimation(targetView: View, performAfterParentLayout: Boolean, setTransparentBeforeStart: Boolean, animationDuration: Long): ValueAnimatorSet {
        if (setTransparentBeforeStart) {
            targetView.alpha = 0.0f
        }
        val engYPositionAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        engYPositionAnimator.addUpdateListener(object : AnimatorUpdateListener {
            var startX: Float? = null
            var endX: Float? = null
            var onLayoutChangeAdded = false
            var alphaRestored = false
            override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
                val parentView = targetView.parent as View ?: return
                if (startX == null || endX == null) {
                    if (performAfterParentLayout) {
                        if (!onLayoutChangeAdded) {
                            onLayoutChangeAdded = true
                            parentView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                                override fun onLayoutChange(view: View, i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int) {
                                    startX = targetView.x + targetView.width
                                    endX = targetView.x
                                    view.removeOnLayoutChangeListener(this)
                                }
                            })
                            //parentView의 onLayoutChangeListener를 무조건 호출하도록 할 수 있다.
                            targetView.requestLayout()
                        }
                    } else {
                        startX = targetView.x + targetView.width
                        endX = targetView.x
                    }
                }
                if (startX == null || endX == null) return

                //Log.d(LOG_TAG,"Right To Left : " + startX + "," + endX);
                val value = valueAnimator.animatedValue as Float
                val newX: Float = MathUtil.lerp(startX!!, endX!!, value)
                targetView.x = newX
                if (setTransparentBeforeStart && !alphaRestored) {
                    alphaRestored = true
                    targetView.alpha = 1.0f
                }
            }
        })
        val valueAnimatorSet = ValueAnimatorSet(arrayOf(engYPositionAnimator))
        valueAnimatorSet.interpolator = DecelerateInterpolator()
        valueAnimatorSet.duration = animationDuration
        return valueAnimatorSet
    }

    /**
     * Left to Right 애니메이션
     * @param targetView
     * @return
     */
    fun makeLefToRightOutAnimation(
        targetView: View,
        performAfterParentLayout: Boolean,
        setTransparentBeforeStart: Boolean,
        animationDuration: Long
    ): ValueAnimatorSet {
        if (setTransparentBeforeStart) {
            targetView.alpha = 0.0f
        }
        val engYPositionAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        engYPositionAnimator.addUpdateListener(object : AnimatorUpdateListener {
            var startX: Float? = null
            var endX: Float? = null
            var onLayoutChangeAdded = false
            var alphaRestored = false
            override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
                val parentView = targetView.parent as View ?: return
                if (startX == null || endX == null) {
                    if (performAfterParentLayout) {
                        if (!onLayoutChangeAdded) {
                            onLayoutChangeAdded = true
                            parentView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                                override fun onLayoutChange(view: View, i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int) {
                                    startX = targetView.x
                                    endX = targetView.x - targetView.width
                                    view.removeOnLayoutChangeListener(this)
                                }
                            })
                            //parentView의 onLayoutChangeListener를 무조건 호출하도록 할 수 있다.
                            targetView.requestLayout()
                        }
                    } else {
                        startX = targetView.x
                        endX = targetView.x - targetView.width
                    }
                }
                if (startX == null || endX == null) return

                //Log.d(LOG_TAG,"Down To Up : " + startX + "," + endX);
                val value = valueAnimator.animatedValue as Float
                val newX: Float = MathUtil.lerp(startX!!, endX!!, value)
                targetView.x = newX
                if (setTransparentBeforeStart && !alphaRestored) {
                    alphaRestored = true
                    targetView.alpha = 1.0f
                }
            }
        })
        val valueAnimatorSet = ValueAnimatorSet(arrayOf(engYPositionAnimator))
        valueAnimatorSet.interpolator = DecelerateInterpolator()
        valueAnimatorSet.duration = animationDuration
        return valueAnimatorSet
    }

    /**
     * Left to Right 애니메이션
     * @param targetView
     * @return
     */
    fun makeLefToRightInAnimation(
        targetView: View,
        performAfterParentLayout: Boolean,
        setTransparentBeforeStart: Boolean,
        animationDuration: Long
    ): ValueAnimatorSet {
        if (setTransparentBeforeStart) {
            targetView.alpha = 0.0f
        }
        val engYPositionAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        engYPositionAnimator.addUpdateListener(object : AnimatorUpdateListener {
            var startX: Float? = null
            var endX: Float? = null
            var onLayoutChangeAdded = false
            var alphaRestored = false
            override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
                val parentView = targetView.parent as View ?: return
                if (startX == null || endX == null) {
                    if (performAfterParentLayout) {
                        if (!onLayoutChangeAdded) {
                            onLayoutChangeAdded = true
                            parentView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                                override fun onLayoutChange(view: View, i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int) {
                                    startX = targetView.x - targetView.width
                                    endX = targetView.x
                                    view.removeOnLayoutChangeListener(this)
                                }
                            })
                            //parentView의 onLayoutChangeListener를 무조건 호출하도록 할 수 있다.
                            targetView.requestLayout()
                        }
                    } else {
                        startX = targetView.x - targetView.width
                        endX = targetView.x
                    }
                }
                if (startX == null || endX == null) return

                //Log.d(LOG_TAG,"Down To Up : " + startX + "," + endX);
                val value = valueAnimator.animatedValue as Float
                val newX: Float = MathUtil.lerp(startX!!, endX!!, value)
                targetView.x = newX
                if (setTransparentBeforeStart && !alphaRestored) {
                    alphaRestored = true
                    targetView.alpha = 1.0f
                }
            }
        })
        val valueAnimatorSet = ValueAnimatorSet(arrayOf(engYPositionAnimator))
        valueAnimatorSet.interpolator = DecelerateInterpolator()
        valueAnimatorSet.duration = animationDuration
        return valueAnimatorSet
    }

    /**
     * 종합 애니메이션
     * position , scale, rotation, alpha 모두 동시에 애니메이션 가능.
     */
    fun makeTransitionAnimation(
        targetView: View, startX: Float, endX: Float, startY: Float, endY: Float,
        startScaleX: Float, endScaleX: Float, startScaleY: Float, endScaleY: Float,
        startRotation: Float, midRotation: Float, endRotation: Float,
        startAlpha: Float, endAlpha: Float
    ): ValueAnimatorSet {
        val valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        valueAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.x = MathUtil.lerp(startX, endX, value)
            targetView.y = MathUtil.lerp(startY, endY, value)
            val scaleX: Float = MathUtil.lerp(startScaleX, endScaleX, value)
            val scaleY: Float = MathUtil.lerp(startScaleY, endScaleY, value)
            //Log.d(LOG_TAG,"start ScX : " + startScaleX + ", end Scx : " + endScaleX+", ScaleX : " + scaleX);
            //Log.d(LOG_TAG,"start ScY : " + startScaleY + ", end ScY : " + endScaleY+", ScaleY : " + scaleY);
            targetView.scaleX = scaleX
            targetView.scaleY = scaleY
            targetView.alpha = MathUtil.lerp(startAlpha, endAlpha, value)
        }
        val rotationAnimator =
            ValueAnimator.ofFloat(startRotation, midRotation, endRotation)
        rotationAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.rotation = value
        }
        val valueAnimatorSet = ValueAnimatorSet(arrayOf(valueAnimator, rotationAnimator))
        valueAnimatorSet.interpolator = DecelerateInterpolator()
        return valueAnimatorSet
    }

    fun makeRotationAnimation(
        targetView: View, startRotation: Float, midRotation: Float, endRotation: Float
    ): ValueAnimatorSet {
        val rotationAnimator = ValueAnimator.ofFloat(startRotation, midRotation, endRotation)
        rotationAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.rotation = value
        }
        val valueAnimatorSet = ValueAnimatorSet(arrayOf(rotationAnimator))
        valueAnimatorSet.interpolator = DecelerateInterpolator()
        return valueAnimatorSet
    }

    fun makePositionWithAlphaAnimation(
        targetView: View, startX: Float, endX: Float, startY: Float, endY: Float,
        startAlpha: Float, endAlpha: Float, animationDuration: Long
    ): ValueAnimatorSet {
        val valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        valueAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.x = MathUtil.lerp(startX, endX, value)
            targetView.y = MathUtil.lerp(startY, endY, value)
            targetView.alpha = MathUtil.lerp(startAlpha, endAlpha, value)
        }
        val valueAnimatorSet = ValueAnimatorSet(arrayOf(valueAnimator))
        valueAnimatorSet.interpolator = DecelerateInterpolator()
        valueAnimatorSet.duration = animationDuration
        return valueAnimatorSet
    }

    fun makePositionWithAlphaAnimation(targetView: View, startX: Float, endX: Float, startY: Float, endY: Float, startAlpha: Float, endAlpha: Float): ValueAnimatorSet {
        return makePositionWithAlphaAnimation(
            targetView,
            startX,
            endX,
            startY,
            endY,
            startAlpha,
            endAlpha,
            0
        )
    }

    fun makePositionAnimation(
        targetView: View,
        startX: Float, endX: Float, startY: Float, endY: Float,
        animationDuration: Long
    ): ValueAnimatorSet {
        val valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        valueAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.x = MathUtil.lerp(startX, endX, value)
            targetView.y = MathUtil.lerp(startY, endY, value)
        }
        val valueAnimatorSet = ValueAnimatorSet(arrayOf(valueAnimator))
        valueAnimatorSet.interpolator = DecelerateInterpolator()
        valueAnimatorSet.duration = animationDuration
        return valueAnimatorSet
    }

    fun makePositionAnimation(targetView: View, startX: Float, endX: Float, startY: Float, endY: Float): ValueAnimatorSet {
        return makePositionAnimation(
            targetView,
            startX,
            endX,
            startY,
            endY,
            0
        )
    }

    fun makeScaleWithAlphaAnimation(targetView: View,
        startScaleX: Float, endScaleX: Float, startScaleY: Float, endScaleY: Float,
        startAlpha: Float, endAlpha: Float): ValueAnimatorSet
    {
        val valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        valueAnimator.addUpdateListener { valueAnimator ->
            targetView.pivotX = targetView.width / 2.toFloat()
            targetView.pivotY = targetView.height / 2.toFloat()
            val value = valueAnimator.animatedValue as Float
            val scaleX: Float = MathUtil.lerp(startScaleX, endScaleX, value)
            val scaleY: Float = MathUtil.lerp(startScaleY, endScaleY, value)
            //Log.d(LOG_TAG,"start ScX : " + startScaleX + ", end Scx : " + endScaleX+", ScaleX : " + scaleX);
            //Log.d(LOG_TAG,"start ScY : " + startScaleY + ", end ScY : " + endScaleY+", ScaleY : " + scaleY);
            targetView.scaleX = scaleX
            targetView.scaleY = scaleY
            targetView.alpha = MathUtil.lerp(startAlpha, endAlpha, value)
        }
        val valueAnimatorSet = ValueAnimatorSet(arrayOf(valueAnimator))
        valueAnimatorSet.interpolator = DecelerateInterpolator()
        valueAnimatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animator: Animator) {
                targetView.pivotX = targetView.width / 2.toFloat()
                targetView.pivotY = targetView.height / 2.toFloat()
            }
        })
        return valueAnimatorSet
    }

    fun makeAlphaAnimation(
        targetView: View,
        startAlpha: Float,
        endAlpha: Float
    ): ValueAnimatorSet {
        return makeAlphaAnimation(
            targetView,
            startAlpha,
            endAlpha,
            0
        )
    }

    fun makeAlphaAnimation(targetView: View, startAlpha: Float,
                           endAlpha: Float, duration: Long): ValueAnimatorSet {
        val valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        valueAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            targetView.alpha = MathUtil.lerp(startAlpha, endAlpha, value)
        }
        val valueAnimatorSet =
            ValueAnimatorSet(arrayOf(valueAnimator))
        valueAnimatorSet.interpolator = DecelerateInterpolator()
        valueAnimatorSet.duration = duration
        return valueAnimatorSet
    }

    fun makeSmallToNormalFadeInAnimation(targetView: View, duration: Long): ValueAnimatorSet {
        val result: ValueAnimatorSet =
            makeScaleWithAlphaAnimation(
                targetView, 0.9f, 1.0f, 0.9f, 1.0f,
                0.0f, 1.0f
            )
        result.duration = duration
        return result
    }

    fun makeNormalToSmallFadeOutAnimation(targetView: View, duration: Long): ValueAnimatorSet {
        val result: ValueAnimatorSet =
            makeScaleWithAlphaAnimation(
                targetView, 1.0f, 0.9f, 1.0f, 0.9f,
                1.0f, 0.0f
            )
        result.duration = duration
        return result
    }

    fun makeNormalToBigFadeOutAnimation(targetView: View, duration: Long): ValueAnimatorSet {
        val result: ValueAnimatorSet =
            makeScaleWithAlphaAnimation(
                targetView, 1.0f, 1.1f, 1.0f, 1.1f,
                1.0f, 0.0f
            )
        result.duration = duration
        return result
    }
}
