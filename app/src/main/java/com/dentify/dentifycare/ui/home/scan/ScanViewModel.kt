package com.dentify.dentifycare.ui.home.scan

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dentify.dentifycare.data.remote.response.PredictResponse
import com.dentify.dentifycare.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ScanViewModel(): ViewModel() {
    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> = _imageUri

    private val _predict = MutableLiveData<PredictResponse>()
    val predict = MutableLiveData<PredictResponse>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    fun getPredict(context: Context, file: MultipartBody.Part, onSuccess: (PredictResponse?) -> Unit) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().predict(file)
                _predict.value = response
                _isLoading.value = false
                onSuccess(response)
            } catch (e: Exception) {
                _isLoading.value = false
                Toast.makeText(context, "Not Found", Toast.LENGTH_SHORT).show()
                onSuccess(null)
            }
        }
    }
}