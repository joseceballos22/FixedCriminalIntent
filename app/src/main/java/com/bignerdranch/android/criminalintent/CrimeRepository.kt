package com.bignerdranch.android.criminalintent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import database.CrimeDatabase
import java.lang.IllegalStateException
import java.util.*

private const val DATABASE_NAME = "crime-database"


/**A Repository class encapsulates the logic for accessing data from a single source or a set of sources
 * It Determines how to fetch and store a particular set of data
 * Whether locally in a database or from a remove server */

//To access your database you will use the repository pattern
//UI will get all info from singleton because it wont die
//Defines Our SingleTon Used to Communicate with the Data Base (ONLY EVER BE ONE INSTANCE OF IT IN YOUR APP PROCESS)
class CrimeRepository private constructor(context: Context){


    /**A SingleTon Exists as long as the application stays in memory
     * Therefore Storing any properties on the singleton will keep them avaliable throughout any life cycle changes in
     * you activities and fragments*/

    //Defining DataBase Private Variable
    private val database : CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    ).build()


    //Defining the Data Access Object All this is created from the ROOM Compiler
    private val crimeDao = database.crimeDao()

    //Updating methods to return LiveData from its query functions
    fun getCrimes() : LiveData<List<Crime>> = crimeDao.getCrimes()

    fun getCrime(id: UUID): LiveData<Crime?> = crimeDao.getCrime(id)

    //Defining the SingleTON
    companion object {
        private var INSTANCE: CrimeRepository? = null
        //Ensuring we have a Valid SingleTon
        fun initialize(context: Context){
            if(INSTANCE == null){
                INSTANCE = CrimeRepository(context)
            }
        }
        fun get(): CrimeRepository {
            return INSTANCE?:
                    throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}