package com.example.companycatalog.ui.companydetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.companycatalog.R
import com.example.companycatalog.api.Urls
import com.example.companycatalog.base.callback.ImageLoadedCallback
import com.example.companycatalog.base.extention.formatNumberPhone
import com.example.companycatalog.model.Resource
import com.example.companycatalog.model.dataclass.CompanyDetail
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_company_detail.*

class CompanyDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel by lazy {
        ViewModelProvider(this).get(CompanyDetailViewModel::class.java)
    }
    private var googleMap: GoogleMap? = null

    companion object {

        private const val COMPANY_ID = "CompanyDetailActivity.COMPANY_ID"

        fun newIntent(context: Context, companyId: String): Intent {
            val intent = Intent(context, CompanyDetailActivity::class.java)
            intent.putExtra(COMPANY_ID, companyId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getArgs()
        initGoogleMap()
        initLiveData()
    }

    private fun getArgs() {
        viewModel.companyId = intent?.extras?.getString(COMPANY_ID)!!
    }

    private fun initGoogleMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentGoogleMap) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        viewModel.isMarkerAdded = false
    }

    private fun initLiveData() {
        viewModel.companyDetailLiveData.observe(this, Observer { resource ->
            when (resource.status) {
                Resource.Status.LOADING -> startLoading()
                Resource.Status.SUCCESS -> {
                    if (!resource.data.isNullOrEmpty())
                        initView(resource.data[0])
                    completeLoading()
                }
                Resource.Status.ERROR -> errorLoading()
            }
        })
    }

    private fun startLoading() {
        pbCompanyDetail.visibility = View.VISIBLE
        svCompanyDetail.visibility = View.GONE
        includeCompanyDetailError.visibility = View.GONE
    }

    private fun completeLoading() {
        svCompanyDetail.visibility = View.VISIBLE
        pbCompanyDetail.visibility = View.GONE
        includeCompanyDetailError.visibility = View.GONE
    }

    private fun errorLoading() {
        includeCompanyDetailError.visibility = View.VISIBLE
        svCompanyDetail.visibility = View.GONE
        pbCompanyDetail.visibility = View.GONE
    }

    private fun initView(company: CompanyDetail) {
        tvNameCompany.text = company.name
        tvDescriptionCompany.text = company.description
        Picasso.get()
            .load(Urls.BASE_URL + company.img)
            .into(
                ivCompany,
                ImageLoadedCallback(
                    ivCompany,
                    pbCompanyDetailImage
                )
            )

        if (googleMap != null && !viewModel.isMarkerAdded) {
            if (company.lat != null && company.lon != null && company.lat != 0.0 && company.lon != 0.0)
                addMarker(company.lat, company.lon, company.name)
            else
                fragmentGoogleMap.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(company.www))
            tvLinkCompany.text = company.www
        else
            llLinkCompany.visibility = View.GONE

        if (!TextUtils.isEmpty(company.phone))
            tvPhoneCompany.text = company.phone!!.formatNumberPhone()
        else
            llPhoneCompany.visibility = View.GONE
    }

    private fun addMarker(latitude: Double, longitude: Double, nameMarker: String?) {
        viewModel.isMarkerAdded = true
        val latLng = LatLng(latitude, longitude)
        googleMap?.addMarker(
            MarkerOptions().position(latLng).title(nameMarker)
        )
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        if (!viewModel.isMarkerAdded && !viewModel.companyDetailLiveData.value?.data.isNullOrEmpty()) {
            val company = viewModel.companyDetailLiveData.value?.data!![0]
            if (company.lat != null && company.lon != null && company.lat != 0.0 && company.lon != 0.0)
                addMarker(company.lat, company.lon, company.name)
            else
                fragmentGoogleMap.visibility = View.GONE
        }
    }
}