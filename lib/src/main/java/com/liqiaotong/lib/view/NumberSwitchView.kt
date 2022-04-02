package com.liqiaotong.lib.view

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.liqiaotong.lib.R
import kotlin.math.roundToInt

class NumberSwitchView : View {

    private val dp2px = 0.5f + context.resources.displayMetrics.scaledDensity
    private val defaultTextSize = 12f + dp2px
    private val defaultColor = Color.parseColor("#000000")
    private val defaultBackgroundWidth = 1f + dp2px

    private var textPaint: Paint = Paint()
    private var backgroundPaint: Paint = Paint()
    private var text: String = ""
    private var textSize: Float = defaultTextSize
    private var unSelectTextColor: Int = defaultColor
    private var selectedTextColor: Int = defaultColor
    private var backgroundWidth: Float = defaultBackgroundWidth
    private var unSelectBackgroundColor: Int = defaultColor
    private var selectedBackgroundColor: Int = defaultColor
    private var scale: Float = 0.85f
    private var scaleProgress: Float = 0.85f
    private var isNotScale: Boolean = false
    private var duration: Long = 200L
    private var valueAnimator: ValueAnimator? = null
    private var interpolator:TimeInterpolator? = DecelerateInterpolator()
    private val textRect = Rect()

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet? = null) {
        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER

        backgroundPaint.isAntiAlias = true
        backgroundPaint.style = Paint.Style.STROKE

        attrs?.let {
            var typeArray =
                context.obtainStyledAttributes(it, R.styleable.NumberSwitchView, -1, 0)
            text = typeArray.getString(R.styleable.NumberSwitchView_nsv_text) ?: ""
            textSize = typeArray.getDimension(
                R.styleable.NumberSwitchView_nsv_text_size,
                defaultTextSize
            )
            scale = typeArray.getFloat(
                R.styleable.NumberSwitchView_nsv_scale,
                0.85f
            )
            scaleProgress = scale

            duration = typeArray.getFloat(
                R.styleable.NumberSwitchView_nsv_duration,
                200f
            )?.toLong()
            backgroundWidth = typeArray.getDimension(
                R.styleable.NumberSwitchView_nsv_background_width,
                defaultBackgroundWidth
            )
            selectedTextColor = typeArray.getColor(
                R.styleable.NumberSwitchView_nsv_text_selected_color,
                defaultColor
            )
            selectedTextColor = typeArray.getColor(
                R.styleable.NumberSwitchView_nsv_text_selected_color,
                defaultColor
            )
            unSelectTextColor = typeArray.getColor(
                R.styleable.NumberSwitchView_nsv_text_unselect_color,
                defaultColor
            )
            selectedBackgroundColor = typeArray.getColor(
                R.styleable.NumberSwitchView_nsv_background_selected_color,
                defaultColor
            )
            unSelectBackgroundColor = typeArray.getColor(
                R.styleable.NumberSwitchView_nsv_background_unselect_color,
                defaultColor
            )
            typeArray.recycle()
        }

        textPaint?.textSize = textSize
        textPaint?.color = unSelectTextColor
        backgroundPaint?.strokeWidth = backgroundWidth
        backgroundPaint?.color = unSelectBackgroundColor

        invalidate()
    }

    fun setScale(scale: Float? = null) {
        scale?.let {
            this.scale = it
            this.scaleProgress = it
        }
        invalidate()
    }

    fun setText(text: String? = null) {
        text?.let { this.text = it }
        invalidate()
    }

    fun setTextSize(textSize: Float? = null) {
        textSize?.let { this.textSize = it }
        invalidate()
    }

    fun setUnSelectTextColor(unSelectTextColor: Int? = null) {
        unSelectTextColor?.let { this.unSelectTextColor = it }
        invalidate()
    }

    fun setSelectedTextColor(selectedTextColor: Int? = null) {
        selectedTextColor?.let { this.selectedTextColor = it }
        invalidate()
    }

    fun setBackgroundWidth(backgroundWidth: Float? = null) {
        backgroundWidth?.let { this.backgroundWidth = it }
        invalidate()
    }

    fun setUnSelectBackgroundColor(unSelectBackgroundColor: Int? = null) {
        unSelectBackgroundColor?.let { this.unSelectBackgroundColor = it }
        invalidate()
    }

    fun setSelectedBackgroundColor(selectedBackgroundColor: Int? = null) {
        selectedBackgroundColor?.let { this.selectedBackgroundColor = it }
        invalidate()
    }

    fun setInterpolator(value: TimeInterpolator? = DecelerateInterpolator()){
        interpolator = value
    }

    override fun isSelected(): Boolean {
        return isNotScale
    }

    override fun setSelected(isSelect: Boolean) {
        if (isNotScale != isSelect) {
            isNotScale = isSelect
            startAnimation()
        }
    }

    private fun startAnimation(duration: Long? = null) {
        valueAnimator?.cancel()
        valueAnimator =
            if (isNotScale) ValueAnimator.ofFloat(scaleProgress, 1f) else ValueAnimator.ofFloat(
                scaleProgress,
                scale
            )
        valueAnimator?.duration = duration ?: this.duration
        valueAnimator?.addUpdateListener {
            scaleProgress = it.animatedValue as Float
            invalidate()
        }
        valueAnimator?.interpolator = interpolator
        valueAnimator?.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val progress = (scaleProgress - scale) / (1f - scale)
        val centerX = width * 0.5f
        val centerY = height * 0.5f
        val radius = (if (width < height) width * 0.5f else height * 0.5f) - backgroundWidth * 0.5f
        canvas?.scale(scaleProgress, scaleProgress, centerX, centerY)
        backgroundPaint?.color =
            getColor(unSelectBackgroundColor, selectedBackgroundColor, progress)
        canvas?.drawCircle(centerX, centerY, radius, backgroundPaint)
        textPaint?.color = getColor(unSelectTextColor, selectedTextColor, progress)
        textPaint?.getTextBounds(text, 0, text.length, textRect)
        val textCenterY = centerY + textRect.height() * 0.5f
        canvas?.drawText(text, centerX, textCenterY, textPaint)
    }

    private fun getColor(startColor: Int, endColor: Int, progress: Float): Int {
        fun ave(src: Int, dst: Int, progress: Float): Int {
            return src + (progress * (dst - src)).roundToInt()
        }

        val a = ave(Color.alpha(startColor), Color.alpha(endColor), progress)
        val r = ave(Color.red(startColor), Color.red(endColor), progress)
        val g = ave(Color.green(startColor), Color.green(endColor), progress)
        val b = ave(Color.blue(startColor), Color.blue(endColor), progress)
        return Color.argb(a, r, g, b)
    }

}