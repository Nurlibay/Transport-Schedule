package uz.nurlibaydev.transportschedule.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.nurlibaydev.transportschedule.data.helper.TaxiHelper
import uz.nurlibaydev.transportschedule.data.models.TaxiData
import javax.inject.Inject

@HiltViewModel
class MainViewModelImpl @Inject constructor(
    private val taxiHelper: TaxiHelper,
) : MainViewModel, ViewModel() {

    override val allTaxis = MutableStateFlow<List<TaxiData>>(emptyList())

    override val messageFlow = MutableSharedFlow<String>()

    override val errorFlow = MutableSharedFlow<String>()

    override val progressFlow = MutableSharedFlow<Boolean>()

    private var taxis: List<TaxiData> = emptyList()

    override fun getAllTaxis() {
        viewModelScope.launch {
            progressFlow.emit(true)
            taxiHelper.getAllTaxi()
                .collectLatest {
                    progressFlow.emit(false)
                    it.onSuccess { data ->
                        taxis = data
                        allTaxis.emit(data)
                    }.onMessage { message ->
                        messageFlow.emit(message)
                    }.onError { error ->
                        errorFlow.emit(error.localizedMessage ?: "Unknown error")
                    }
                }
        }
    }

    override fun searchTaxis(query: String) {
        viewModelScope.launch {
            val list = taxis.filter {
                it.taxiName.contains(query) || it.phone.startsWith(query)
            }
            allTaxis.emit(list)
        }
    }
}