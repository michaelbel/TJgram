package org.michaelbel.tjgram.core.views

import android.view.View

@Suppress("unused")
abstract class DebouncingOnClickListener: View.OnClickListener {

    companion object {
        var enabled = true
        val ENABLE_AGAIN = { enabled = true }
    }

    override fun onClick(v: View) {
        if (enabled) {
            enabled = false
            v.post(ENABLE_AGAIN)
            doClick(v)
        }
    }

    abstract fun doClick(v: View)
}