package uz.nurlibaydev.transportschedule.presentation.auth.signup

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.nurlibaydev.transportschedule.R
import uz.nurlibaydev.transportschedule.databinding.ScreenSignupBinding
import uz.nurlibaydev.transportschedule.utils.UiState
import uz.nurlibaydev.transportschedule.utils.extenions.onClick
import uz.nurlibaydev.transportschedule.utils.extenions.showMessage
import uz.nurlibaydev.transportschedule.utils.extenions.showToast

/**
 *  Created by Nurlibay Koshkinbaev on 17/11/2022 00:15
 */

@AndroidEntryPoint
class SignUpScreen : Fragment(R.layout.screen_signup) {

    private val binding: ScreenSignupBinding by viewBinding()
    private val navController: NavController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }
    private val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        binding.apply {
            tvSignIn.onClick {
                navController.navigate(SignUpScreenDirections.actionSignUpScreenToSignInScreen())
            }
            btnSignUp.onClick {
                if (validate()) {
                    viewModel.signUp(
                        etFullName.text.toString(),
                        etEmail.text.toString(),
                        etPassword.text.toString()
                    )
                } else {
                    showToast("Invalid data!")
                }
            }
        }
    }

    private fun setupObserver() {
        lifecycleScope.launchWhenResumed {
            viewModel.signup.collect {
                when (it) {
                    is UiState.Loading -> {
                        setLoading(true)
                        showToast("Loading...")
                    }
                    is UiState.NetworkError -> {
                        setLoading(false)
                        showToast("Network Error!")
                    }
                    is UiState.Error -> {
                        showToast(it.msg.toString())
                        setLoading(false)
                    }
                    is UiState.Success -> {
                        setLoading(false)
                        navController.navigate(SignUpScreenDirections.actionSignUpScreenToMainScreen())
                    }
                    else -> {
                        showToast("Unknown Error!")
                    }
                }
            }
        }
    }

    private fun validate(): Boolean {
        binding.apply {
            return if (etEmail.text!!.isNotEmpty() && etPassword.text!!.isNotEmpty() && etFullName.text!!.isNotEmpty() && etPassword.length() >= 6) {
                true
            } else if (etPassword.length() < 6) {
                tilPassword.error = getString(R.string.password_length_condition)
                false
            } else if (etFullName.text.isNullOrEmpty()) {
                tilPassword.error = getString(R.string.enter_full_name)
                false
            } else {
                false
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.apply {
            progressBar.isVisible = isLoading
            etEmail.isEnabled = !isLoading
            etFullName.isEnabled = !isLoading
            etPassword.isEnabled = !isLoading
        }
    }
}