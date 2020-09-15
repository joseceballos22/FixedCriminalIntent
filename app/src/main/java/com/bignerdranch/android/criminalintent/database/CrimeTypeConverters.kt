package com.bignerdranch.android.criminalintent.database

import androidx.room.TypeConverter
import java.util.*

/**
 * This class will define methods to tell Room on how to convert back and fourth between Data and UUID
 * Useful so we can store these in our CrimeDataBase
 * */
class CrimeTypeConverters {

    //For Date
    @TypeConverter
    fun fromDate(date: Date?): Long?
    {
        return date?.time
    }
    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date?
    {
        return millisSinceEpoch?.let{
            Date(it)
        }
    }
    //For UUID
    @TypeConverter
    fun toUUID(uuid: String?): UUID?
    {
        return UUID.fromString(uuid)
    }
    @TypeConverter
    fun fromUUID(uuid: UUID?): String?
    {
        return uuid?.toString()
    }



}









