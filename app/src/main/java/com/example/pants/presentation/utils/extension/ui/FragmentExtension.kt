package com.example.pants.presentation.utils.extension.ui

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pants.presentation.utils.extension.ui.DialogTags.ERROR_DIALOG_TAG
import com.example.pants.presentation.ui.error.ErrorDialogFragment

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showErrorDialog(message: String) {
    val dialog = ErrorDialogFragment.newInstance(message)
    dialog.show(childFragmentManager, ERROR_DIALOG_TAG)
}

object DialogTags {
    const val ERROR_DIALOG_TAG = "ErrorDialog"
}
