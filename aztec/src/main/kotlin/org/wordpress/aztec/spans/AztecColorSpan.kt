package org.wordpress.aztec.spans

import android.text.style.ForegroundColorSpan
import org.wordpress.aztec.AztecAttributes

class AztecColorSpan(
        val color: Int,
        tag: String = "color",
        override var attributes: AztecAttributes = AztecAttributes()
) : ForegroundColorSpan(color), IAztecInlineSpan {

    constructor(span: AztecColorSpan) : this(span.color)

    fun getColorHex(): String {
        return java.lang.String.format("#%06X", 0xFFFFFF and color)
    }

    override val TAG = tag

    override fun toString(): String {
        return "AztecColorSpan {$color}"
    }
}