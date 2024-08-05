package com.example.musicapplication.DataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    fun insertSong(song: SongInstance)

    @Delete
    fun deleteSong(song: SongInstance)

    @Query("SELECT * FROM songs")
    fun getAllSong(): Flow<List<SongInstance>>

    @Query("SELECT * FROM songs WHERE id = :id")
    fun getSongById(id: Int): Flow<SongInstance>

    @Query("SELECT * FROM songs WHERE id > :id LIMIT 1")
    fun getNextSong(id: Int): Flow<SongInstance>

    @Query("SELECT * FROM songs WHERE id < :id ORDER BY id DESC LIMIT 1")
    fun getPreviousSong(id: Int): Flow<SongInstance>

    @Query("DELETE FROM songs")
    fun deleteAllSong()
}