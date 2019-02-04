package org.michaelbel.tjgram.utils

import android.animation.ObjectAnimator
import android.animation.StateListAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import timber.log.Timber

@Suppress("unused")
object ViewUtil {

    fun clearCursorDrawable(editText: EditText?) {
        if (editText == null) {
            return
        }

        try {
            val mCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            mCursorDrawableRes.isAccessible = true
            mCursorDrawableRes.setInt(editText, 0)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun getIcon(context: Context, @DrawableRes resource: Int, colorFilter: Int): Drawable? {
        return getIcon(context, resource, colorFilter, PorterDuff.Mode.MULTIPLY)
    }

    private fun getIcon(context: Context, @DrawableRes resource: Int, colorFilter: Int, mode: PorterDuff.Mode): Drawable? {
        val iconDrawable = ContextCompat.getDrawable(context, resource)
        val color = ContextCompat.getColor(context, colorFilter)

        if (iconDrawable != null) {
            iconDrawable.clearColorFilter()
            iconDrawable.mutate().setColorFilter(color, mode)
        }

        return iconDrawable
    }

    fun getAttrColor(@NonNull context: Context, @AttrRes colorAttr: Int): Int {
        var color = 0
        val attrs = intArrayOf(colorAttr)

        try {
            val typedArray = context.obtainStyledAttributes(attrs)
            color = typedArray.getColor(0, 0)
            typedArray.recycle()
        } catch (e: Exception) {
            Timber.e(e)
        }

        return color
    }

    fun selectableItemBackground(context: Context): Int {
        val attrs = intArrayOf(org.michaelbel.tjgram.R.attr.selectableItemBackground)
        val typedArray = context.obtainStyledAttributes(attrs)
        val backgroundResource = typedArray.getResourceId(0, 0)
        typedArray.recycle()
        return backgroundResource
    }

    fun selectableItemBackgroundBorderless(context: Context): Int {
        val attrs = intArrayOf(org.michaelbel.tjgram.R.attr.selectableItemBackgroundBorderless)
        val typedArray = context.obtainStyledAttributes(attrs)
        val backgroundResource = typedArray.getResourceId(0, 0)
        typedArray.recycle()
        return backgroundResource
    }

    fun selectableItemBackgroundDrawable(context: Context): Drawable? {
        val attrs = intArrayOf(android.R.attr.selectableItemBackground)
        val typedArray = context.obtainStyledAttributes(attrs)
        val drawableFromTheme = typedArray.getDrawable(0)
        typedArray.recycle()
        return drawableFromTheme
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun selectableItemBackgroundBorderlessDrawable(context: Context): Drawable? {
        val attrs = intArrayOf(android.R.attr.selectableItemBackgroundBorderless)
        val typedArray = context.obtainStyledAttributes(attrs)
        val drawableFromTheme = typedArray.getDrawable(0)
        typedArray.recycle()
        return drawableFromTheme
    }

    @SuppressLint("PrivateResource")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun setElevation(view: View, elevation : Float) {
        val dur = view.resources.getInteger(org.michaelbel.tjgram.R.integer.app_bar_elevation_anim_duration)
        val sla = StateListAnimator()
        sla.addState(intArrayOf(16842766, org.michaelbel.tjgram.R.attr.state_liftable, -org.michaelbel.tjgram.R.attr.state_lifted), ObjectAnimator.ofFloat(view, "elevation", 0.0f).setDuration(dur.toLong()))
        sla.addState(intArrayOf(16842766), ObjectAnimator.ofFloat(view, "elevation", elevation).setDuration(dur.toLong()))
        sla.addState(IntArray(0), ObjectAnimator.ofFloat(view, "elevation", 0.0f).setDuration(0L))
        view.stateListAnimator = sla
    }

    fun setScrollFlags(view: View, flags: Int): AppBarLayout.LayoutParams {
        val params = view.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = flags
        return params
    }
}