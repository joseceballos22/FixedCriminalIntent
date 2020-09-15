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
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "CrimeListFragment"

//This will be the Fragment Class (MINI Activity For the CrimeListViewModel)
class CrimeListFragment : Fragment() {


    //Creating an Adapter to talking with Recycler View
    private var adapter: CrimeAdapter? = null

    //Used to Reference the RecyclerView View
    private lateinit var crimeRecyclerView: RecyclerView

    //by lazy says we will only initialize it once the property is used
    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}")
    }

    //Makes Static Methods / static variables
    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }

    //Where we Hook up the Layout to the Fragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Hooking up the layout with the fragment
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        //Getting Widget References
        this.crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        //Every Recycler View Needs a LayoutManger to position every item and also define how scrolling works
        //LinearLayoutManger Positions all the items vertically
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        //Calling Method So that Recycler view and Adapter can talk with each other
        this.updateUI()

        return view //Returning the view
    }


    //Method Used so adapter can talk with recycler view
    private fun updateUI()
    {
        val crimes = crimeListViewModel.crimes
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }



    //WILL NOW ALSO HANDLE CLICKS by adding interface View.OnClickListener

    //Since every item view needs a view holder because thats how Recycler view expectts item views to be wrapped
    //We are going to create our own inner private class that defines a view holder

    /*IN CrimeHolder's constructor you take in the view to hold on to. Immediately you pass it as the arguments to the Recyclerview.ViewHolder constructor. The
    * Base ViewHolder class will then hold on to the view in a property named itemView*/
    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var crime: Crime

        //Stashing references to the title and date text views so I can easily change the values displayed
        //Remember itemView holds the reference to the view (Stored in ViewHolder Class)
        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)

        init {
            super.itemView.setOnClickListener(this)
        }

        //Used so that onBindView Doesnt have to much info about a crime object
        fun bind(crime: Crime)
        {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()

            //Making the ImageView Visible If we solved the crime
            this.solvedImageView.visibility = if(crime.isSolved)
            {
                View.VISIBLE
            }
            else
            {
                View.GONE
            }

        }

        override fun onClick(p0: View?) {
            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
        }

    }

    //Creating Adapter class
    /**Adapter is Responsible for:
     * Creating the necessary ViewHolders when asked
     * Binding ViewHolders to data from the model layer when asked*/

    private inner class CrimeAdapter(var crimes: List<Crime>) : RecyclerView.Adapter<CrimeHolder>()
    {
        //Respondsible for creating a view to display, wrapping the view in a view holder, and returning the result
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            //Inflating the Layout of the item views so it know what it has
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)

            //Returning a CrimeHolder with that view
            return CrimeHolder(view)
        }

        //When the recycler view needs to know how many items are in the data set backing it will ask its adapter by calling this method
        //The Total Number of ViewHolders ("Item Views") is the size of the list passted in
        override fun getItemCount(): Int = crimes.size


        //Responsible for populating a given holder with the crime from a given position
        //Initializing the ViewHolders With the data from the crimes list Depending on the position that they are on
        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]

            holder.bind(crime)
        }


    }



}







