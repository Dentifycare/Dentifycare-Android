package com.dentify.dentifycare.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("Accuracy")
	val accuracy: String? = null,

	@field:SerializedName("Diagnosis")
	val diagnosis: String? = null
)
