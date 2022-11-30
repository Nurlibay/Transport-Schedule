package uz.nurlibaydev.transportschedule.utils.extenions

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment
import uz.nurlibaydev.transportschedule.presentation.dialogs.ErrorDialog
import uz.nurlibaydev.transportschedule.presentation.dialogs.MessageDialog

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    if (context != null) {
        Toast.makeText(context, message, duration).show()
    }
}

fun Activity.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Fragment.showError(message: String) {
    val dialog = ErrorDialog(requireContext(), message)
    dialog.show()
}

fun Fragment.showMessage(message: String) {
    val dialog = MessageDialog(requireContext(), message)
    dialog.show()

}