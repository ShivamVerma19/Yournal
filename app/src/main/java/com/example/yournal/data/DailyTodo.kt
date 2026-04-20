package com.example.yournal.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

import androidx.room.Index

@Entity(
    tableName = "daily_todos",
    foreignKeys = [
        ForeignKey(
            entity = DailyEntry::class,
            parentColumns = ["id"],
            childColumns = ["dailyEntryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["dailyEntryId"])]
)
data class DailyTodo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dailyEntryId: Int,
    val todo: String,
    val isCompleted: Boolean = false
)
