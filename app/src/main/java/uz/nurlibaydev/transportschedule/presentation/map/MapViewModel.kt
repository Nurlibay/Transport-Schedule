package uz.nurlibaydev.transportschedule.presentation.map

import kotlinx.coroutines.flow.SharedFlow
import uz.nurlibaydev.transportschedule.data.models.Routing
import uz.nurlibaydev.transportschedule.data.models.TaxiData

// Created by Jamshid Isoqov an 11/20/2022
interface MapViewModel {

    val messageFlow:SharedFlow<String>
    
    val errorFlow:SharedFlow<String>

    val progressFlow:SharedFlow<Boolean>

    val routes:SharedFlow<Routing>

    fun finRoutes(taxiData: TaxiData)

}