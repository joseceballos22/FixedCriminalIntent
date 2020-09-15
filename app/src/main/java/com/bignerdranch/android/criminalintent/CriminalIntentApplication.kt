package com.bignerdranch.android.criminalintent

import android.app.Application

class CriminalIntentApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        //Used to Initialize the Repository SingleTon
        CrimeRepository.initialize(this)
    }
}