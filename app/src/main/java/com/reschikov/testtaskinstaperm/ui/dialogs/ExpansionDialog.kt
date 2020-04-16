package com.reschikov.testtaskinstaperm.ui.dialogs

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.reschikov.testtaskinstaperm.R

fun Activity.showAlertDialog(title: String, message: String){
    AlertDialog.Builder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
        .setTitle(title)
        .setIcon(R.drawable.ic_warning)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton(getString(R.string.but_ok)){ dialog, which ->
            dialog.dismiss()
        }
        .create()
        .show()
}