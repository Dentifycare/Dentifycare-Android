package com.dentify.dentifycare.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dentify.dentifycare.data.DentifyRepository

class LoginViewModel(private val dentifyRepository: DentifyRepository): ViewModel() {
    val _loginUser = MutableLiveData<LoginResponse>()
    val login: LiveData<LoginResponse>
        get() = _loginUser

    fun loginUser(email: String, password: String) {
        val temp = dentifyRepository.loginUser(email, password)
        _loginUser.postValue(temp)
    }
}