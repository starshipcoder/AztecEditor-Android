package org.wordpress.aztec

import android.app.Activity
import android.widget.ImageButton
import android.widget.ToggleButton
import androidx.core.text.HtmlCompat
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.wordpress.aztec.colorpicker.ColorPickerView
import org.wordpress.aztec.plugins.CssBackgroundColorPlugin
import org.wordpress.aztec.plugins.CssColorPlugin
import org.wordpress.aztec.source.SourceViewEditText
import org.wordpress.aztec.toolbar.AztecToolbar

/**
 * Combined test for toolbar and inline styles.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = intArrayOf(28))
class AztecToolbarColorTest {
    lateinit var editText: AztecText
    lateinit var sourceText: SourceViewEditText
    lateinit var toolbar: AztecToolbar

    lateinit var colorButton: ImageButton
    lateinit var boldButton: ToggleButton
    lateinit var colorPickerView: ColorPickerView

    private val COLOR_BEGIN = 1234
    private val COLOR_CHANGE = 4321

    /**
     * Initialize variables.
     */
    @Before
    fun init() {
        val activity = Robolectric.buildActivity(Activity::class.java).create().visible().get()
        editText = AztecText(activity)
        editText.setCalypsoMode(false)
        sourceText = SourceViewEditText(activity)
        colorPickerView = ColorPickerView(activity)

        activity.setContentView(editText)
        toolbar = AztecToolbar(activity)
        toolbar.setEditor(editText, sourceText)

        toolbar.setColorPicker(colorPickerView)

        boldButton = toolbar.findViewById(R.id.format_bar_button_bold)
        colorButton = toolbar.findViewById(R.id.format_bar_button_color)

//        editText.plugins.add(CssBackgroundColorPlugin())
//        editText.plugins.add(CssColorPlugin())
    }

    private fun checkColor(editText: AztecText, array: Array<Int?>) {
        Assert.assertEquals(array.size, editText.text.length)

        for (i in editText.text.indices) {
            editText.setSelection(i, i)
            Assert.assertEquals(" indice $i", array[i]?.and(0xFFFFFF), (editText.inlineFormatter.spanColor)?.and(0xFFFFFF))
        }
    }

    private fun AztecText.toFromHtml() {
        val html = HtmlCompat.toHtml(this.text, HtmlCompat.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL)
        System.out.println("html: $html")
        this.fromHtml(html, false)
    }


    /**
     * Testing initial state of the editor and a toolbar.
     *
     * @throws Exception
     */
    @Test
    @Throws(Exception::class)
    fun initialState() {
        Assert.assertFalse(boldButton.isChecked)

        Assert.assertTrue(TestUtils.safeEmpty(editText))
    }

    /**
     * Toggle bold button and type.
     *
     * @throws Exception
     */
    @Test
    @Throws(Exception::class)
    fun `test select text and color it`() {
        Assert.assertNull(editText.inlineFormatter.spanColor)

        editText.append("color")
        editText.setSelection(0, editText.length())
        colorPickerView.onPickColor(COLOR_BEGIN)

        editText.toFromHtml()

        checkColor(editText, arrayOf(COLOR_BEGIN, COLOR_BEGIN, COLOR_BEGIN, COLOR_BEGIN, COLOR_BEGIN))
    }

    @Test
    @Throws(Exception::class)
    fun `test select text and color it twice so should be black again`() {
        Assert.assertNull(editText.inlineFormatter.spanColor)

        editText.append("color")
        editText.setSelection(0, editText.length())
        
        colorPickerView.onPickColor(COLOR_BEGIN)
        System.out.println("***********************")
        colorPickerView.onPickColor(COLOR_BEGIN)

        editText.toFromHtml()

        checkColor(editText, arrayOf(null, null, null, null, null))

    }

    @Test
    @Throws(Exception::class)
    fun `test insert text inside colored ones`() {
        Assert.assertNull(editText.inlineFormatter.spanColor)


        editText.append("color")
        editText.setSelection(0, editText.length())

        colorPickerView.onPickColor(COLOR_BEGIN)

        editText.setSelection(1,1)

        editText.text.insert(editText.selectionStart, "tp")

        editText.toFromHtml()

        checkColor(editText, arrayOf(COLOR_BEGIN, COLOR_BEGIN, COLOR_BEGIN, COLOR_BEGIN, COLOR_BEGIN, COLOR_BEGIN, COLOR_BEGIN))
    }

    @Test
    @Throws(Exception::class)
    fun `test add text in middle of a colored one`() {
        Assert.assertNull(editText.inlineFormatter.spanColor)

        editText.append("color")
        System.out.println("***********************")
        editText.setSelection(0, editText.length())
        System.out.println("***********************")

        colorPickerView.onPickColor(COLOR_BEGIN)
        System.out.println("***********************")

        editText.setSelection(1, 3)
        System.out.println("***********************")
        colorPickerView.onPickColor(COLOR_CHANGE)
        System.out.println("***********************")

        editText.toFromHtml()

        checkColor(editText, arrayOf(COLOR_BEGIN, COLOR_BEGIN, COLOR_CHANGE, COLOR_CHANGE, COLOR_BEGIN))
    }

    @Test
    @Throws(Exception::class)
    fun `test add text with a new color in middle of a colored one`() {
        Assert.assertNull(editText.inlineFormatter.spanColor)

        editText.append("color")
        editText.setSelection(0, editText.length())

        colorPickerView.onPickColor(COLOR_BEGIN)

        System.out.println("*****************************")

        editText.setSelection(2, 2)
        colorPickerView.onPickColor(COLOR_CHANGE)
        Assert.assertEquals(COLOR_CHANGE, editText.inlineFormatter.spanColor)

        editText.text.insert(editText.selectionStart, "X")
        editText.inlineFormatter.joinStyleSpans(0, editText.text.length)

        editText.toFromHtml()

        checkColor(editText, arrayOf(COLOR_BEGIN, COLOR_BEGIN, COLOR_BEGIN, COLOR_CHANGE, COLOR_BEGIN, COLOR_BEGIN))

    }
}
