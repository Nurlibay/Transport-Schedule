package uz.nurlibaydev.transportschedule.presentation.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.nurlibaydev.transportschedule.data.helper.AuthHelper
import uz.nurlibaydev.transportschedule.utils.UiState
import uz.nurlibaydev.transportschedule.utils.hasConnection
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authHelper: AuthHelper
): ViewModel() {

    private val mutableSignIn = MutableStateFlow<UiState<String>>(UiState.Empty)
    val signIn: StateFlow<UiState<String>> = mutableSignIn

    fun signIn(
        email: String,
        password: String,
    ) {
        if (!hasConnection()) {
            mutableSignIn.value = UiState.NetworkError("No Internet connection!")
            return
        }
        viewModelScope.launch {
            mutableSignIn.value = UiState.Loading
            authHelper.signIn(
                email,
                password,
                {
                    mutableSignIn.value = UiState.Success("")
                },
                {
                    mutableSignIn.value = UiState.Error(it.toString())
                }
            )
        }
    }
}