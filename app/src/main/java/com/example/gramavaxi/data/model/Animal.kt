package com.example.gramavaxi.data.model

import com.google.firebase.firestore.DocumentId

data class Animal(
    @DocumentId val id: String = "",
    val name: String = "",
    val species: String = "", // Goat, Sheep, Cow, Buffalo
    val breed: String = "",
    val age: Int = 0,
    val gender: String = "",
    val imageUrl: String = "",
    val ownerId: String = "",
    val registrationDate: Long = System.currentTimeMillis(),
    val vaccinationStatus: String = "Pending", // Vaccinated, Pending, Overdue
    val notes: String = ""
)
