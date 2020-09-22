package com.bignerdranch.android.criminalintent

import android.app.Application



//Used to Initialize the Crime Repository
//Because Application Subclass allows you to access life cycles information
//About the Application itself
//Good Place todo any one time initialization Opeartions
class CriminalIntentApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        //SINGLETON
        CrimeRepository.initialize(this)
    }
}