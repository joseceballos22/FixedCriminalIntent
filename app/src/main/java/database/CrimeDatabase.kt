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
    //Registering Data Access Object class with database class
    /**
     * Now when the database is created, Room will generate
     * a Concrete implemenation of the DAO that you can access once you have
     * a referencce to the DAO , You can call any of the functions defined on it to interact with your data base
     * */
    abstract fun crimeDao() : CrimeDao
}