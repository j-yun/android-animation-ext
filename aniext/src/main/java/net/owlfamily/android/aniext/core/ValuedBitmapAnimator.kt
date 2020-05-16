package net.owlfamily.android.aniext.core

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.widget.ImageView
import java.lang.IllegalStateException
import kotlin.math.floor


/**
 * Created by jyun
 *
 * ValueAnimatorSet 의 자식 Class 이다.
 * ValueAnimator 의 Progress 를 기반으로 Bitmap Sequence Animation 을 수행한다.
 */
@Suppress("ConstantConditionIf", "MemberVisibilityCanBePrivate")
class ValuedBitmapAnimator(
    imageView: ImageView,
    frames: IntArray,
    animators: Array<ValueAnimator> = arrayOf()
) : ValueAnimatorSet(animators) {
    private val totalFramesCount: Float
    /**  animation frames */
    private val frames: IntArray?
    private val imageView: ImageView?
    private var reusableBitmapOptions: BitmapFactory.Options? = null
    private var reusableBitmap: Bitmap? = null
    private var animationCancelled: Boolean
    private var currentFrame: Int
    private var isResetAnimationOnStop = true
    private var currentBitmap: Bitmap? = null

    companion object {
        private const val USE_REUSABLE_BITMAP = false
        private const val USE_PURGABLE_BITMAP = false
        private val LOG_TAG = ValuedBitmapAnimator::class.java.simpleName
    }

    init {
        if(frames.isEmpty()){
            throw IllegalStateException()
        }

        animationCancelled = false
        this.imageView = imageView
        this.frames = frames
        totalFramesCount = frames.size.toFloat()
        currentFrame = -1
        isResetAnimationOnStop = false
        if (USE_REUSABLE_BITMAP) {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            options.inMutable = true
            BitmapFactory.decodeResource(imageView.resources, frames[0], options)

            // we will create empty bitmap by using the option
            reusableBitmap =
                Bitmap.createBitmap(options.outWidth, options.outHeight, Bitmap.Config.ARGB_8888)
            reusableBitmapOptions = BitmapFactory.Options()
            reusableBitmapOptions!!.inBitmap = reusableBitmap
        }
        imageView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {}
            override fun onViewDetachedFromWindow(v: View) {
                Log.d(LOG_TAG, "Clear bitmap with detached view")
                v.removeOnAttachStateChangeListener(this)
                clear()
            }
        })
    }

    fun showFrame(frame: Int) {
        currentFrame = frame
        if (imageView == null || !imageView.isShown) {
            //Log.d(LOG_TAG,"Image not shown! with frame " + currentFrame);
            //imageView.setImageBitmap(null);
            return
        }
        if (currentBitmap != null) {
            if (!USE_REUSABLE_BITMAP) {
                if (!currentBitmap!!.isRecycled) {
                    currentBitmap!!.recycle()
                }
            }
            currentBitmap = null
            imageView.setImageBitmap(null)
        }
        val imageRes = frames!![currentFrame]
        try {
            currentBitmap = if (USE_REUSABLE_BITMAP) {
                BitmapFactory.decodeResource(
                    imageView.resources,
                    imageRes,
                    reusableBitmapOptions
                )
            } else {
                val bitmapOptions = BitmapFactory.Options()
                BitmapFactory.decodeResource(imageView.resources, imageRes, bitmapOptions)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (currentBitmap != null) {
            imageView.setImageBitmap(currentBitmap)
        }
    }

    fun clear() {
        if (isRunning) {
            cancel()
        }
        imageView?.setImageBitmap(null)
        if (reusableBitmap != null && !reusableBitmap!!.isRecycled) {
            reusableBitmap!!.recycle()
            reusableBitmap = null
        }
    }

    override fun onAnimationStart(animator: Animator) {
        super.onAnimationStart(animator)
        animationCancelled = false
    }

    override fun onAnimationCancel(animator: Animator) {
        super.onAnimationCancel(animator)
        animationCancelled = true
    }

    override fun onAnimationEnd(animator: Animator) {
        super.onAnimationEnd(animator)
        if (!animationCancelled && isResetAnimationOnStop) {
            currentPlayTime = 0
            Log.d(LOG_TAG, "Reset Animation state on end")
        }
    }

    override fun onAnimationRepeat(animator: Animator) {
        super.onAnimationRepeat(animator)
    }

    override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
        super.onAnimationUpdate(valueAnimator)
        val value = valueAnimator.animatedValue as Float
        var newFrame = floor(totalFramesCount * value.toDouble()).toInt()
        //index out bound를 피하기 위해서 위코드에서 totalFrameCount-1 을 해버리면 마지막 프레임이 짤리기 때문에 아래와 같이 한다.
        if (newFrame >= totalFramesCount) {
            newFrame = (totalFramesCount - 1).toInt()
        }
        if (newFrame != currentFrame) {
            showFrame(newFrame)
        }
    }
}
