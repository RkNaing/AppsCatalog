package com.rk.appscatalog.ui.views

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton

/**
 * Created by ZMN on 17/06/2020.
 **/
class MaterialToggleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
):  MaterialButton(context, attrs, defStyleAttr){

    override fun toggle() {
        if (!isChecked) {
            super.toggle()
        }
    }
}