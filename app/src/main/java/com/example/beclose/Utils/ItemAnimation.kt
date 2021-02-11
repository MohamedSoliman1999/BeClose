package com.example.beclose.Utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View


class ItemAnimation {

companion object{
    /* animation duration */
    private val DURATION_IN_BOTTOM_UP: Long = 150
    private val DURATION_IN_FADE_ID: Long = 500
    private val DURATION_IN_LEFT_RIGHT: Long = 150
    private val DURATION_IN_RIGHT_LEFT: Long = 250


    fun animateBottomUp(view: View, position: Int) {
        var position = position
        val not_first_item = position == -1
        position = position + 1
        view.setTranslationY(if (not_first_item) 800f else 500f)
        view.setAlpha(0f)
        val animatorSet = AnimatorSet()
        val animatorTranslateY: ObjectAnimator =
            ObjectAnimator.ofFloat(view, "translationY", if (not_first_item) 800f else 500f, 0f)
        val animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 1f)
        animatorTranslateY.startDelay = if (not_first_item) 0 else position * DURATION_IN_BOTTOM_UP
        animatorTranslateY.duration = (if (not_first_item) 3 else 1) * DURATION_IN_BOTTOM_UP
        animatorSet.playTogether(animatorTranslateY, animatorAlpha)
        animatorSet.start()
    }

    fun animateFadeIn(view: View, position: Int) {
        var position = position
        val not_first_item = position == -1
        position = position + 1
        view.setAlpha(0f)
        val animatorSet = AnimatorSet()
        val animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 0.5f, 1f)
        ObjectAnimator.ofFloat(view, "alpha", 0f).start()
        animatorAlpha.startDelay =
            if (not_first_item) DURATION_IN_FADE_ID / 2 else position * DURATION_IN_FADE_ID / 3
        animatorAlpha.duration = DURATION_IN_FADE_ID
        animatorSet.play(animatorAlpha)
        animatorSet.start()
    }

    fun animateLeftRight(view: View, position: Int) {
        var position = position
        val not_first_item = position == -1
        position = position + 1
        view.setTranslationX(-400f)
        view.setAlpha(0f)
        val animatorSet = AnimatorSet()
        val animatorTranslateY: ObjectAnimator =
            ObjectAnimator.ofFloat(view, "translationX", -400f, 0f)
        val animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 1f)
        ObjectAnimator.ofFloat(view, "alpha", 0f).start()
        animatorTranslateY.startDelay =
            if (not_first_item) DURATION_IN_LEFT_RIGHT else position * DURATION_IN_LEFT_RIGHT
        animatorTranslateY.duration = (if (not_first_item) 2 else 1) * DURATION_IN_LEFT_RIGHT
        animatorSet.playTogether(animatorTranslateY, animatorAlpha)
        animatorSet.start()
    }

    fun animateRightLeft(view: View, position: Int) {
        var position = position
        val not_first_item = position == -1
        position = position + 1
        view.setTranslationX(view.getX() + 900)
        view.setAlpha(0f)
        val animatorSet = AnimatorSet()
        val animatorTranslateY: ObjectAnimator =
            ObjectAnimator.ofFloat(view, "translationX", view.getX() + 900f, 0f)
        val animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 1f)
        ObjectAnimator.ofFloat(view, "alpha", 0f).start()
        animatorTranslateY.startDelay =
            if (not_first_item) DURATION_IN_RIGHT_LEFT else position * DURATION_IN_RIGHT_LEFT
        animatorTranslateY.duration = (if (not_first_item) 2 else 1) * DURATION_IN_RIGHT_LEFT
        animatorSet.playTogether(animatorTranslateY, animatorAlpha)
        animatorSet.start()
    }


}
}