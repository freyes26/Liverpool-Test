package com.liverpool.test.liverpooltest.ui.Dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.liverpool.test.liverpooltest.R

class DialogMessage(val title : String, val closeListener: CloseListener) : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(title)
                .setPositiveButton(R.string.close){ dialog, _ ->
                    closeListener.onClose()
                    dialog.dismiss()
                }
            builder.create()
        }?: throw IllegalStateException(getString(R.string.activity_null))
    }
}
class CloseListener(val onClickListener: () -> Unit) {
    fun onClose() = onClickListener()
}