package uz.nurlibaydev.transportschedule.presentation.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import uz.nurlibaydev.transportschedule.databinding.DialogMessageBinding
import uz.nurlibaydev.transportschedule.utils.extenions.config

// Created by Jamshid Isoqov an 10/12/2022
class MessageDialog(ctx: Context, private val message: String) : Dialog(ctx) {

    private lateinit var binding: DialogMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DialogMessageBinding.inflate(layoutInflater)
        config(binding)
        binding.tvMessage.text = message

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }
}