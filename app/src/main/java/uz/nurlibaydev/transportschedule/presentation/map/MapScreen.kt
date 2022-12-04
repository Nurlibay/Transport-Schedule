package uz.nurlibaydev.transportschedule.presentation.map

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.addPolyline
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.nurlibaydev.transportschedule.R
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

            stepView
                .setStepsViewIndicatorComplectingPosition(data.address.size)
                .setStepViewTexts(data.address)
                .reverseDraw(false)
                .setStepsViewIndicatorCompletedLineColor(Color.parseColor("#0C79FE"))
                .setStepsViewIndicatorCompleteIcon(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_check
                    )
                )
                .setStepsViewIndicatorDefaultIcon(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_location_blue_14
                    )
                ).setStepViewComplectedTextColor(Color.parseColor("#0C79FE"))

            val mapFragment =
                childFragmentManager.findFragmentById(R.id.map) as MapHelper
            mapFragment.getMapAsync(mapFragment)
            mapFragment.onMapReady {
                googleMap = it
                googleMap.uiSettings.apply {
                    isCompassEnabled = false
                }

                googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            args.taxiData.startLan,
                            args.taxiData.startLng
                        ), 16f
                    )
                )

                googleMap.addMarker {
                    title("Start?")
                    snippet("Data")
                    icon(bitmapFromVector(R.drawable.ic_location_red_14))
                    position(LatLng(args.taxiData.startLan, args.taxiData.startLng))
                }

                googleMap.addMarker {
                    title("End?")
                    snippet("Data")
                    icon(bitmapFromVector(R.drawable.ic_location_blue_14))
                    position(LatLng(args.taxiData.endLan, args.taxiData.endLng))
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
                    val middleLatLng = polyline.points[k / 2]

                    val cameraUpdateMiddle =
                        CameraUpdateFactory.newLatLngZoom(
                            middleLatLng ?: LatLng(args.taxiData.startLan, args.taxiData.startLng),
                            12f
                        )
                    googleMap.moveCamera(cameraUpdateMiddle)

                }.launchIn(lifecycleScope)

                viewModel.finRoutes(args.taxiData)
            }
        }
    }
}