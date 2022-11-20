package uz.nurlibaydev.transportschedule.data.models

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize
import java.util.*

// Created by Jamshid Isoqov an 11/19/2022
@Parcelize
data class TaxiData(
    val id: String = UUID.randomUUID().toString(),
    val start: LatLng = LatLng(42.460168, 59.607280),
    val end: LatLng = LatLng(42.460168, 59.607280),
    val schedule: String = "09:00 - 21:00",
    val phone: String = "+99 890 714 41 02",
    val taxiName: String = "Taxi 1031",
    val address: List<String> = emptyList()
):Parcelable
