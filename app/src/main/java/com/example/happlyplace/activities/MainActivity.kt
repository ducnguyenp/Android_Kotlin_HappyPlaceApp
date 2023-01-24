package com.example.happlyplace.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happlyplace.adapter.HappyPlacesAdapter
import com.example.happlyplace.databases.DatabaseHandler
import com.example.happlyplace.databinding.ActivityMainBinding
import com.example.happlyplace.models.HappyPlaceModel
import com.example.happlyplace.utils.SwipeToDeleteItem
import com.example.happlyplace.utils.SwipeToEditItem

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var mHappyPlaceList: ArrayList<HappyPlaceModel>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.fabAddHappyPlace?.setOnClickListener() {
            val intent = Intent(this, AddHappyPlaceActivity::class.java)
            startActivityForResult(intent, ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }
        binding?.profileImage?.setOnClickListener() {
            val intent = Intent(this, TakeCameraActivity::class.java)
            startActivity(intent)
        }
        getHappyPlacesFromDB()
        enableSwipeToEdit()
        enableSwipeToDelete()
    }

    private fun setUpHappyPlacesList(happyPlaces: ArrayList<HappyPlaceModel>) {
        binding?.rvHappyPlacesList?.layoutManager = LinearLayoutManager(this)
        binding?.rvHappyPlacesList?.setHasFixedSize(true)
        val happyPlacesAdapter = HappyPlacesAdapter(this, happyPlaces)
        binding?.rvHappyPlacesList?.adapter = happyPlacesAdapter
    }

    private fun getHappyPlacesFromDB() {
        val dbHandler = DatabaseHandler(this)
        val happyPlaceList: ArrayList<HappyPlaceModel> = dbHandler.getHappyPlacesList()
        mHappyPlaceList = happyPlaceList

        if (happyPlaceList.size > 0) {
            binding?.rvHappyPlacesList?.visibility = View.VISIBLE
            binding?.tvNoRecord?.visibility = View.GONE
            setUpHappyPlacesList(happyPlaceList)
        } else {
            binding?.rvHappyPlacesList?.visibility = View.GONE
            binding?.tvNoRecord?.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                getHappyPlacesFromDB()
            } else {
                Log.i("Activity", "Cancelled or Back pressed")
            }
        }
    }

    private fun enableSwipeToEdit() {
        val happyPlaceListView: RecyclerView? = binding?.rvHappyPlacesList
        val editSwipeHandler = object : SwipeToEditItem(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = happyPlaceListView?.adapter as HappyPlacesAdapter
                adapter.notifyEditItem(
                    this@MainActivity,
                    viewHolder.adapterPosition,
                    ADD_PLACE_ACTIVITY_REQUEST_CODE
                )
            }
        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(happyPlaceListView)
    }

    private fun enableSwipeToDelete() {
        val happyPlaceListView: RecyclerView? = binding?.rvHappyPlacesList
        val deleteSwipeHandler = object : SwipeToDeleteItem(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = happyPlaceListView?.adapter as HappyPlacesAdapter
                adapter.notifyDeleteItem(viewHolder.adapterPosition)
            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(happyPlaceListView)
        getHappyPlacesFromDB()
    }

    companion object {
        var ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
    }
}