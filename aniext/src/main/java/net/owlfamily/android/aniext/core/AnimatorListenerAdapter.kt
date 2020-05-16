package net.owlfamily.android.aniext.core

import android.animation.Animator


/**
 * Created by jyun on 9/7/16.
 */
open class AnimatorListenerAdapter : Animator.AnimatorListener {
    override fun onAnimationStart(animator: Animator) {}
    override fun onAnimationEnd(animator: Animator) {}
    override fun onAnimationCancel(animator: Animator) {}
    override fun onAnimationRepeat(animator: Animator) {}
}
