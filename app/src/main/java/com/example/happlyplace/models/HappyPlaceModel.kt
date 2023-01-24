package com.example.happlyplace.models

data class HappyPlaceModel(
    val id: Int,
    val title: String,
    val image: String,
    val description: String,
    val date: String,
    val location: String,
    val latitude: Double,
    val longitude: Double
) : java.io.Serializable // using Serializable to add to intent cause it doesnt allow object