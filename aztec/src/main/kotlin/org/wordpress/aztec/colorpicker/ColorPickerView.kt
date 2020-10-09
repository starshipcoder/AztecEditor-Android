package org.wordpress.aztec.colorpicker

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.wordpress.aztec.R

class ColorPickerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    private var listener: ColorPickerListener? = null

    private var colors: IntArray

    private fun initView() {
        adapter = ColorPickerAdapter(colors, object : ColorPickerAdapter.Listener {
            override fun onColorClick(color: Int) {
                listener?.onPickColor(color)
            }
        })
        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
    }

    fun setColorPickerListener(listener: ColorPickerListener?) {
        this.listener = listener
    }

    fun setColor(selectedColor: Int?) {
        (adapter as ColorPickerAdapter).setSelected(selectedColor)
    }

    //for test
    fun onPickColor(color: Int) {
        listener?.onPickColor(color)
    }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerView)
        val colorsId = ta.getResourceId(R.styleable.ColorPickerView_colors, R.array.colorPickerColors)
        colors = ta.resources.getIntArray(colorsId)
        ta.recycle()
        initView()
    }
}