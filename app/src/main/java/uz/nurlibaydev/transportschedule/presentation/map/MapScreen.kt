package uz.nurlibaydev.transportschedule.presentation.map

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.addPolyline
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.nurlibaydev.transportschedule.R
import uz.nurlibaydev.transportschedule.databinding.ListItemRoutesBinding
import uz.nurlibaydev.transportschedule.databinding.ScreenMapBinding
import uz.nurlibaydev.transportschedule.presentation.dialogs.ProgressDialog
import uz.nurlibaydev.transportschedule.utils.extenions.bitmapFromVector
import uz.nurlibaydev.transportschedule.utils.extenions.showError
import uz.nurlibaydev.transportschedule.utils.extenions.showMessage
import uz.nurlibaydev.transportschedule.utils.extenions.showToast

// Created by Jamshid Isoqov an 11/18/2022

@AndroidEntryPoint
class MapScreen : Fragment(R.layout.screen_map) {

    private val viewModel: MapViewModel by viewModels<MapViewModelImpl>()

    private val viewBinding: ScreenMapBinding by viewBinding()

    private val args: MapScreenArgs by navArgs()
    private var line: Polyline? = null

    private lateinit var dialog: ProgressDialog

    private lateinit var googleMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.finRoutes(args.taxiData)

        dialog = ProgressDialog(ctx = requireContext(), "Progress")

        viewModel.errorFlow.onEach {
            showError(it)
        }.launchIn(lifecycleScope)

        viewModel.messageFlow.onEach {
            showMessage(it)
        }.launchIn(lifecycleScope)

        viewModel.progressFlow.onEach {
            if (it) {
                dialog.show()
            } else {
                dialog.cancel()
            }
        }.launchIn(lifecycleScope)

        viewBinding.apply {

            val data = args.taxiData
            tvTaxiName.text = data.taxiName
            tvPhone.text = data.phone
            tvDuration.text = data.schedule

            for (i in data.address) {
                val binding = ListItemRoutesBinding.bind(
                    layoutInflater.inflate(
                        R.layout.list_item_routes,
                        null,
                        false
                    )
                )
                binding.root.text = i
                viewBinding.containerDetails.addView(binding.root)
            }

            val routes = args.taxiData.address

            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as MapHelper
            mapFragment.getMapAsync(mapFragment)
            mapFragment.onMapReady {
                googleMap = it
                googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

                googleMap.uiSettings.apply {
                    isCompassEnabled = false
                }

                googleMap.addMarker(MarkerOptions().position(LatLng(args.taxiData.startLan, args.taxiData.startLng))
                    .title(routes[0])
                    .icon(bitmapFromVector(R.drawable.ic_location_red_14)))
                googleMap.addMarker(MarkerOptions().position(LatLng(args.taxiData.endLan, args.taxiData.endLng))
                    .title(routes[routes.lastIndex])
                    .icon(bitmapFromVector(R.drawable.ic_location_blue_14)))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(args.taxiData.startLan, args.taxiData.startLng), 18F))

                viewModel.routes.onEach { routing ->

                    val listPoints: List<LatLng> = routing.route!![0].points
                    val options = PolylineOptions().width(5f).color(Color.BLUE).geodesic(true)
                    val iterator = listPoints.iterator()
                    while (iterator.hasNext()) {
                        val data = iterator.next()
                        options.add(data)
                    }
                    line?.remove()
                    line = googleMap.addPolyline(options)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(routing.route[0].latLgnBounds.center))
                    val builder = LatLngBounds.Builder()
                    builder.include(LatLng(args.taxiData.startLan, args.taxiData.startLng))
                    builder.include(LatLng(args.taxiData.endLan, args.taxiData.endLng))
                    val bounds = builder.build()
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50))
                }.launchIn(lifecycleScope)
            }
        }
    }
}