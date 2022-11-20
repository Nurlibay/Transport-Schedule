package uz.nurlibaydev.transportschedule.presentation.main

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import uz.nurlibaydev.transportschedule.data.models.TaxiData

// Created by Jamshid Isoqov an 11/19/2022
interface MainViewModel {

    val allTaxis: StateFlow<List<TaxiData>>

    val messageFlow:SharedFlow<String>

    val errorFlow:SharedFlow<String>

    val progressFlow:SharedFlow<Boolean>

    fun getAllTaxis()

    fun searchTaxis(query:String)

}