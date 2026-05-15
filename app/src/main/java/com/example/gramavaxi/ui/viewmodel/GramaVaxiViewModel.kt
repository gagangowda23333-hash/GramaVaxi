package com.example.gramavaxi.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramavaxi.data.model.Animal
import com.example.gramavaxi.data.model.DiseaseReport
import com.example.gramavaxi.data.model.VaccineSchedule
import com.example.gramavaxi.data.repository.GramaVaxiRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GramaVaxiViewModel(private val repository: GramaVaxiRepository = GramaVaxiRepository()) : ViewModel() {

    private val _authState = MutableStateFlow<Boolean?>(null)
    val authState: StateFlow<Boolean?> = _authState.asStateFlow()

    val animals: StateFlow<List<Animal>> = repository.getAnimals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val vaccineSchedules: StateFlow<List<VaccineSchedule>> = repository.getVaccineSchedules()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        checkAuth()
    }

    private fun checkAuth() {
        if (repository.getCurrentUserId() == null) {
            viewModelScope.launch {
                val result = repository.signInAnonymously()
                _authState.value = result.isSuccess
            }
        } else {
            _authState.value = true
        }
    }

    fun addAnimal(name: String, species: String, breed: String, age: Int, gender: String, notes: String, imageUri: Uri?) {
        viewModelScope.launch {
            val animal = Animal(
                name = name,
                species = species,
                breed = breed,
                age = age,
                gender = gender,
                notes = notes
            )
            repository.addAnimal(animal, imageUri)
        }
    }

    fun deleteAnimal(animalId: String) {
        viewModelScope.launch {
            repository.deleteAnimal(animalId)
        }
    }

    fun updateVaccineStatus(scheduleId: String, status: String) {
        viewModelScope.launch {
            repository.updateVaccineStatus(scheduleId, status)
        }
    }

    fun reportDisease(animal: Animal, symptoms: String, severity: String, imageUri: Uri?) {
        viewModelScope.launch {
            val report = DiseaseReport(
                animalId = animal.id,
                animalName = animal.name,
                symptoms = symptoms,
                severity = severity
            )
            repository.reportDisease(report, imageUri)
        }
    }
}
