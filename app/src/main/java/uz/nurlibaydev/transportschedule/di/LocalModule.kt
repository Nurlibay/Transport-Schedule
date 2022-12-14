package uz.nurlibaydev.transportschedule.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.nurlibaydev.transportschedule.data.sharedpref.SharePref
import javax.inject.Singleton

/**
 *  Created by Nurlibay Koshkinbaev on 17/11/2022 00:43
 */

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @[Provides Singleton]
    fun getSharedPreference(@ApplicationContext context: Context): SharePref = SharePref(context)
}