package uz.nurlibaydev.transportschedule.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.nurlibaydev.transportschedule.R
import uz.nurlibaydev.transportschedule.data.sharedpref.SharePref
import uz.nurlibaydev.transportschedule.databinding.DialogChooseLanguageBinding
import uz.nurlibaydev.transportschedule.presentation.MainActivity
import uz.nurlibaydev.transportschedule.utils.extenions.onClick
import javax.inject.Inject

@AndroidEntryPoint
class LanguageDialog : DialogFragment() {

    private val binding: DialogChooseLanguageBinding by viewBinding()
    @Inject
    lateinit var pref: SharePref

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireDialog().window?.setBackgroundDrawableResource(R.drawable.shape_dialog)
        return inflater.inflate(R.layout.dialog_choose_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvEng.onClick {
                pref.language = "en"
                dismiss()
                (requireActivity() as MainActivity).setNewLocale()
                dismiss()
            }
            tvRu.onClick {
                pref.language = "ru"
                dismiss()
                (requireActivity() as MainActivity).setNewLocale()
                dismiss()
            }
        }
    }
}