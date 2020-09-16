package com.bignerdranch.android.criminalintent

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

//Fragment for Our list Eventually Will link Both Fragments UP
class CrimeListFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        //Getting our Recycler View
        crimeRecyclerView =
            view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        crimeRecyclerView.adapter = adapter
        return view
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

        override fun onClick(v: View) {
            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT)
                .show()
        }
    }
    // Recycler View needs A Crime Adapter
    private inner class CrimeAdapter(var crimes: List<Crime>) :
        RecyclerView.Adapter<CrimeHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : CrimeHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)

            return CrimeHolder(view)
        }

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