package com.example.companycatalog.ui.companylist

import androidx.lifecycle.ViewModel
import com.example.companycatalog.api.Repository

class CompanyListViewModel : ViewModel() {

    val repository = Repository.instance
    val companyListLiveData = repository.getCompanyListLiveData()

    fun updateCompanyList() {
        repository.getCompanyListLiveData()
    }
}