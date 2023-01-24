package com.example.happlyplace.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.happlyplace.activities.AddHappyPlaceActivity
import com.example.happlyplace.activities.HappyPlaceDetailActivity
import com.example.happlyplace.databases.DatabaseHandler
import com.example.happlyplace.databinding.ItemHappyPlaceBinding
import com.example.happlyplace.models.HappyPlaceModel


class HappyPlacesAdapter(
    private val context: Context,
    private val list: ArrayList<HappyPlaceModel>
) : RecyclerView.Adapter<HappyPlacesAdapter.ListViewHolder>() {

    inner class ListViewHolder(private val itemBinding: ItemHappyPlaceBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(model: HappyPlaceModel) {
            itemBinding.tvTitle.text = model.title
            itemBinding.tvDescription.text = model.description
            itemBinding.ivPlaceImage.setImageURI(Uri.parse(model.image))
            itemBinding.itemContainer?.setOnClickListener {
                var intent = Intent(context, HappyPlaceDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAIL, model)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val model = list[position]
        holder.bindItem(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            ItemHappyPlaceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int) {
        val intent = Intent(context, AddHappyPlaceActivity::class.java)
        intent.putExtra(EXTRA_PLACE_DETAIL, list[position])
        activity.startActivityForResult(
            intent,
            requestCode
        )
        notifyItemChanged(position)
    }

    fun notifyDeleteItem(position: Int) {
        val dbHandler = DatabaseHandler(context)
        val isDeleted = dbHandler.deleteHappyPlace(list[position])

        if (isDeleted > 0) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    companion object {
        var EXTRA_PLACE_DETAIL = "extra_place_detail"
        var ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
    }
}