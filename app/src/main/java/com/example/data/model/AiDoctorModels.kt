package com.example.data.model

data class DoctorProfile(
    val id: String,
    val name: String,
    val title: String,
    val specialization: String,
    val category: String, // "Dokter Hewan" or "Agronom Tanaman"
    val rating: Float,
    val experienceYears: Int,
    val consultFee: Long,
    val strNumber: String,
    val availableSchedule: String,
    val bio: String
)

data class AiConsultationRecord(
    val id: String,
    val category: String, // "Hewan" or "Tanaman"
    val subjectName: String,
    val symptoms: String,
    val diagnosis: String,
    val confidence: Int,
    val urgencyLevel: String, // "Rendah", "Sedang", "Darurat / Hubungi Dokter"
    val recommendations: List<String>,
    val recommendedProducts: List<String>,
    val timestamp: String
)
