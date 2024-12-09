package com.dentify.dentifycare.views

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    var isEmailValid: Boolean = false

    init {
        inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        val email = text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setError("Invalid email format", null)
            isEmailValid = false
        } else {
            setError(null, null)
            isEmailValid = true
        }
    }
}
