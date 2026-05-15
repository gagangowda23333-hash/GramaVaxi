package com.example.gramavaxi.data.repository

import android.net.Uri
import com.example.gramavaxi.data.model.Animal
import com.example.gramavaxi.data.model.DiseaseReport
import com.example.gramavaxi.data.model.VaccineSchedule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.*

class GramaVaxiRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    suspend fun signInAnonymously(): Result<Unit> = try {
        auth.signInAnonymously().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun getAnimals(): Flow<List<Animal>> = callbackFlow {
        val userId = getCurrentUserId() ?: return@callbackFlow
        val subscription = firestore.collection("animals")
            .whereEqualTo("ownerId", userId)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val animals = snapshot.toObjects(Animal::class.java)
                    trySend(animals)
                }
            }
        awaitClose { subscription.remove() }
    }

    suspend fun addAnimal(animal: Animal, imageUri: Uri?): Result<Unit> = try {
        val userId = getCurrentUserId() ?: throw Exception("User not logged in")
        var finalImageUrl = ""
        
        if (imageUri != null) {
            val ref = storage.reference.child("animals/${UUID.randomUUID()}.jpg")
            ref.putFile(imageUri).await()
            finalImageUrl = ref.downloadUrl.await().toString()
        }

        val animalRef = firestore.collection("animals").document()
        val animalWithId = animal.copy(id = animalRef.id, ownerId = userId, imageUrl = finalImageUrl)
        animalRef.set(animalWithId).await()
        
        generateVaccineSchedule(animalWithId)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun deleteAnimal(animalId: String): Result<Unit> = try {
        // Delete animal
        firestore.collection("animals").document(animalId).delete().await()
        // Delete associated schedules
        val schedules = firestore.collection("vaccineSchedules")
            .whereEqualTo("animalId", animalId)
            .get().await()
        schedules.forEach { it.reference.delete() }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private suspend fun generateVaccineSchedule(animal: Animal) {
        val vaccines = when (animal.species) {
            "Goat" -> listOf("PPR" to 30, "ET" to 60)
            "Sheep" -> listOf("FMD" to 45, "Blue Tongue" to 90)
            "Cow" -> listOf("Anthrax" to 30, "HS" to 120, "FMD" to 180)
            "Buffalo" -> listOf("HS" to 30, "FMD" to 150)
            else -> emptyList()
        }

        vaccines.forEach { (name, days) ->
            val dueDate = System.currentTimeMillis() + (days * 24 * 60 * 60 * 1000L)
            val schedule = VaccineSchedule(
                animalId = animal.id,
                vaccineName = name,
                dueDate = dueDate,
                status = "Pending"
            )
            firestore.collection("vaccineSchedules").add(schedule).await()
        }
    }

    fun getVaccineSchedules(): Flow<List<VaccineSchedule>> = callbackFlow {
        val subscription = firestore.collection("vaccineSchedules")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    trySend(snapshot.toObjects(VaccineSchedule::class.java))
                }
            }
        awaitClose { subscription.remove() }
    }

    suspend fun updateVaccineStatus(scheduleId: String, status: String): Result<Unit> = try {
        firestore.collection("vaccineSchedules").document(scheduleId)
            .update("status", status, "completionDate", if (status == "Completed") System.currentTimeMillis() else null)
            .await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun reportDisease(report: DiseaseReport, imageUri: Uri?): Result<Unit> = try {
        val userId = getCurrentUserId() ?: throw Exception("User not logged in")
        var finalImageUrl = ""
        
        if (imageUri != null) {
            val ref = storage.reference.child("diseases/${UUID.randomUUID()}.jpg")
            ref.putFile(imageUri).await()
            finalImageUrl = ref.downloadUrl.await().toString()
        }

        val aiSuggestion = when (report.severity) {
            "High" -> "Critical: Isolate animal immediately and call a vet at +91 00000-00000."
            "Medium" -> "Possible Infection. Monitor temperature and provide plenty of water."
            else -> "Ensure proper nutrition and keep the surroundings clean."
        }

        val reportWithData = report.copy(
            ownerId = userId,
            imageUrl = finalImageUrl,
            aiSuggestion = aiSuggestion
        )
        firestore.collection("diseaseReports").add(reportWithData).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
