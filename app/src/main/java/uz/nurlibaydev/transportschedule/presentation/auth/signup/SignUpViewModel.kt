package uz.nurlibaydev.transportschedule.presentation.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.nurlibaydev.transportschedule.data.helper.AuthHelper
import uz.nurlibaydev.transportschedule.utils.UiState
import uz.nurlibaydev.transportschedule.utils.hasConnection
import javax.inject.Inject

/**
 *  Created by Nurlibay Koshkinbaev on 17/11/2022 00:15
 */

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authHelper: AuthHelper,
) : ViewModel() {

    private val mutableSignup = MutableStateFlow<UiState<String>>(UiState.Empty)
    val signup: StateFlow<UiState<String>> = mutableSignup

    fun signUp(
        fullName: String,
        email: String,
        password: String,
    ) {
        if (!hasConnection()) {
            mutableSignup.value = UiState.NetworkError("No Internet connection!")
            return
        }
        viewModelScope.launch {
            mutableSignup.value = UiState.Loading
            authHelper.signUp(
                fullName,
                email,
                password,
                {
                    mutableSignup.value = UiState.Success("")
                },
                {
                    mutableSignup.value = UiState.Error(it.toString())
                }
            )
        }
    }
}