package com.so.dingbring

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.imangazaliev.circlemenu.CircleMenuButton
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

internal class ItemSelectionAnimator(
    private val context: Context
) {

    private var circleColor = 0
    private var circleAlpha: Int
    private var startAngle = 0f
    private var currentCircleAngle: Float
    private val originalCircleStrokeWidth: Float
    private var currentCircleStrokeWidth = 0f
    private var circleCenterX: Float = 0f
    private var circleCenterY: Float = 0f
    private val circleRect = RectF()
    private var currentIconBitmap: Bitmap? = null
    private var iconSourceRect: Rect? = null
    private val iconRect = RectF()
    private var isAnimating = false

    init {
        currentCircleAngle = START_CIRCLE_ANGLE.toFloat()
        circleAlpha = ALPHA_OPAQUE
        originalCircleStrokeWidth = context.resources.getDimension(R.dimen.circle_menu_button_size)
    }

    fun setCenterButtonPosition(centerButtonX: Float, centerButtonY: Float) {
        this.circleCenterX = centerButtonX + originalCircleStrokeWidth / 2
        this.circleCenterY = centerButtonY + originalCircleStrokeWidth / 2
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun startSelectAnimation(
    ) {
        if (isAnimating) {
            return
        }

        currentCircleStrokeWidth = originalCircleStrokeWidth
        startCircleDrawingAnimation()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getIconBitmap(drawable: Drawable): Bitmap? {
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else if (drawable is VectorDrawableCompat
            || drawable is VectorDrawable
        ) {
            getBitmapFromVectorDrawable(drawable)
        } else {
            null
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getBitmapFromVectorDrawable(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun startCircleDrawingAnimation() {
        isAnimating = true
        val circleAngleAnimation = ValueAnimator.ofFloat(START_CIRCLE_ANGLE.toFloat(), END_CIRCLE_ANGLE.toFloat())
        circleAngleAnimation.duration = SELECT_ANIMATION_DURATION.toLong()
        circleAngleAnimation.interpolator = DecelerateInterpolator()
        circleAngleAnimation.addUpdateListener { animation ->
            currentCircleAngle = animation.animatedValue as Float
            if (currentCircleAngle == END_CIRCLE_ANGLE.toFloat()) {
                startExitAnimation()
            }
        }
        circleAngleAnimation.start()
    }

    private fun startExitAnimation() {
        val circleSizeAnimation = ValueAnimator.ofFloat(START_CIRCLE_SIZE_RATIO, END_CIRCLE_SIZE_RATIO)
        circleSizeAnimation.duration = EXIT_ANIMATION_DURATION.toLong()
        circleSizeAnimation.interpolator = DecelerateInterpolator()
        circleSizeAnimation.addUpdateListener { animation ->
            val animationValue = animation.animatedValue as Float
            currentCircleStrokeWidth = originalCircleStrokeWidth * animationValue
            if (animationValue == END_CIRCLE_SIZE_RATIO) {
                currentCircleAngle = START_CIRCLE_ANGLE.toFloat()
                currentCircleStrokeWidth = originalCircleStrokeWidth
                isAnimating = false
            }
        }
        val circleAlphaAnimation = ValueAnimator.ofInt(ALPHA_OPAQUE, ALPHA_TRANSPARENT)
        circleAlphaAnimation.duration = EXIT_ANIMATION_DURATION.toLong()
        circleAlphaAnimation.interpolator = DecelerateInterpolator()
        circleAlphaAnimation.addUpdateListener { animation ->
            circleAlpha = animation.animatedValue as Int
            if (circleAlpha == ALPHA_TRANSPARENT) {
                circleAlpha = ALPHA_OPAQUE
            }
        }
        val animatorSet = AnimatorSet()
        animatorSet.play(circleSizeAnimation)
        animatorSet.play(circleAlphaAnimation)
        animatorSet.start()
    }

    fun onDraw(canvas: Canvas) {
        if (!isAnimating) {
            return
        }
        drawCircle(canvas)
        drawIcon(canvas)
    }

    private fun drawCircle(canvas: Canvas) {
        val left = circleCenterX
        val top = circleCenterY
        val right = circleCenterX
        val bottom = circleCenterY
        circleRect[left, top, right] = bottom
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.color = circleColor
        paint.strokeWidth = currentCircleStrokeWidth
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND
        paint.alpha = circleAlpha
        canvas.drawArc(circleRect, startAngle, currentCircleAngle, false, paint)
    }

    private fun drawIcon(canvas: Canvas) {
        if (currentIconBitmap == null || currentCircleAngle == END_CIRCLE_ANGLE.toFloat()) {
            return
        }
        val angle = startAngle + currentCircleAngle
        val centerX = (circleCenterX - currentIconBitmap!!.width / 2.0).toFloat().roundToInt().toFloat()
        val centerY = (circleCenterY - currentIconBitmap!!.height / 2.0).toFloat().roundToInt().toFloat()
        val left = (centerX  * cos(Math.toRadians(angle.toDouble()))).toFloat().roundToInt().toFloat()
        val top = (centerY  * sin(Math.toRadians(angle.toDouble()))).toFloat().roundToInt().toFloat()
        val right = left + iconSourceRect!!.right
        val bottom = top + iconSourceRect!!.bottom
        iconRect[left, top, right] = bottom
        canvas.drawBitmap(currentIconBitmap!!, iconSourceRect, iconRect, null)
    }

    companion object {
        private const val SELECT_ANIMATION_DURATION = 550
        private const val EXIT_ANIMATION_DURATION = 600
        private const val START_CIRCLE_SIZE_RATIO = 1f
        const val END_CIRCLE_SIZE_RATIO = 1.3f
        private const val START_CIRCLE_ANGLE = 1
        private const val END_CIRCLE_ANGLE = 360
        private const val ALPHA_TRANSPARENT = 0
        private const val ALPHA_OPAQUE = 255
    }

}