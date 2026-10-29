package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit_targets")
data class HabitTargetEntity(
    @PrimaryKey
    val id: Int = 1,
    // Target counts for daily practices
    val targetKuldeviNaamjap: Int = 1000,
    val targetGurudevDattaNaamjap: Int = 1000,
    val targetKapurItrUpay: Int = 2,
    val targetPrarthana: Int = 5,
    val targetKrutajnata: Int = 5,
    val targetAavaranKadhne: Int = 2,
    val targetBhavjagruti: Int = 3,
    val targetAatmanivedan: Int = 2,
    val targetSwayamsuchana: Int = 3,
    val targetGranthPathanPages: Int = 5
)
