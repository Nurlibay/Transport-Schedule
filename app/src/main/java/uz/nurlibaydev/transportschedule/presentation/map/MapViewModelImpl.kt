package uz.nurlibaydev.transportschedule.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.directions.route.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.internal.OnConnectionFailedListener
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import uz.nurlibaydev.transportschedule.R
import uz.nurlibaydev.transportschedule.app.App
import uz.nurlibaydev.transportschedule.data.models.RouteData
import uz.nurlibaydev.transportschedule.data.models.TaxiData
import javax.inject.Inject

@HiltViewModel
class MapViewModelImpl @Inject constructor() : MapViewModel, RoutingListener,
    OnConnectionFailedListener, ViewModel() {

    override val messageFlow = MutableSharedFlow<String>()

    override val errorFlow = MutableSharedFlow<String>()

    override val progressFlow = MutableSharedFlow<Boolean>()

    override val routes = MutableSharedFlow<RouteData>()

    override fun finRoutes(taxiData: TaxiData) {
        val routeData = Routing.Builder()
            .travelMode(AbstractRouting.TravelMode.DRIVING)
            .withListener(this)
            .alternativeRoutes(true)
            .waypoints(LatLng(taxiData.startLan, taxiData.startLng), LatLng(taxiData.endLan, taxiData.endLng))
            .key(App.instance.getString(R.string.map_key))
            .build()
        routeData.execute()
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
            routes.emit(RouteData(p0, p1))
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