package com.bignerdranch.android.criminalintent

import android.content.Context
import androidx.room.Room
import com.bignerdranch.android.criminalintent.database.CrimeDatabase
import java.lang.IllegalStateException
import java.util.*


private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor (context: Context){

    /**
     * Room.databaseBuilder() creates a concrete implementation
     * of your abstract CrimeDatabase using three parameters
     * - Needs a Context object since the database is accessing the file system
     * - Needs the database class that you want Room to Create
     * - Needs the name of the database file you want Room to create for you
     * */
    private val database: CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    ).build()

    //FOR DAO OBJECT REFERENCE IN SINGLETON
    private val crimeDao = database.crimeDao()

    /**
     * Adding functions to repository (SINGLETON)
     * so that other components can perform operations
     * on the database
     * */
    fun getCrimes(): List<Crime> = this.crimeDao.getCrimes()
    fun getCrime(id: UUID): Crime? = this.crimeDao.getCrime(id)

    //CREATING A SINGLETON using the companion object
    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context)
        {
            //If the SINGLETON is not initialized
            //Create it
            if(INSTANCE == null)
            {
                INSTANCE = CrimeRepository(context)
            }
        }
        //Returns an Instance of the SINGLETON
        fun get(): CrimeRepository
        {
            return INSTANCE ?: throw IllegalStateException("Crime Repository Must Be Initialized")
        }
    }
}