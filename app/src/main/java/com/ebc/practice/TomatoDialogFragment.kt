/*package com.ebc.practice

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment

class TomatoDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tomato_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val confirmButton: Button = view.findViewById(R.id.confirm_button)
        confirmButton.setOnClickListener {
            // Inform MainActivity that the coffee break was confirmed
            (activity as? MainActivity)?.onCoffeeBreakConfirmed()
            dismiss() // Close the dialog
        }
    }
}


 */