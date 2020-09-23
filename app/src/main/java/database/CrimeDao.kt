package database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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

    //Function to Update an Existing Crime After the User Edits it Room will define the code for it
    //Takes in a  crime Object, uses the ID stored in that crime to find the associated row, and
    //Then updates the data in that row based on the new data in the crime object
    @Update
    fun updateCrime(crime: Crime)

    //Function that Will Insert a New Crime to Database Room will define the code for it
    //Adds a crime to the database Table NEEDS @Insert Annotation
    @Insert
    fun addCrime(crime: Crime)

}