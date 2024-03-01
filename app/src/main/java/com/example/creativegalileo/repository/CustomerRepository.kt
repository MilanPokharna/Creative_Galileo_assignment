package com.example.creativegalileo.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.creativegalileo.api.RetrofitService
import com.example.creativegalileo.paging.CustomerPagingSource
import javax.inject.Inject


class CustomerRepository @Inject constructor(private val retrofitService: RetrofitService) {

        fun getCustomers(cgId:String,name:String,mobile:String,email:String) = Pager(
            config = PagingConfig(pageSize = 50),
            pagingSourceFactory = { CustomerPagingSource(retrofitService,cgId,name,mobile,email,"true",50) }
        ).liveData

}