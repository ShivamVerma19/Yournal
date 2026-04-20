package com.example.yournal.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

import androidx.room.Index

@Entity(
    tableName = "monthly_reviews",
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
data class MonthlyReview(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val year: Int,
    val month: Int,
    val whatWentWell: String? = null,
    val whatDidntGoWell: String? = null,
    val lessonsLearned: String? = null,
    val overallRating: Int? = null
)
