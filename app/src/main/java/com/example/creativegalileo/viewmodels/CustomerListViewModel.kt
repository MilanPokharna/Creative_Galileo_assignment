package com.example.creativegalileo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.creativegalileo.models.Customer
import com.example.creativegalileo.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CustomerListViewModel
@Inject constructor(private val customerRepository: CustomerRepository) : ViewModel(){


    var customerList = customerRepository.getCustomers("", "", "", "").cachedIn(viewModelScope)

    suspend fun fetchCustomers(cgId:String, name:String, mobile:String, email:String) :LiveData<PagingData<Customer>>  {

        return customerRepository.getCustomers(cgId, name, mobile, email).cachedIn(viewModelScope)
    }

}
