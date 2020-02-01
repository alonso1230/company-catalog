package com.example.companycatalog.ui.companylist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.companycatalog.R
import com.example.companycatalog.api.Urls
import com.example.companycatalog.base.callback.ImageLoadedCallback
import com.example.companycatalog.model.dataclass.Company
import com.example.companycatalog.ui.companydetail.CompanyDetailActivity
import com.squareup.picasso.Picasso

class CompanyListAdapter() :
    RecyclerView.Adapter<CompanyListAdapter.ViewHolder>() {

    private var companyList: List<Company>? = null
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.context = parent.context
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_company,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val company = companyList?.get(position);
        if (company != null) {
            holder.tvNameCompany.text = company.name
            Picasso.get()
                .load(Urls.BASE_URL + company.img)
                .into(
                    holder.ivCompany,
                    ImageLoadedCallback(
                        holder.ivCompany,
                        holder.pbCompany
                    )
                )
            holder.llCompanyContainer.setOnClickListener {
                context?.startActivity(CompanyDetailActivity.newIntent(context!!, company.id!!))
            }
        }
    }

    override fun getItemCount() = companyList?.size ?: 0

    fun setData(companyList: List<Company>?) {
        this.companyList = companyList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llCompanyContainer: LinearLayout = itemView.findViewById(R.id.llCompanyContainer);
        val tvNameCompany: TextView = itemView.findViewById(R.id.tvNameCompany);
        val pbCompany: ProgressBar = itemView.findViewById(R.id.pbCompanyListImage);
        val ivCompany: ImageView = itemView.findViewById(R.id.ivCompany);
    }

}