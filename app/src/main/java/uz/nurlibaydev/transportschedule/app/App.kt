package uz.nurlibaydev.transportschedule.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import uz.nurlibaydev.transportschedule.BuildConfig

/**
 *  Created by Nurlibay Koshkinbaev on 16/11/2022 23:23
 */

@HiltAndroidApp
class App : Application() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        instance = this
    }
}