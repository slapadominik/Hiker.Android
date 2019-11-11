package com.hiker.domain.entities

import com.hiker.data.remote.dto.MountainTrailBrief

data class Mountain(
    val id: Int,
    val name: String,
    val metersAboveSeaLevel: Int,
    val upcomingTripsCount: Int?,
    val location : Location?,
    val trails: MountainTrailBrief?
){
    override fun toString(): String {
        return "${name}, ${metersAboveSeaLevel} m n.p.m."
    }
}