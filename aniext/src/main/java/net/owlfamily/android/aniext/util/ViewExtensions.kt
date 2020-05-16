package net.owlfamily.android.aniext.util

import android.view.View


fun View.waitUntilLayout(callback:()->Unit){
    addOnLayoutChangeListener(object: View.OnLayoutChangeListener{
        override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
            removeOnLayoutChangeListener(this)
            callback()
        }
    })

    requestLayout()
}