package uz.nurlibaydev.transportschedule.data.models

import com.directions.route.Route


// Created by Jamshid Isoqov an 11/20/2022
data class Routing(
    val route: ArrayList<Route>?,
    val shortestRouteIndex: Int,
)
