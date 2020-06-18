package com.rk.appscatalog.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by ZMN on 18/06/2020.
 **/
class SpacingItemDecoration(context: Context, @DimenRes spacingDimen: Int) :
    RecyclerView.ItemDecoration() {

    private val spacingInPixel: Int = context.resources.getDimensionPixelOffset(spacingDimen)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val layoutManager = parent.layoutManager ?: return
        val childAdapterPosition = parent.getChildAdapterPosition(view)

        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            val column = childAdapterPosition % spanCount

            outRect.left = column * spacingInPixel / spanCount
            outRect.right = spacingInPixel - (column + 1) * spacingInPixel / spanCount

            if (childAdapterPosition >= spanCount) {
                outRect.top = spacingInPixel
            }
        } else if (layoutManager is LinearLayoutManager) {
            if (childAdapterPosition <= 0) {
                return
            }

            if (layoutManager.orientation == RecyclerView.VERTICAL) {
                outRect.top = spacingInPixel
            } else {
                outRect.left = spacingInPixel
            }
        }
    }
}