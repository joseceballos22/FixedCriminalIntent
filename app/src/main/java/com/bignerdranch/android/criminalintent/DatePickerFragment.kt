package com.bignerdranch.android.criminalintent

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*



/**All A Dialog is it demands attention and input from the user
 * They are useful for presenting a choice or important information
 * */

//Used to know the fragment argument id in the argument bundle when being passed
private const val ARG_DATE = "date"

/**Wrapping our Dialog in a Fragment so on a Rotation it doesnt disappear */

class DatePickerFragment : DialogFragment()
{

    /**Interface Used to send information to the CrimeFragment when this Fragment is Deleted*/
    interface Callbacks
    {
        fun onDateSelected(date: Date)
    }


    /**The Fragment manger of the hosting activty calls this function as part of putting the Ddialog Fragment OnScreen*/
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        //This Listener is a parameter in the DatePickerDialog
        /**Listener used to send the date to the CrimeFragment when this fragment is Dead*/
        val dateListener = DatePickerDialog.OnDateSetListener {
                _: DatePicker, year: Int, month: Int, day: Int ->
                val resultDate: Date = GregorianCalendar(year,month,day).time

                targetFragment?.let { fragment -> (fragment as Callbacks).onDateSelected(resultDate) }
        }


        //Retrieving the date from the argument bundle so that we appriately create the dialog
        val date = arguments?.getSerializable(ARG_DATE) as Date

        //Stuff used to initialize the DatePickerDialog
        val calendar = Calendar.getInstance()

        calendar.time = date //Ensuring the Calendar gets the correct date

        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        //Returning the DatePicker Dialog
        return DatePickerDialog(requireContext(), dateListener, initialYear,initialMonth,initialDay)
    }

    companion object
    {
        fun newInstance(date:Date) : DatePickerFragment
        {
            //Storing the Date in the Argument Bundle of this Fragment when created
            val args = Bundle().apply {
                putSerializable(ARG_DATE,date)
            }

            //Returning that object with its bundle
            return DatePickerFragment().apply {
                arguments = args
            }

        }


    }


}
