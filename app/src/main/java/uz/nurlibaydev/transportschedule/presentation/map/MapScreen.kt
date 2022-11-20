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

// Created by Jamshid Isoqov an 11/18/2022
@AndroidEntryPoint
class MapScreen : Fragment(R.layout.screen_map) {

    private val viewModel: MapViewModel by viewModels<MapViewModelImpl>()

    private val viewBinding: ScreenMapBinding by viewBinding()

    private val args: MapScreenArgs by navArgs()

    private lateinit var dialog: ProgressDialog

    private lateinit var googleMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

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

            val mapFragment =
                childFragmentManager.findFragmentById(R.id.map) as MapHelper
            mapFragment.getMapAsync(mapFragment)
            mapFragment.onMapReady {
                googleMap = it
                googleMap.uiSettings.apply {
                    isCompassEnabled = false
                }

                viewModel.routes.onEach {
                    val shortestRouteIndex = it.shortestRouteIndex
                    val route = it.route?.get(shortestRouteIndex)

                    val polyline = googleMap.addPolyline {
                        width(8f)
                        color(Color.parseColor("#000000"))
                        addAll(route!!.points)
                    }

                    val k = polyline.points.size
                    val startLatLng = polyline.points[0]
                    val middleLatLng = polyline.points[k / 2]
                    val endLatLng = polyline.points[k - 1]

                    val cameraUpdateMiddle =
                        CameraUpdateFactory.newLatLngZoom(middleLatLng ?: args.taxiData.start, 12f)
                    googleMap.moveCamera(cameraUpdateMiddle)

                    val routes = args.taxiData.address
                    googleMap.addMarker {
                        title("Start?")
                        snippet(routes[0])
                        icon(bitmapFromVector(R.drawable.ic_location_red_14))
                        position(startLatLng!!)
                    }

                    googleMap.addMarker {
                        title("End?")
                        snippet(routes[routes.lastIndex])
                        icon(bitmapFromVector(R.drawable.ic_location_blue_14))
                        position(endLatLng!!)
                    }
                }.launchIn(lifecycleScope)

                viewModel.finRoutes(args.taxiData)
            }
        }
    }
}