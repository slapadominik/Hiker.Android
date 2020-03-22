package com.hiker.domain.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.hiker.R


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
