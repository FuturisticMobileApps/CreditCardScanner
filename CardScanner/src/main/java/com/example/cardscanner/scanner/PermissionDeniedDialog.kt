package com.example.cardscanner.scanner

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PermissionDeniedDialog(
    private val settingsClicked: () -> Unit,
    private val manualEntryClicked: () -> Unit,
) : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return MaterialAlertDialogBuilder(requireContext()).apply {

            setTitle("Permission denied")

            setMessage("Camera permission is necessary to scan a card!. Allow it in the settings or Enter details manually!")

            setNegativeButton("Enter details manually") { _, _ ->
                manualEntryClicked()
                dismiss()
            }

            setPositiveButton("Settings") { _, _ ->
                settingsClicked()
                dismiss()
            }

        }.create()

    }
}