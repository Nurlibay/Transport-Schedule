package uz.nurlibaydev.transportschedule.data.helper

import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import uz.nurlibaydev.transportschedule.data.models.TaxiData
import uz.nurlibaydev.transportschedule.utils.Constants
import uz.nurlibaydev.transportschedule.utils.ResultData
import uz.nurlibaydev.transportschedule.utils.hasConnection
import javax.inject.Inject

// Created by Jamshid Isoqov an 11/19/2022
@ViewModelScoped
class TaxiHelper @Inject constructor(
    private val fireStore: FirebaseFirestore
) {

    fun getAllTaxi(): Flow<ResultData<List<TaxiData>>> = callbackFlow<ResultData<List<TaxiData>>> {
        if (hasConnection()) {
            fireStore.collection(Constants.TAXIS)
                .addSnapshotListener { value, error ->
                    if (error == null) {
                        if (value!=null){
                            value.documents
                                .map {
                                it.toObject(TaxiData::class.java)
                            }
                        }else{
                            trySend(ResultData.Message("Information not found"))
                        }
                    } else {
                        throw error
                    }
                }
        } else {
            throw RuntimeException("No internet connection")
        }
    }.catch { error ->
        emit(ResultData.error(error))
    }.flowOn(Dispatchers.IO)
}