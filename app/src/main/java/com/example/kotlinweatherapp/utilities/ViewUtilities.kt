package com.example.kotlinweatherapp.utilities

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_SHORT
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}

fun View.showSnackbarNoAction(
    message: String,
    length: Int
) {
    Snackbar.make(this, message, length).show()
}