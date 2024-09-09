package com.example.musicapplication.DataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert(entity = SongInstance::class)
    fun insertSongInHome(song: SongInstance)
    @Delete(entity = SongInstance::class)
    fun deleteSongFromHome(song: SongInstance)
    @Query("DELETE FROM songs")
    fun deleteAllSongFromHome()
    @Query("SELECT * FROM songs")
    fun getAllSong(): Flow<List<SongInstance>>

    @Insert(entity = AddSongInstance::class)
    fun insertSongInBrowse(song: SongInstance)
    @Delete(entity = AddSongInstance::class)
    fun deleteSongFromBrowse(song: SongInstance)
    @Query("DELETE FROM addSongs")
    fun deleteAllSongFromBrowse()
    @Query("SELECT * FROM addSongs")
    fun getAllSongInBrowse(): Flow<List<SongInstance>>

    @Query("SELECT * FROM songs WHERE id = :id")
    fun getSongByIdFromHome(id: Int): Flow<SongInstance>
    @Query("SELECT * FROM songs WHERE id > :id LIMIT 1")
    fun getNextSongFromHome(id: Int): Flow<SongInstance>
    @Query("SELECT * FROM songs WHERE id < :id ORDER BY id DESC LIMIT 1")
    fun getPreviousSongFromHome(id: Int): Flow<SongInstance>


    @Query("SELECT * FROM addSongs WHERE id = :id")
    fun getSongByIdFromBrowse(id: Int): Flow<SongInstance>
    @Query("SELECT * FROM addSongs WHERE id > :id LIMIT 1")
    fun getNextSongFromBrowse(id: Int): Flow<SongInstance>
    @Query("SELECT * FROM addSongs WHERE id < :id ORDER BY id DESC LIMIT 1")
    fun getPreviousSongFromBrowse(id: Int): Flow<SongInstance>
}