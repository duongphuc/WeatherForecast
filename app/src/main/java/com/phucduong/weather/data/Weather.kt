package com.phucduong.weather.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "weather")
data class Weather constructor(
    @ColumnInfo var city: String,
    @ColumnInfo var datetime: String,
    @ColumnInfo var humidity: Int,
    @ColumnInfo var pressure: Int,
    @ColumnInfo var averageTemp: Int,
    @ColumnInfo var description: String,
    @ColumnInfo var keyword: String,
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()
) {

}