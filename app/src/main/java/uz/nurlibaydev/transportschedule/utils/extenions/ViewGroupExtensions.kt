package uz.nurlibaydev.transportschedule.utils.extenions

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

fun Dialog.config(viewBinding: ViewBinding) {
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    setContentView(viewBinding.root)
}

fun Fragment.bitmapFromVector(vectorResId: Int): BitmapDescriptor {
    val vectorDrawable = ContextCompat.getDrawable(this.requireContext(), vectorResId)

    vectorDrawable!!.setBounds(
        0,
        0,
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight
    )

    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}