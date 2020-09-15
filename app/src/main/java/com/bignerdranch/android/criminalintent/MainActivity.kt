package com.bignerdranch.android.criminalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Defining the Fragment Manager Which handles:
        //- A list of Fragements
        //- and Adding the Fragments view to the activity and handling their lifecycle funs
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        //Code Creates and Commits a Fragment Transaction
        if(currentFragment == null)
        {
            val fragment = CrimeListFragment() //Creating a CrimeListFragment
            //Putting that fragment on top of the FragmentManger Stack
            supportFragmentManager.beginTransaction().add(R.id.fragment_container,fragment).commit()
        }

    }
}