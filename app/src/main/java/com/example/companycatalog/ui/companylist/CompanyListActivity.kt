package com.example.companycatalog.ui.companylist

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.companycatalog.R
import com.example.companycatalog.model.Resource
import kotlinx.android.synthetic.main.activity_company_detail.*
import kotlinx.android.synthetic.main.activity_company_list.*

class CompanyListActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(CompanyListViewModel::class.java)
    }
    private lateinit var companyListAdapter: CompanyListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_list)
        initAdapter()
        initLiveData()
        initListener()
    }

    private fun initAdapter() {
        companyListAdapter = CompanyListAdapter()
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            rvCompanyList.layoutManager = LinearLayoutManager(this)
        } else {
            rvCompanyList.layoutManager = GridLayoutManager(this, 4)
        }
        rvCompanyList.itemAnimator = DefaultItemAnimator()
        rvCompanyList.adapter = companyListAdapter
    }

    private fun initLiveData() {
        viewModel.companyListLiveData.observe(this, Observer { resource ->
            when (resource.status) {
                Resource.Status.LOADING -> startLoading()
                Resource.Status.SUCCESS -> {
                    companyListAdapter.setData(resource.data)
                    completeLoading()
                }
                Resource.Status.ERROR -> errorLoading()
            }
        })
    }

    private fun initListener() {
        srlCompanyList.setOnRefreshListener {
            startLoading()
            viewModel.updateCompanyList()
        }
    }

    private fun startLoading() {
        pbCompanyList.visibility = if (srlCompanyList.isRefreshing) View.GONE else View.VISIBLE
        rvCompanyList.visibility = View.GONE
        includeCompanyListError.visibility = View.GONE
    }

    private fun completeLoading() {
        rvCompanyList.visibility = View.VISIBLE
        pbCompanyList.visibility = View.GONE
        includeCompanyListError.visibility = View.GONE
        srlCompanyList.isRefreshing = false
    }

    private fun errorLoading() {
        includeCompanyListError.visibility = View.VISIBLE
        rvCompanyList.visibility = View.GONE
        pbCompanyList.visibility = View.GONE
        srlCompanyList.isRefreshing = false
    }

}
