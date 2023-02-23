package com.pethabittracker.gora.presentation.extensions

import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import com.pethabittracker.gora.R

fun RecyclerView.addVerticalGaps(@DimenRes gapRes: Int = R.dimen.gap) {

    val verticalGap = context.resources.getDimensionPixelSize(gapRes)
    addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            if (position != parent.adapter?.itemCount?.minus(1)) {
                outRect.bottom = verticalGap
            }
        }
    })
}