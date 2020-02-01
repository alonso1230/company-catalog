package com.example.companycatalog.model.dataclass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CompanyDetail(
    val id: String?,
    val name: String?,
    val img: String?,
    val description: String?,
    val lat: Double?,
    val lon: Double?,
    val www: String?,
    val phone: String?
) : Parcelable