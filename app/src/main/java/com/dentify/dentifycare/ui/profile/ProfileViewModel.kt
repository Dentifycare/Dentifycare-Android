package com.dentify.dentifycare.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _role = MutableLiveData<String>()
    val role: LiveData<String> get() = _role

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        loadUserData()
    }

    private fun loadUserData() {
        _isLoading.postValue(true)
        userId?.let {
            db.collection("users").document(it).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("name")
                        val role = document.getString("role")

                        if (!_name.value.equals(name)) {
                            _name.postValue(name ?: "")
                        }
                        if (!_role.value.equals(role)) {
                            _role.postValue(role ?: "")
                        }
                    }
                    _isLoading.postValue(false)
                }
                .addOnFailureListener {
                    _isLoading.postValue(false)
                }
        }
    }
}
