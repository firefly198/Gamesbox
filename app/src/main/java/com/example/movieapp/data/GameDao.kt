package com.example.movieapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GameDao {
    @Query("SELECT * FROM games")
    suspend fun getAllGames(): List<GameEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<GameEntity>)

    @Query("UPDATE games SET isFavorite = :isFav WHERE id = :gameId")
    suspend fun updateFavorite(gameId: Int, isFav: Boolean)

    @Query("SELECT * FROM games WHERE isFavorite = 1")
    suspend fun getFavoriteGames(): List<GameEntity>

    @Query("SELECT * FROM games WHERE id = :id")
    suspend fun getGameById(id: Int): GameEntity

    @Query("UPDATE games SET rating = :rating WHERE id = :gameId")
    suspend fun updateRating(gameId: Int, rating: Int)

}