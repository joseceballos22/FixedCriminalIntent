package com.bignerdranch.android.criminalintent

import androidx.lifecycle.ViewModel


//ViewModel Used to Hold UI Data
class CrimeListViewModel : ViewModel(){
    //Getting our SingleTon To talk with the Data Base
    private val crimeRepository = CrimeRepository.get()

    //Renaming Crimes property so its more clear what data the property holds
    //Getting the List of Crimes From the Data Base
    val crimeListLiveData = crimeRepository.getCrimes()

    //Addding a Function to wrap a call to the Repos Add crime function
    fun addCrime(crime: Crime)
    {
        crimeRepository.addCrime(crime)
    }



}