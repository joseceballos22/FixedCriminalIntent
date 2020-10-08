package com.bignerdranch.android.criminalintent

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.format.DateFormat
import androidx.lifecycle.Observer
import java.util.*

/**
 * Side Notes:
 *  - A Fragment's view lifecycle is owned and tracked separately by FragmentViewLifecycleOwner
 *  Each Fragment has an instance of FragmentViewLifecycleOwner that keeeps track of the lifecycle of that fragment's view 
 * */


private const val TAG = "CrimeListFragment"

/**
 * With the Callbacks Interface CrimeListFragment now has a way to call functions on its hosting activity
 * Regardless of which activity is doing the hosting
 * As long as the activity implements CrimeListFragment.Callbacks
 * */


//Fragment for Our list Eventually Will link Both Fragments UP
class CrimeListFragment : Fragment() {

    /**
     * Doing it this way so that Fragments Remain Independent And MainActivity Handles The Switching Between Fragments
     * Required Interface For Hosting Activity
     * Using a Callback Interface to delegate on-click events from CrimeListFragment
     * back to its hosting activity
     * */

    interface Callbacks
    {
        //Method Used by Hosting Activity to Switch Between Fragments depending on user input
        fun onCrimeSelected(crimeId: UUID)
    }

    //Variable Used to Know if a CallBack Occured
    private var callbacks: Callbacks? = null

    /**
     * Overriding LifeCycle Functions
     * This is called when a fragment is attached to an activity
     * Here you stash the Context Argument passed in your callbacks Property
     *
     * */
    override fun onAttach(context: Context)
    {
        super.onAttach(context)

        //This ensures THAT The hosting Activity MUST Implement
        //CrimeListFragment.Callbacks Since we are type Casting the Context Which is the Hosting Activity
        callbacks = context as Callbacks?
    }

    /**
     * This LifeCycle Function
     * You set the variable to null here because afterward you cannot access the activity or count on the activity continuing to exist
     *
     */

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }


    private lateinit var crimeRecyclerView: RecyclerView
    //Since the fragment will have to wait for results from the database before it can populat ethe recycler view with crimes
    //It Initially starts with an empty list
    private var adapter: CrimeAdapter? = CrimeAdapter(emptyList())

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }


    //Where you inflate the Fragments View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Getting the Fragments View
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        //Getting our Recycler View
        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView

        //Giving Recycler View A LayoutManager IT REQUIRES IT TO WORK CORRECTLY
        //Layout Manager Positions every item("widget") and also defines how scrolling works
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        crimeRecyclerView.adapter = adapter

        return view //Returning Our View
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
        * LiveData.observe(LifeCycleOwner, Observer) function is used to register an observer on the LiveData instance and tie the life of the observation
        * To the life of another component , such as an activity or fragment
         * The second parameter to the observation(..) function is an Observer implementation. This object is responsible for reacting to new data from the LiveData
         * The LifecycleOwner parameter the lifetime of the Observer you provide is scoped to the lifetime of the Android component represented
         * by the Lifecycle Owner you provided
         * As long as the lifecycle owner you scope your observer to is in a valid lifecycle state
         * */
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer{ crimes ->
                crimes?.let {
                    Log.i(TAG, "Got crimes ${crimes.size}")
                    updateUI(crimes)
                }
            })
    }

    private fun updateUI(crimes: List<Crime>) {
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }

    //Recycler View Needs a Crime Holder
    //It expects an Item View to be wrapped in an instance of View Holder
    //View Holder - stores a reference to an item's view and sometimes references to specific widgets within that view
    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        //PRIVATE DATA FIELDS
        private lateinit var crime: Crime
        //Getting References to Widgets
        private val titleTextView: TextView =
            itemView.findViewById(R.id.crime_title)

        private val dateTextView: TextView =
            itemView.findViewById(R.id.crime_date)

        private val solvedImageView: ImageView =
            itemView.findViewById(R.id.crime_solved)

        init {
            itemView.setOnClickListener(this)
        }
        //The base ViewHolder class will hold onTo the view in a property named item view

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title

            val currentDate = DateFormat.format("EEEE, MMMM dd, yyyy", Calendar.getInstance())
            dateTextView.text = currentDate

            solvedImageView.visibility = if (crime.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        /***
         * Updating the clcik listener for individual items in the crime list
         * so that pressing a crime notifies the hosting activity via the Callbacks interface
         */

        override fun onClick(v: View) {
            callbacks?.onCrimeSelected(crime.id)
        }
    }
    // Recycler View needs A Crime Adapter
    //Recycler View Does not create ViewHolders itself Instead it ask an Adapter
    //An Adapter is a controller object that sits between the RecyclerView And the data set that the RecyclerView should display
    //Responsible for: Creating the necessary View Holder when asked
    //Binding ViewHolders to data from the model layer when asked
    private inner class CrimeAdapter(var crimes: List<Crime>) :
        RecyclerView.Adapter<CrimeHolder>() {

        //Creating a ViewHolder
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : CrimeHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)

            return CrimeHolder(view)
        }

        //Getting the Data to store it in a ViewHolder and Binding them together
        //KEEP THIS SMALL So scroll animation feels smooth
        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }

        override fun getItemCount() = crimes.size

    }

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}