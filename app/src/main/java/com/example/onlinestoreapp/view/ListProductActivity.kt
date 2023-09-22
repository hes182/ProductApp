package com.example.onlinestoreapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.example.onlinestoreapp.R
import com.example.onlinestoreapp.databinding.ActivityListProductBinding
import com.example.onlinestoreapp.view.viewmodel.ProductViewModel

class ListProductActivity : AppCompatActivity() {
    lateinit var binding: ActivityListProductBinding

    private lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productViewModel = ViewModelProviders.of(this)[ProductViewModel::class.java]

        productViewModel.setProductListener(object : ProductViewModel.RequestListener() {
            override fun OnStart() {
                super.OnStart()
                binding.shimmerProduct.visibility = View.VISIBLE
                binding.shimmerProduct.startShimmer()
                binding.tvlbAddproduct.visibility = View.GONE
                binding.ibmAddproduct.visibility = View.GONE
            }

            override fun OnData(totData: Int) {
                super.OnData(totData)
                if (totData > 0) {
                    binding.tvlbAddproduct.visibility = View.GONE
                    binding.ibmAddproduct.visibility = View.GONE
                } else {
                    binding.tvlbAddproduct.visibility = View.VISIBLE
                    binding.ibmAddproduct.visibility = View.VISIBLE
                }
            }

            override fun OnFinish() {
                super.OnFinish()
                binding.shimmerProduct.visibility = View.GONE
                binding.shimmerProduct.stopShimmer()
            }
        })

        productViewModel.setProduct()

        binding.ibmAddproduct.setOnClickListener {
            val inten = Intent(this, AddProductActivity::class.java)
            startActivity(inten)
        }
    }
}