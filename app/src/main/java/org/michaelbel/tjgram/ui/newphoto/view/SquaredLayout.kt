package org.michaelbel.tjgram.ui.newphoto.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

@Suppress("unused")
class SquaredLayout : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredWidth)
    }
}