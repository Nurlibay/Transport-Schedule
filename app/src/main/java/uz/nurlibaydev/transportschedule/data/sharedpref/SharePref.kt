package uz.nurlibaydev.transportschedule.data.sharedpref

import android.content.Context
import uz.nurlibaydev.transportschedule.utils.BooleanPreference
import uz.nurlibaydev.transportschedule.utils.StringPreference
import javax.inject.Inject

class SharePref @Inject constructor(context: Context){

    private val pref = context.getSharedPreferences("Transport-Schedule", Context.MODE_PRIVATE)

    var firstName: String by StringPreference(pref, "No firstName")

    var isSigned: Boolean by BooleanPreference(pref, false)

    var language: String by StringPreference(pref, "ru")
}