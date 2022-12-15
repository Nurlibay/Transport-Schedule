package uz.nurlibaydev.transportschedule.presentation.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import uz.nurlibaydev.transportschedule.R
import uz.nurlibaydev.transportschedule.data.sharedpref.SharePref
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreen : Fragment(R.layout.screen_splash) {

    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }
    @Inject
    lateinit var pref: SharePref

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenResumed {
            delay(1500)
            if (pref.isSigned) {
                navController.navigate(R.id.action_splashScreen_to_mainScreen)
            } else {
                navController.navigate(SplashScreenDirections.actionSplashScreenToSignInScreen())
            }
        }
    }
}