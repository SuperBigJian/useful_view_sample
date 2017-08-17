package com.midea.fridge.useful_view_library.view

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.*
import com.midea.fridge.useful_view_library.R
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.buttonDrawableResource
import org.jetbrains.anko.textColor

/**
 * Created by chenjian on 17-8-16.
 */
class UsefulEditText(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    val mTitle: TextView = TextView(context)
    val mEdit: EditText = EditText(context, attrs)
    val mClear: ImageView = ImageView(context)
    val mShow: CheckBox = CheckBox(context)

    var mWatcher: TextWatcher? = null
    var content: String
        get() = mEdit.text.toString()
        set(value) {
            mEdit.setText(value)
        }

    fun getLength() = mEdit.length()

    fun setSelection(index: Int) {
        mEdit.setSelection(index)
    }

    private val titleParams: LayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
    private val editParams: LayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
    private val showParams: LayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
    private val clearParams: LayoutParams = LayoutParams(36, 36)

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.UsefulEditText)

        val bg = ta.getResourceId(R.styleable.UsefulEditText_ue_background, R.drawable.useful_et_bg)

        val titleText = ta.getText(R.styleable.UsefulEditText_titleText)
        val titleColor = ta.getColor(R.styleable.UsefulEditText_titleTextColor, Color.BLACK)
        val titleSize = ta.getDimension(R.styleable.UsefulEditText_titleTextSize, 30f)

        val bt = ta.getResourceId(R.styleable.UsefulEditText_ue_Button, R.drawable.useful_cb_showpsd)
        val cSrc = ta.getResourceId(R.styleable.UsefulEditText_ue_clearSrc, R.drawable.useful_ic_delete)
        val isPassword = ta.getBoolean(R.styleable.UsefulEditText_ue_isPassword, false)
        ta.recycle()

        gravity = Gravity.CENTER_VERTICAL
        backgroundResource = bg
        setPadding(30, 0, 0, 0)

        if (!titleText.isNullOrEmpty()) {
            mTitle.text = titleText
            mTitle.textColor = titleColor
            mTitle.textSize = titleSize
            mTitle.gravity = Gravity.CENTER_VERTICAL
            titleParams.marginEnd = 10
            addView(mTitle, titleParams)
        }

        editParams.weight = 1.0F
        editParams.marginEnd = 10
        mEdit.background = null
        addView(mEdit, editParams)

        mClear.setImageResource(cSrc)
        clearParams.marginEnd = 18
        mClear.visibility = View.GONE
        addView(mClear, clearParams)

        mShow.buttonDrawableResource = bt
        mShow.text = ""
        mShow.isChecked = false
        showParams.marginEnd = 30
        addView(mShow, showParams)

        if (isPassword) {
            mEdit.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        } else {
            mShow.visibility = View.GONE
        }

        initEvent()
    }

    private fun initEvent() {

        mEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(str: Editable?) {
                if (str.isNullOrEmpty())
                    mClear.visibility = View.GONE
                else
                    mClear.visibility = View.VISIBLE

                mWatcher?.afterTextChanged(str)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mWatcher?.beforeTextChanged(p0, p1, p2, p3)
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mWatcher?.onTextChanged(p0, p1, p2, p3)
            }
        })

        mShow.isChecked = false
        mShow.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mEdit.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                mEdit.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            mEdit.setSelection(mEdit.length())//set cursor to the end
        }

        mClear.setOnClickListener {
            mEdit.setText("")
        }

    }


}