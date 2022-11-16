package uz.nurlibaydev.transportschedule.app

import android.app.Application
import timber.log.Timber
import uz.nurlibaydev.transportschedule.BuildConfig

/**
 *  Created by Nurlibay Koshkinbaev on 16/11/2022 23:23
 */

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}