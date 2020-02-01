package com.example.companycatalog.api

import androidx.lifecycle.MutableLiveData
import com.example.companycatalog.model.Resource
import com.example.companycatalog.model.dataclass.Company
import com.example.companycatalog.model.dataclass.CompanyDetail
import kotlinx.coroutines.*
import retrofit2.HttpException

class Repository private constructor() {

    companion object {
        val instance = Repository()
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
    private val service by lazy {
        Service.createService()
    }

    private val companyListLiveData = MutableLiveData<Resource<List<Company>>>()
    private val companyDetailLiveData = MutableLiveData<Resource<List<CompanyDetail>>>()

    fun getCompanyListLiveData(): MutableLiveData<Resource<List<Company>>> {
        companyListLiveData.postValue(Resource(Resource.Status.LOADING, null, null))
        coroutineScope.launch {
            try {
                val request = service.getCompanyList()
                withContext(Dispatchers.Main) {
                    companyListLiveData.value = Resource(Resource.Status.SUCCESS, request, null)
                }
            } catch (e: HttpException) {
                companyListLiveData.postValue(Resource(Resource.Status.ERROR, null, e))
            } catch (e: Throwable) {
                companyListLiveData.postValue(Resource(Resource.Status.ERROR, null, e))
            }
        }
        return companyListLiveData
    }

    fun getCompanyDetailLiveData(idCompany: String): MutableLiveData<Resource<List<CompanyDetail>>> {
        companyDetailLiveData.value = Resource(Resource.Status.LOADING, null, null)
        coroutineScope.launch {
            try {
                val request = service.getCompanyDetail(idCompany)
                withContext(Dispatchers.Main) {
                    companyDetailLiveData.value = Resource(Resource.Status.SUCCESS, request, null)
                }
            } catch (e: HttpException) {
                companyDetailLiveData.postValue(Resource(Resource.Status.ERROR, null, e))
            } catch (e: Throwable) {
                companyDetailLiveData.postValue(Resource(Resource.Status.ERROR, null, e))
            }
        }
        return companyDetailLiveData;
    }

}