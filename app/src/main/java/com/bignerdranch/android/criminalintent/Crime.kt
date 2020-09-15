package com.bignerdranch.android.criminalintent

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

//Defining This Model Class as a Database Entity


@Entity
//Creating a Data class with initial values for all variables
data class Crime (@PrimaryKey var id: UUID = UUID.randomUUID(),
                  var title: String = "",
                  var date: Date = Date(),
                  var isSolved: Boolean = false)
{
}