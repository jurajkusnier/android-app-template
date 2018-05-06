package com.jurajkusnier.androidapptemplate.ui.jobs

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jurajkusnier.androidapptemplate.R
import com.jurajkusnier.androidapptemplate.data.model.Job
import kotlinx.android.synthetic.main.job_recycler_item.view.*
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri


class JobsViewAdapter(private val jobsList:List<Job>?): RecyclerView.Adapter<JobsViewAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val viewHolder = RecyclerViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.job_recycler_item, parent, false))

        viewHolder.itemView.buttonMoreInfo.setOnClickListener {
            val url= jobsList?.get(viewHolder.adapterPosition)?.url
            if (url != null) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(parent.context, browserIntent,null)
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(jobsList?.get(position))
    }

    override fun getItemCount(): Int {
        return jobsList?.size ?: 0
    }

    class RecyclerViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(job:Job?) {
            view.textTitle.text =  job?.title
            view.textTime.text = job?.created_at
            view.textCompany.text = job?.company
            view.textLocation.text = job?.location
        }
    }
}