package uz.nurlibaydev.transportschedule.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.widget.textChanges
import uz.nurlibaydev.transportschedule.R
import uz.nurlibaydev.transportschedule.data.sharedpref.SharePref
import uz.nurlibaydev.transportschedule.databinding.ScreenMainBinding
import uz.nurlibaydev.transportschedule.presentation.dialogs.LanguageDialog
import uz.nurlibaydev.transportschedule.presentation.dialogs.ProgressDialog
import uz.nurlibaydev.transportschedule.utils.extenions.onClick
import uz.nurlibaydev.transportschedule.utils.extenions.showError
import uz.nurlibaydev.transportschedule.utils.extenions.showMessage
import javax.inject.Inject

// Created by Jamshid Isoqov an 11/18/2022
@AndroidEntryPoint
class MainScreen : Fragment(R.layout.screen_main) {

    private val viewModel: MainViewModel by viewModels<MainViewModelImpl>()
    private val viewBinding: ScreenMainBinding by viewBinding()
    private val adapter: MainAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MainAdapter()
    }
    private lateinit var dialog: ProgressDialog
    @Inject
    lateinit var pref: SharePref

    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pref.isSigned = true
        viewBinding.listTaxi.adapter = adapter

        dialog = ProgressDialog(ctx = requireContext(), "Progress")

        viewModel.getAllTaxis()

        viewBinding.btnLang.onClick {
            val dialog = LanguageDialog()
            dialog.show(requireActivity().supportFragmentManager, "LanguageDialog")
        }

        viewBinding.inputSearch
            .textChanges()
            .debounce(200L)
            .onEach {
                viewModel.searchTaxis(it.toString())
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.allTaxis.onEach {
            adapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        adapter.setItemClickListener {
           findNavController().navigate(MainScreenDirections.actionMainScreenToMapScreen(it))
        }

        viewModel.errorFlow.onEach {
            showError(it)
        }.launchIn(lifecycleScope)

        viewModel.messageFlow.onEach {
            showMessage(it)
        }.launchIn(lifecycleScope)

        viewModel.progressFlow.onEach {
            if (it) {
                dialog.show()
            } else {
                dialog.cancel()
            }
        }.launchIn(lifecycleScope)
    }
}