package com.hiker.domain.repository

import com.hiker.domain.entities.Location

interface TouristAttractionRepository {
    fun getByLocation(location: Location)
}