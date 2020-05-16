package net.owlfamily.android.aniext.core

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.TargetApi
import android.os.Build
import android.view.animation.LinearInterpolator
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by jyun
 */


/**
 * Android 의 ValueAnimator 클래스를 기반으로 한 Animator Set
 * 여러가지 유형의 Animation 을 한객체 내부에서 병렬수행할 수 있다.
 * 모든 자식 Animation 들이 Delay, Duration, Interpolator 등 기본 설정값을 공유한다.
 */
@Suppress("MemberVisibilityCanBePrivate", "JoinDeclarationAndAssignment")
@TargetApi(Build.VERSION_CODES.KITKAT)
open class ValueAnimatorSet : Animator.AnimatorListener, AnimatorUpdateListener {
    companion object {
        private const val LOG_TAG = "ValueAnimatorSet"
    }

    private val listeners: ArrayList<Animator.AnimatorListener> = ArrayList()

    constructor(initAnimators:List<ValueAnimator>):this(initAnimators.toTypedArray())
    constructor(initAnimators:Array<ValueAnimator>){
        mainAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        mainAnimator.addListener(this)
        mainAnimator.addUpdateListener(this)
        this.animators = ArrayList()
        this.animators.add(mainAnimator)
        for (animator in initAnimators) {
            this.animators.add(animator)
        }
        interpolator = LinearInterpolator()
    }

    val animators: ArrayList<ValueAnimator>
    val mainAnimator: ValueAnimator

    private var lastAnimationCalledTime: Long = 0
    private var pausedTime: Long = 0
    private var elapsedTime = 0f

    var interpolator: TimeInterpolator?
        get() = mainAnimator.interpolator
        set(interpolator) {
            for (animator in animators) {
                animator.interpolator = interpolator
            }
        }

    var currentPlayTime: Long
        get() = mainAnimator.currentPlayTime
        set(playTime) {
            for (animator in animators) {
                animator.currentPlayTime = playTime
            }
        }

    open var duration: Long
        get() = mainAnimator.duration
        set(duration) {
            for (animator in animators) {
                animator.duration = duration
            }
        }

    open fun start() {
        cancel()
        lastAnimationCalledTime = Date().time
        elapsedTime = 0.0f
        for (animator in animators) {
            animator.start()
        }
    }

    fun setStartDelay(startDelay: Int) {
        for (animator in animators) {
            animator.startDelay = startDelay.toLong()
        }
    }

    val isRunning: Boolean get() = mainAnimator.isRunning
    val isPaused: Boolean get() = mainAnimator.isPaused

    fun setRepeatCount(repeatCount: Int) {
        for (animator in animators) {
            animator.repeatCount = repeatCount
        }
    }

    fun setRepeatMode(repeatMode: Int) {
        for (animator in animators) {
            animator.repeatMode = repeatMode
        }
    }

    open fun reverse() {
        for (animator in animators) {
            animator.reverse()
        }
    }

    open fun cancel() {
        for (animator in animators) {
            animator.cancel()
        }
    }

    open fun cancelWithReset() {
        for (animator in animators) {
            animator.currentPlayTime = 0
            animator.cancel()
        }
    }

    open fun resume() {
        if (!isPaused) return
        val now = Date().time
        val pausedDuration = now - pausedTime
        lastAnimationCalledTime += pausedDuration
        for (animator in animators) {
            animator.resume()
        }
    }

    open fun pause() {
        if (isPaused) return
        pausedTime = Date().time
        for (animator in animators) {
            animator.pause()
        }
    }

    fun addListener(listener: Animator.AnimatorListener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
            //mainAnimator.addListener(listener);
        }
    }

    fun removeListener(listener: Animator.AnimatorListener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener)
            //mainAnimator.removeListener(listener);
        }
    }

    fun getListeners(): List<Animator.AnimatorListener> {
        return listeners
    }

    fun removeAllListeners() {
        listeners.clear()
    }

    override fun onAnimationStart(animator: Animator) {
        //Log.d(LOG_TAG, "onAnimationStart");
        lastAnimationCalledTime = Date().time
        elapsedTime = 0.0f
        val copiedList: List<Animator.AnimatorListener> =
            ArrayList(listeners)
        for (listener in copiedList) {
            listener.onAnimationStart(animator)
        }
    }

    override fun onAnimationEnd(animator: Animator) {
        //Log.d(LOG_TAG,"onAnimationEnd");
        val copiedList: List<Animator.AnimatorListener> =
            ArrayList(listeners)
        for (listener in copiedList) {
            listener.onAnimationEnd(animator)
        }
    }

    override fun onAnimationCancel(animator: Animator) {
        //Log.d(LOG_TAG,"onAnimationCancel");
        val copiedList: List<Animator.AnimatorListener> =
            ArrayList(listeners)
        for (listener in copiedList) {
            listener.onAnimationCancel(animator)
        }
    }

    override fun onAnimationRepeat(animator: Animator) {
        ///Log.d(LOG_TAG,"onAnimationRepeat");
        val copiedList: List<Animator.AnimatorListener> =
            ArrayList(listeners)
        for (listener in copiedList) {
            listener.onAnimationRepeat(animator)
        }
    }

    override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
        val now = Date().time
        elapsedTime = ((now - lastAnimationCalledTime).toDouble() / 1000.0).toFloat()
        lastAnimationCalledTime = now
    }


}
