package com.bignerdranch.android.criminalintent

import androidx.lifecycle.ViewModel


//ViewModel Used to Hold UI Data
class CrimeListViewModel : ViewModel(){
    //Getting our SingleTon To talk with the Data Base
    private val crimeRepository = CrimeRepository.get()

    //Getting the List of Crimes From the Data Base
    val crimeListLiveData = crimeRepository.getCrimes()
}