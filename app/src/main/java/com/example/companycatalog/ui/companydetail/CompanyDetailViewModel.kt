package com.example.companycatalog.ui.companydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.companycatalog.api.Repository
import com.example.companycatalog.model.Resource
import com.example.companycatalog.model.dataclass.CompanyDetail

class CompanyDetailViewModel : ViewModel() {

    val repository = Repository.instance
    val companyDetailLiveData: LiveData<Resource<List<CompanyDetail>>> by lazy {
        repository.getCompanyDetailLiveData(companyId)
    }

    lateinit var companyId: String
    var isMarkerAdded = false
}