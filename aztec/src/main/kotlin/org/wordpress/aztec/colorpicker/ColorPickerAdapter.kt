package org.wordpress.aztec.colorpicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wordpress.aztec.R

class ColorPickerAdapter(
        private val colors: IntArray,
        private val listener: Listener
) : RecyclerView.Adapter<ColorPickerAdapter.ColorViewHolder>() {

    private var selected = 0

    interface Listener {
        fun onColorClick(color: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false))
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.itemView.setBackgroundColor(colors[position])
        holder.checkedIcon.visibility = if (selected == position) View.VISIBLE else View.GONE
        holder.itemView.setOnClickListener {
            listener.onColorClick(colors[position])
        }
    }

    override fun getItemCount() = colors.size

    fun setSelected(selectedColor: Int?) {
        colors.forEachIndexed { index, i ->
            if (i == selectedColor) {
                selected = index
                notifyDataSetChanged()
                return
            }
        }
        selected = 0
        notifyDataSetChanged()
    }

    class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkedIcon: View = itemView.findViewById<View>(R.id.checkedIcon)
    }
}
