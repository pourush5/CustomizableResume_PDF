package com.pourush.assignment1prag

data class Resume(
    val name: String,
    val contactInfo: String,
    val skills: List<String>,
    val workExperience: List<String>
)