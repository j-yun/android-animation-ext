package net.owlfamily.android.aniext.demo

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import net.owlfamily.android.aniext.customized.CustomAnimations
import net.owlfamily.android.aniext.util.waitUntilLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        titleTv.waitUntilLayout {
            val animator = CustomAnimations.makeAirAnimation(titleTv, isDown = false)
            animator.setRepeatCount(ValueAnimator.INFINITE)
            animator.duration = 1000
            animator.start()
        }
    }
}
