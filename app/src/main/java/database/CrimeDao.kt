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


//Data Access Object
@Dao
interface CrimeDao {

    //LiveData is a Data Holder class found in the Jetpack lifecycle-extensions library
    //Enables us to pass data between threads and simplifies passing data between different parts of APP
    //SINCE LiveData Room will automatically execute those query operations on a background thread and then publish the results to the livedata object
    @Query("SELECT * FROM crime")
    fun getCrimes(): LiveData<List<Crime>>
    @Query("SELECT * FROM crime WHERE id=(:id)")
    fun getCrime(id: UUID): LiveData<Crime?>
}