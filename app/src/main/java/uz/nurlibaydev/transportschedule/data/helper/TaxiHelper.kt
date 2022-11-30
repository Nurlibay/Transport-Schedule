package uz.nurlibaydev.transportschedule.data.helper

import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import uz.nurlibaydev.transportschedule.data.models.TaxiData
import uz.nurlibaydev.transportschedule.utils.Constants
import uz.nurlibaydev.transportschedule.utils.ResultData
import uz.nurlibaydev.transportschedule.utils.hasConnection
import javax.inject.Inject

// Created by Jamshid Isoqov an 11/19/2022
@ViewModelScoped
class TaxiHelper @Inject constructor(
    private val fireStore: FirebaseFirestore,
) {

    fun getAllTaxi(): Flow<ResultData<List<TaxiData>>> = callbackFlow<ResultData<List<TaxiData>>> {
        if (hasConnection()) {
            fireStore.collection(Constants.TAXIS).get()
                .addOnSuccessListener {
                    val taxiData = it.documents.map { doc ->
                        doc.toObject(TaxiData::class.java)!!
                    }
                    trySend(ResultData.Success(taxiData))
                }
                .addOnFailureListener {
                    trySend(ResultData.Message(it.localizedMessage!!.toString()))
                }
        } else {
            trySend(ResultData.Message("No internet connection!"))
        }
        awaitClose { }
    }.catch { error ->
        emit(ResultData.error(error))
    }.flowOn(Dispatchers.IO)
}