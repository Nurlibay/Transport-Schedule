package uz.nurlibaydev.transportschedule.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.directions.route.AbstractRouting
import com.directions.route.Route
import com.directions.route.RouteException
import com.directions.route.RoutingListener
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.internal.OnConnectionFailedListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import uz.nurlibaydev.transportschedule.data.models.Routing
import uz.nurlibaydev.transportschedule.data.models.TaxiData
import javax.inject.Inject

@HiltViewModel
class MapViewModelImpl @Inject constructor() : MapViewModel, RoutingListener,
    OnConnectionFailedListener, ViewModel() {

    override val messageFlow = MutableSharedFlow<String>()

    override val errorFlow = MutableSharedFlow<String>()

    override val progressFlow = MutableSharedFlow<Boolean>()

    override val routes = MutableSharedFlow<Routing>()


    override fun finRoutes(taxiData: TaxiData) {
        val routing = com.directions.route.Routing.Builder()
            .travelMode(AbstractRouting.TravelMode.DRIVING)
            .withListener(this)
            .alternativeRoutes(true)
            .waypoints(taxiData.start, taxiData.end)
            .key("AIzaSyDg2O5oMElsgmb5ROB_Yo_jkgWdFL5gZso")
            .build()
        routing.execute()
    }

    override fun onRoutingFailure(p0: RouteException?) {
        viewModelScope.launch {
            progressFlow.emit(false)
            errorFlow.emit(p0?.localizedMessage ?: "Unknown error")
        }
    }

    override fun onRoutingStart() {
        viewModelScope.launch {
            progressFlow.emit(true)
        }
    }

    override fun onRoutingSuccess(p0: ArrayList<Route>?, p1: Int) {
        viewModelScope.launch {
            progressFlow.emit(false)
            routes.emit(Routing(p0, p1))
        }
    }

    override fun onRoutingCancelled() {
        viewModelScope.launch {
            progressFlow.emit(false)
            messageFlow.emit("Routing cancelled")
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        viewModelScope.launch {
            progressFlow.emit(false)
            errorFlow.emit(p0.errorMessage!!)
        }
    }
}