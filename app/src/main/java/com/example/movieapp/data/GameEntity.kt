package com.example.movieapp.data


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val fullDescription: String,
    val link: String,
    val imageRes: Int,
    val imageBig: Int,
    val isFavorite: Boolean = false,
    val rating: Int = 0
)
