package com.example.gramavaxi.data.model

import com.google.firebase.firestore.DocumentId

data class VaccineSchedule(
    @DocumentId val id: String = "",
    val animalId: String = "",
    val vaccineName: String = "",
    val dueDate: Long = 0L,
    val status: String = "Pending", // Pending, Completed, Overdue
    val completionDate: Long? = null,
    val reminderSent: Boolean = false
)
