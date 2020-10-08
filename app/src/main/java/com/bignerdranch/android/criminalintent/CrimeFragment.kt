package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import java.util.*


/**
 * Using Fragment Arguments to Pass data between hosting Activity and Fragment
 * Fragment Arguments allow you to stash pieces of data in "Argument Bundles" that belong to the fragment
 * MUST BE DONE After Fragment Created And Before Added TO Activity
 * Creating a newInstance() Function so that this function creates the fragment instance
 * And Bundles up and sets its arguments
 * */

//Used to retrieve the date from the DatePickerFragment
private const val REQUEST_CODE = 0

//Used To Log and ensure we are getting the Correct ID from Argument Bundle
private const val TAG = "CrimeFragment"

//Used for Argument Bundle
private const val ARG_CRIME_ID = "crime_id"

//Used to Identify the DatePickerFragment
private const val DIALOG_DATE = "DialogDate"

//Fragment Class Used so that UI Flexible
class CrimeFragment : Fragment() , DatePickerFragment.Callbacks{

    //THIS Crime Property represents the edits the user is currently making
    private lateinit var crime : Crime
    private lateinit var titleField : EditText
    private lateinit var dateButton : Button
    private lateinit var solvedCheckBox : CheckBox

    //Creating a ViewModel to get data from Data base reason a viewModel
    //Is so that it wont change on Rotations Also the view model handles getting and returning the data from database
    private val crimeDetailViewModel: CrimeDetailViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeDetailViewModel::class.java)
    }


    /**
     * When MainActivity needs to Create a CrimeFragment
     * Instead of creating a Default one we call the
     * CrimeFragment().newInstance Static method which will
     * Create the Object and Create a Argument Bundle
     * Pass it to the CrimeFragment Object and return it
     * Note the Bundle has the crimeId Since newInstance Passes
     * the crimeId from the MainActivity to the newly created CrimeFragment
     * */

    companion object
    {
        fun newInstance(crimeId: UUID): CrimeFragment
        {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeFragment().apply {
                arguments = args
            }
        }



    }

    /**
     * Getting the Arguments from the Argument Bundle of this Fragment
     * The argument we want is the Crime ID which was saved in its Bundle
     *
     * */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()

        //Getting the crimeId from the Argument Bundle
        val crimeId: UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID


        //Loading the CrimeID From the Data Base using a view model
        //And the Correct CrimeID For implementation look at CrimeDetailViewModel
        crimeDetailViewModel.loadCrime(crimeId)
    }


    //Method Where We inflate our Fragment View
    override fun onCreateView(inflater : LayoutInflater,
                             container : ViewGroup?,
                             savedInstanceState: Bundle?) : View?{
        val view = inflater.inflate(R.layout.fragment_crime, container, false)

        titleField = view.findViewById(R.id.crime_title) as EditText
        dateButton = view.findViewById(R.id.crime_date) as Button
        solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox


        return view
    }



    /**
     *CrimeFragment "Publishes" the User's Edits when the fragments moves to the stopped state by writing the
     * Updated Data to the DataBase
     * */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Using Observers Dont know what they do

        /**
         * Be Sure to Import androidx.lifecycle.Observer
         * */

        crimeDetailViewModel.crimeLiveData.observe(
            viewLifecycleOwner,
            Observer {
                crime ->
                crime?.let {
                    this.crime = crime
                    updateUI()
                }
            }
        )
    }

    /**
     * Updates the UI Information of the CrimeFragment Using the Details Accquired from
     * The User clicking on a specific Crime
     * */
    private fun updateUI()
    {
        titleField.setText(crime.title)
        dateButton.text = crime.date.toString()

        /**
         * Skipping the Check Box Animation to prevent Lag
         * */
        //solvedCheckBox.isChecked = crime.isSolved

        solvedCheckBox.apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState() //Skips animation
        }
    }

    /**
     * Function used to save the Users Edits after they have edited a crime
     * It will call the crimeDetailViewModel which will then call its save Crime Which will update the data base
     *
     * Fragment.onStop() is called anytime your fragment moves to the stopped state
     * This means the data will get saved when the user finishes the detail screen
     * Data will also be saved when the user switches tasks
     *
     * */
    override fun onStop() {
        super.onStop()
        crimeDetailViewModel.saveCrime(crime)
    }



    /**Method Where you define Listeners */
    override fun onStart() {
        super.onStart()

        //Our EditText Widget Takes a TitleWatcher In its listener so defining a inline anonymous class
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?,
                                           start: Int,
                                           count: Int,
                                           after: Int) {
            }
            //Only Method we care abotu
            override fun onTextChanged(sequence: CharSequence?,
                                       start: Int,
                                       before: Int,
                                       count: Int) {
                crime.title = sequence.toString()
            }
            override fun afterTextChanged(sequence: Editable?) {
            }
        }

        titleField.addTextChangedListener(titleWatcher)

        solvedCheckBox.apply{
            setOnCheckedChangeListener{ _, isChecked ->
                crime.isSolved = isChecked
            }

        }

        /**Creating a DatePicker Fragment (which is a Dialog) wrapped in a Fragment when the User clicks the button  */
        this.dateButton.setOnClickListener{
            //Creating the Object using its newInstance static method and storing the date in the argument bundle
            DatePickerFragment.newInstance(crime.date).apply {

                //Setting the CrimeFragment object created in the MainActivity as the Target of the DatePickerFragment
                //So when it dies it knows
                setTargetFragment(this@CrimeFragment, REQUEST_CODE)

                show(this@CrimeFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }

    }

    override fun onDateSelected(date: Date) {
        crime.date = date //Setting the new date the user specified
        updateUI() //Updating the UI

    }
}