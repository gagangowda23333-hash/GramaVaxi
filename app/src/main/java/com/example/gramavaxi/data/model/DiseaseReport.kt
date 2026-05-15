package com.example.gramavaxi.data.model

import com.google.firebase.firestore.DocumentId

data class DiseaseReport(
    @DocumentId val id: String = "",
    val animalId: String = "",
    val animalName: String = "",
    val symptoms: String = "",
    val severity: String = "Low", // Low, Medium, High
    val imageUrl: String? = null,
    val reportDate: Long = System.currentTimeMillis(),
    val aiSuggestion: String = "",
    val ownerId: String = ""
)
