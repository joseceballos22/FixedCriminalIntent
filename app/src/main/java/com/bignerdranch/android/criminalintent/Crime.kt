package com.bignerdranch.android.criminalintent

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID
import java.util.Date

//ENTITY CLASS USED TO DEFINE HASH TABLE ROWS AND COLUMNS
@Entity
data class Crime(@PrimaryKey val id : UUID = UUID.randomUUID(),
                 var title: String = "",
                 var date : Date = Date(),
                 var isSolved : Boolean = false)

