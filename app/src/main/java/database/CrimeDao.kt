package database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.bignerdranch.android.criminalintent.Crime
import java.util.*

/**
 * By returning an instance of LiveData from your DAO class ,
 * you signal Room to run your query on a background thread
 * */
//Data Access Object is an interface that contains functions for each database operation you want to perform

//Data Access Object
@Dao
interface CrimeDao {

    /**
     * Live Data Wraps your query data in a data holder class
     * Its goal is to simplify passing data between different parts of your app
     * Also enables passing of data between Threads
     * When You Configure queries in your Room DAO to return LiveData, Room
     * Will Automatically Execute those queries operations on a BACKGROUND THREAD 
     * */
    //LiveData is a Data Holder class found in the Jetpack lifecycle-extensions library
    //Enables us to pass data between threads and simplifies passing data between different parts of APP
    //SINCE LiveData Room will automatically execute those query operations on a background thread and then publish the results to the livedata object
    @Query("SELECT * FROM crime")
    fun getCrimes(): LiveData<List<Crime>> //Declaring function in interface and telling it what they will return

    //@Query indicates that the function are meatn to pull info out of the database Rather than Inserting , updating or deleting
    @Query("SELECT * FROM crime WHERE id=(:id)")
    fun getCrime(id: UUID): LiveData<Crime?>
}