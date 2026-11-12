package com.example.data.model

enum class ProgramType {
    PERTANIAN,
    PETERNAKAN
}

data class BudgetItem(
    val title: String,
    val amount: Long,
    val description: String
)

data class PantauPanenProgram(
    val id: String,
    val title: String,
    val type: ProgramType,
    val commodity: String,
    val location: String,
    val managerName: String,
    val managerRole: String,
    val capacityTotal: Int,
    val capacityBooked: Int,
    val feePerUnit: Long,
    val unitLabel: String, // "Lot (0.5 Ha)", "Ekor Sapi", "Kandang (50 Ekor)"
    val durationMonths: Int,
    val harvestEstimate: String,
    val description: String,
    val budgetBreakdown: List<BudgetItem>,
    val risks: List<String>,
    val termsAndConditions: String,
    val expectedYield: String
) {
    val progressPercent: Float
        get() = (capacityBooked.toFloat() / capacityTotal.coerceAtLeast(1)).coerceIn(0f, 1f)
    val availableSlots: Int
        get() = (capacityTotal - capacityBooked).coerceAtLeast(0)
}

data class TitipOrder(
    val id: String,
    val programId: String,
    val programTitle: String,
    val slots: Int,
    val totalFee: Long,
    val investorName: String,
    val investorPhone: String,
    val status: String = "Aktif Berjalan",
    val paymentMethod: String,
    val vaNumber: String,
    val joinedDate: String,
    val type: ProgramType
)

data class CropLog(
    val date: String,
    val stage: String,
    val activity: String,
    val notes: String,
    val moisturePercent: Int = 78,
    val temperatureCelsius: Int = 26
)

data class AnimalIndividualRecord(
    val id: String,
    val tagId: String,
    val breed: String,
    val ageMonths: Int,
    val weightKg: Float,
    val initialWeightKg: Float,
    val healthStatus: String, // "Sangat Sehat", "Dalam Observasi", "Sehat"
    val temperature: Float = 38.6f,
    val farmerName: String,
    val penLocation: String,
    val feedLogs: List<String>,
    val vaccineLogs: List<String>,
    val checkupSchedule: String
)
