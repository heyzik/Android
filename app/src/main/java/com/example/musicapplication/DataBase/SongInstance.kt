package com.example.musicapplication.DataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "songs")
data class SongInstance(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "image") val image: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "song") val song: Int
)

