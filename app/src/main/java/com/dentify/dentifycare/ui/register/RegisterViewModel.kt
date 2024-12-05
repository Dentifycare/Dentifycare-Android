package com.dentify.dentifycare.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dentify.dentifycare.data.DentifyRepository

class RegisterViewModel(private val dentifyRepository: DentifyRepository) : ViewModel() {
    val _usersPatient = MutableLiveData<RegisterPatientResponse>()
    val usersPatient: LiveData<RegisterPatientResponse>
        get() = _usersPatient

    val _usersCoAss = MutableLiveData<RegisterCoAssResponse>()
    val usersCoAss: LiveData<RegisterCoAssResponse>
        get() = _usersCoAss

    fun registerPatient(name: String, email: String, password: String, phone: String) {
        val tempPatient = dentifyRepository.registerPatient(name, email, password, phone)
        _usersPatient.postValue(tempPatient)
    }

    fun registerCoAss(name: String, email: String, password: String, phone: String, university: String, semester: String, studentId: String) {
        val tempCoAss = dentifyRepository.registerCoAss(name, email, password, phone, university, semester, studentId)
        _usersCoAss.postValue(tempCoAss)
    }
}