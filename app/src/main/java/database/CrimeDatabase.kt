package database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bignerdranch.android.criminalintent.Crime


//OUR ACTUAL DATABASE Class
@Database(entities = [ Crime::class], version=1)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase(){

    //Getting our Data Access object
    abstract fun crimeDao() : CrimeDao
}