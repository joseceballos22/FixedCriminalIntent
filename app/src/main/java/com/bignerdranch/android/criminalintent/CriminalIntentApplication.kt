package com.bignerdranch.android.criminalintent

import android.app.Application



//Used to Initialize the Crime Repository
class CriminalIntentApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        //SINGLETON
        CrimeRepository.initialize(this)
    }
}