package com.example.creativegalileo.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.example.creativegalileo.R
import com.example.creativegalileo.adapters.CustomerListPagingAdapter
import com.example.creativegalileo.adapters.LoadAdapter
import com.example.creativegalileo.databinding.ActivityCustomerListBinding
import com.example.creativegalileo.utils.NetworkUtils
import com.example.creativegalileo.viewmodels.CustomerListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CustomerListActivity : AppCompatActivity() {

    private val viewModel: CustomerListViewModel by viewModels()
    private val adapter = CustomerListPagingAdapter()
    private lateinit var binding: ActivityCustomerListBinding
    var filterCgid = ""
    var filterName = ""
    var filterEmail = ""
    var filterNumber = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerview.setHasFixedSize(true)

        adapter.addLoadStateListener { loadState ->

            if (loadState.refresh is LoadState.Loading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE

                // getting the error
                val errorState = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                if(errorState == null){
                    binding.txtError.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.btnRetry.visibility = View.GONE
                }
                else {
                    errorState.let {
                        if (!NetworkUtils.isNetworkAvailable(this)) {
                            binding.txtError.setText(R.string.oops)
                            binding.txtError.visibility = View.VISIBLE
                            binding.btnRetry.visibility = View.VISIBLE
                        } else {
                            binding.txtError.setText(R.string.something_went_wrong)
                            binding.txtError.visibility = View.VISIBLE
                            binding.btnRetry.visibility = View.VISIBLE
                        }
                        Toast.makeText(this, it.error.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        binding.btnRetry.setOnClickListener {
            adapter.retry()
        }


        binding.recyclerview.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoadAdapter { adapter.retry() },
            footer = LoadAdapter { adapter.retry() }
        )

        viewModel.customerList.observe(this) {
            if (it != null) {
                adapter.submitData(lifecycle, it)
                binding.progressBar.visibility = View.GONE
                binding.btnRetry.visibility = View.GONE
                binding.txtError.visibility = View.GONE
            }
        }


        binding.imgFilter.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.filter_layout)
            dialog.window!!
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.setCancelable(false)

            val txtCgid = dialog.findViewById<EditText>(R.id.txtCgid)
            val txtEmail = dialog.findViewById<EditText>(R.id.txtEmail)
            val txtName = dialog.findViewById<EditText>(R.id.txtFullName)
            val txtMobile = dialog.findViewById<EditText>(R.id.txtNumber)
            val txtApply = dialog.findViewById<TextView>(R.id.txtApply)
            val imgCancel = dialog.findViewById<ImageView>(R.id.imgCancel)

            txtCgid.setText(filterCgid)
            txtName.setText(filterName)
            txtEmail.setText(filterEmail)
            txtMobile.setText(filterNumber)

            imgCancel.setOnClickListener {
                dialog.dismiss()
            }

            txtApply.setOnClickListener {
                filterCgid = txtCgid.text.toString().trim()
                filterName = txtName.text.toString().trim()
                filterNumber = txtMobile.text.toString().trim()
                filterEmail = txtEmail.text.toString().trim()

                adapter.submitData(lifecycle, PagingData.empty())
                applyFilters()
                binding.progressBar.visibility = View.VISIBLE
                dialog.cancel()
            }
            dialog.show()
        }

    }

    private fun applyFilters() {
        lifecycleScope.launch {
            viewModel.fetchCustomers(
                filterCgid,
                filterName,
                filterNumber,
                filterEmail
            ).observe(this@CustomerListActivity) {
                adapter.submitData(lifecycle, it)
                binding.progressBar.visibility = View.GONE
                binding.btnRetry.visibility = View.GONE
                binding.txtError.visibility = View.GONE
            }
        }
    }
}