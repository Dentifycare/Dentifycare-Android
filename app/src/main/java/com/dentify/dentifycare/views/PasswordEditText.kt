package com.dentify.dentifycare.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
): AppCompatEditText(context, attrs) {

    var isPasswordValid: Boolean = false

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        if (text.toString().length < 8) {
            setError("Password must not be less than 8 characters", null)
            isPasswordValid = false
        } else {
            setError(null, null)
            isPasswordValid = true
        }
    }
}