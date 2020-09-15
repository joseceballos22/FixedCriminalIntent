package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment

class CrimeFragment : Fragment() {

    //Our First Crime
    private lateinit var crime: Crime

    //Used to get a reference to the EditText Widget
    private lateinit var titleField: EditText
    private lateinit var dataButton: Button
    private lateinit var solvedCheckBox: CheckBox


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Creating a Crime Object
        this.crime = Crime()
    }

    //Function that inflates layout and returns inflated view to hosting Activity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Explicitly Inflating the fragments view by calling LayoutInflater.inflate(
        val view = inflater.inflate(R.layout.fragment_crime, container, false)

        //getting widget references
        this.titleField = view.findViewById(R.id.crime_title) as EditText
        this.dataButton = view.findViewById(R.id.crime_date) as Button
        this.solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox

        //Setting the date button to the current date then disabling it
        //using the apply scope function which removes the need for using this.dataButton on every attribute
        //As well as returns a reference to this button
        this.dataButton.apply {
            text = crime.date.toString()
            isEnabled = false
        }

        return view
    }

    override fun onStart() {
        super.onStart()

        //Creating an Object whos class implements TextWatcher Interface using anonymous inline class
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            //Once the text is changed we call the toString method to change the crime title to the seq of char the user inputted
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                crime.title = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
                TODO("Not yet implemented")
            }
        }

        //TextChangedListener taken in an object whos class implements the TextWatcher Interface
        this.titleField.addTextChangedListener(titleWatcher)

        //Setting Listeners of Checkbox
        this.solvedCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->  crime.isSolved = isChecked}
        }
    }
}