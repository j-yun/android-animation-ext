package net.owlfamily.android.aniext.core

import android.view.animation.Animation

open class AnimationListenerAdapter : Animation.AnimationListener{
    override fun onAnimationRepeat(animation: Animation?) {}
    override fun onAnimationEnd(animation: Animation?) {}
    override fun onAnimationStart(animation: Animation?) {}
}