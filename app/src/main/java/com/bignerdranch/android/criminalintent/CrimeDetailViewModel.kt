package com.bignerdranch.android.criminalintent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

/**
 * View Model Used to get and retrieve the crime from the data base
 * Using LiveData
 * Reason Using a ViewModel is SO that the Data will not be lost on rotations
 * */
class CrimeDetailViewModel() : ViewModel() {

    //Need this to access the Data Base Since this is a SingleTon
    private val crimeRepository = CrimeRepository.get()

    //This will store the Live Data From the Data Base
    //Stores the ID of the Crime currently displayed (Or about to be displayed)
    private val crimeIdLiveData = MutableLiveData<UUID>()


    //Publicly EXPOSED YOU SHOULD ENSURE IT IS NOT EXPOSED AS A MUTABLE LIVE DATA
    //IN GENERAL VIEW MODELS SHOULD NEVER EXPOSE MUTABLELIVEDATA MAKE PRIVATE

    /**
     * A Live data Transformation is A way to set up a trigger-response relationship
     * between two LiveData objects
     * A Transformation Function : Takes two inpuits a LiveData Object used as a trigger
     * And a Mapping Function that must return a LiveData object
     * */

    //What Im thinking it does is when ever LiveData.Value changes this will
    //Trigger a responds to get the Crime from the DataBase and save its value
    //In this new Live Data Object crimeLiveData.value

    /**
     * The Transformation Result's Value is Calculated by executing the
     * Mapping Function
     * The value property on the LiveData returned from the mapping function is
     * used to set the value property on the LiveData Transformation result
     * */

    /**
     * Reason Using transformation this way so that CrimeFragment only has to observe the exposed
     * CrimeDetailViewModel.crimeLiveData one time
     * */

    var crimeLiveData: LiveData<Crime?> =
        Transformations.switchMap(crimeIdLiveData)
        {
            crimeId -> crimeRepository.getCrime(crimeId)
        }


    //This function will help us load the UUID to the LiveData so that can use it
    //To get the Crime() object from the Data Base
    //Lets this.crimeIdLiveData know the UUID of the Crime we want from DataBase
    fun loadCrime(crimeId: UUID)
    {
        crimeIdLiveData.value = crimeId
    }


    /**Adding a Function to save a crime object to the database after the user has modified it
     * Accepts a Crime and Writes it to the DataBase using SINGLETON CrimeRepository Update method
     *
     * Since CrimeRepository handles running the update request on a background thread
     *
     * */
    fun saveCrime(crime: Crime)
    {
        crimeRepository.updateCrime(crime)
    }








}