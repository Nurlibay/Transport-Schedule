package uz.nurlibaydev.transportschedule.domain

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 *  Created by Nurlibay Koshkinbaev on 17/11/2022 00:49
 */

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @[Provides Singleton]
    fun provideFireStore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @[Provides Singleton]
    fun provideFirebaseAuth():FirebaseAuth = FirebaseAuth.getInstance()

}