package com.bignerdranch.android.criminalintent

import androidx.lifecycle.ViewModel

//Remember using a ViewModel Because this isnt destroyed until the activity is destroyed regardless of rotation
class CrimeListViewModel : ViewModel() {

    private val crimeRepository = CrimeRepository.get()
    val crimes = crimeRepository.getCrimes()

}