package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class CropScanEntity(
    val id: String = "",
    val userId: String = "",
    val cropType: String = "Rice",
    val diseaseName: String = "",
    val diseaseNameUrdu: String = "",
    val confidence: Double = 0.0,
    val severity: String = "",
    val notes: String? = null,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)
