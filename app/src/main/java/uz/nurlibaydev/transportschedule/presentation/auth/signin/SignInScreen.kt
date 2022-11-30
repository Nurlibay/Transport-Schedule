package uz.nurlibaydev.transportschedule.presentation.auth.signin

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
import uz.nurlibaydev.transportschedule.databinding.ScreenSigninBinding
import uz.nurlibaydev.transportschedule.utils.UiState
import uz.nurlibaydev.transportschedule.utils.extenions.onClick
import uz.nurlibaydev.transportschedule.utils.extenions.showToast

/**
 *  Created by Nurlibay Koshkinbaev on 17/11/2022 00:14
 */

@AndroidEntryPoint
class SignInScreen: Fragment(R.layout.screen_signin) {

    private val binding: ScreenSigninBinding by viewBinding()
    private val navController: NavController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }
    private val viewModel: SignInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        binding.apply {
            tvSignUp.onClick {
                navController.navigate(SignInScreenDirections.actionSignInScreenToSignUpScreen())
            }
            btnSignIn.onClick {
                if(validate()){
                    viewModel.signIn(etEmail.text.toString(), etPassword.text.toString())
                }
            }
        }
    }

    private fun setupObserver() {
        lifecycleScope.launchWhenResumed {
            viewModel.signIn.collect {
                when (it) {
                    is UiState.Loading -> {
                        setLoading(true)
                    }
                    is UiState.NetworkError -> {
                        setLoading(false)
                    }
                    is UiState.Error -> {
                        showToast(it.msg.toString())
                        setLoading(false)
                    }
                    is UiState.Success -> {
                        setLoading(false)
                        navController.navigate(SignInScreenDirections.actionSignInScreenToMainScreen())
                        showToast("Success!")
                    }
                    else -> {

                    }
                }
            }
        }
    }

    private fun validate(): Boolean {
        binding.apply {
            return if (etEmail.text!!.isNotEmpty() && etPassword.text!!.isNotEmpty()
                && etPassword.length() >= 6
            ) {
                true
            } else if (etPassword.length() < 6) {
                tilPassword.error = getString(R.string.password_length_condition)
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
            etPassword.isEnabled = !isLoading
        }
    }
}