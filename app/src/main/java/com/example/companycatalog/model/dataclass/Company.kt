package com.example.companycatalog.model.dataclass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Company(
    val id: String?,
    val name: String?,
    val img: String?
) : Parcelable