package com.example.yournal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "years")
data class YearEntity(
    @PrimaryKey
    val year: Int
)
