package com.example.equipocuatro.utils

import android.view.View
import android.view.animation.AnimationUtils
import com.example.equipocuatro.R

fun View.startTouchAnimation(onAnimationEnd: () -> Unit) {
    val animation = AnimationUtils.loadAnimation(context, R.anim.scale_touch)
    animation.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
        override fun onAnimationStart(animation: android.view.animation.Animation?) {}
        override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
        override fun onAnimationEnd(animation: android.view.animation.Animation?) {
            onAnimationEnd()
        }
    })
    startAnimation(animation)
}
