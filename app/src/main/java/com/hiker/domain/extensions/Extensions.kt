package com.hiker.domain.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.hiker.R
import com.hiker.domain.exceptions.InvalidDateException
import java.util.*
import java.util.regex.Pattern


fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun GoogleMap.setUpMarker(latitude: Double, longitude: Double, title: String, context: Context) : Marker{
    val marker = this.addMarker(
        MarkerOptions()
        .position(LatLng(latitude, longitude))
        .title(title)
        .icon(bitmapDescriptorFromVector(context, R.drawable.ic_marker_pin_0_trips)))
    return marker
}

fun Marker.setIcon(context: Context, @DrawableRes iconLayout: Int){
    this.setIcon(bitmapDescriptorFromVector(context, iconLayout))

}

fun Marker.setNormalStyle(context: Context){
    this.setIcon(bitmapDescriptorFromVector(context, R.drawable.ic_marker_pin_0_trips))
}


fun Marker.setFilteredStyle(context: Context){
    this.setIcon(bitmapDescriptorFromVectorSize(context, R.drawable.ic_marker_pin_0_trips_red, 90, 90))
}

private fun bitmapDescriptorFromVectorSize(context: Context, @DrawableRes vectorDrawableResourceId: Int, width: Int, height: Int): BitmapDescriptor {
    val background = ContextCompat.getDrawable(context, vectorDrawableResourceId)
    background!!.setBounds(0, 0, width, height)
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    background.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

fun getPluralText(itemsCount: Int, text1: String, text2: String, text3: String) : String {
    if ( itemsCount == 1 ) {
        return text1
    }
    if ( itemsCount % 10 > 1 && itemsCount % 10 < 5 && ! ( itemsCount % 100 >= 10 && itemsCount % 100 <= 21 ) ) {
        return text2;
    }

    return text3;
}

fun getUserId(activity: Activity) : String?{
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    return sharedPref.getString(activity.getString(R.string.preferences_userSystemId), null)
}

fun getJwtToken(activity: Activity) : String?{
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    return sharedPref.getString(activity.getString(R.string.preferences_token), null)
}

private fun bitmapDescriptorFromVector(context: Context, @DrawableRes vectorDrawableResourceId: Int): BitmapDescriptor {
    val background = ContextCompat.getDrawable(context, vectorDrawableResourceId)
    background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)
    val bitmap = Bitmap.createBitmap(background.intrinsicWidth, background.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    background.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

fun isNullOrEmpty(str: String?): Boolean {
    if (str != null && !str.trim().isEmpty())
        return false
    return true
}

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }

fun Fragment.hideKeyboard() {
    view?.let {
        activity?.hideKeyboard(it)
    }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
fun String.Companion.empty(): String {
    return ""
}

fun Date.getWeekDayName() : String {
    val calendar = Calendar.getInstance();
    calendar.time = this
    val day = calendar.get(Calendar.DAY_OF_WEEK);

    when (day) {
        Calendar.MONDAY -> return "Poniedziałek"
        Calendar.TUESDAY -> return "Wtorek"
        Calendar.WEDNESDAY -> return "Środa"
        Calendar.THURSDAY -> return "Czwartek"
        Calendar.FRIDAY -> return "Piątek"
        Calendar.SATURDAY -> return "Sobota"
        Calendar.SUNDAY -> return "Niedziela"
        else -> throw InvalidDateException("Niepoprawna data")
    }
}


fun String.isPhoneNumber() : Boolean {
    if(!Pattern.matches("[a-zA-Z]+", this)) {
        return this.length in 7..13
    }
    return false;
}