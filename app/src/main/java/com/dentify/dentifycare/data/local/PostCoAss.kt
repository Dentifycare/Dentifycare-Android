package com.dentify.dentifycare.data.local

data class PostCoAss(
    val postId: String = "",
    val email: String = "",
    var uid: String = "",
    val status: String = "",
    val name: String = "",
    val phone: String = "",
    val hospital: String = "",
    val city: String = "",
    val province: String = "",
    val quota: String = "",
    val additionalInfo: String = "",
    val currentDate: String = "",
    val selectedSkills: List<String> = emptyList(),
    val selectedHours: List<String> = emptyList()
)