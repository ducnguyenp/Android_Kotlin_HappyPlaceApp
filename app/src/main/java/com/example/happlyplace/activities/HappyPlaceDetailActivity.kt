package com.example.happlyplace.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happlyplace.adapter.HappyPlacesAdapter
import com.example.happlyplace.databinding.ActivityHappyPlaceDetailBinding
import com.example.happlyplace.models.HappyPlaceModel

class HappyPlaceDetailActivity : AppCompatActivity() {
    var happyPlaceData: HappyPlaceModel? = null
    private var binding: ActivityHappyPlaceDetailBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHappyPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarHappyPlaceDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Happy place detail"

        binding?.toolbarHappyPlaceDetail?.setNavigationOnClickListener() {
            onBackPressed()
        }
        getHappyPlaceDetail()
    }

    fun getHappyPlaceDetail() {
        if (intent.hasExtra(HappyPlacesAdapter.EXTRA_PLACE_DETAIL)) {
            happyPlaceData =
                intent.getSerializableExtra(HappyPlacesAdapter.EXTRA_PLACE_DETAIL) as HappyPlaceModel
        }
        if (happyPlaceData != null) {
            binding?.ivPlaceImage?.setImageURI(Uri.parse(happyPlaceData?.image))
            binding?.tvDescription?.text = happyPlaceData?.description
            binding?.tvLocation?.text = happyPlaceData?.description
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}