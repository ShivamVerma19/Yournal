package com.example.yournal.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

import androidx.room.Index

@Entity(
    tableName = "monthly_goals",
    foreignKeys = [
        ForeignKey(
            entity = YearEntity::class,
            parentColumns = ["year"],
            childColumns = ["year"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["year"])]
)
data class MonthlyGoal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val year: Int,
    val month: Int,
    val goal: String,
    val isCompleted: Boolean = false
)
