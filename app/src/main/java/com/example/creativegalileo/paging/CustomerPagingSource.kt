package com.example.creativegalileo.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.creativegalileo.api.RetrofitService
import com.example.creativegalileo.models.Customer

class CustomerPagingSource(private val retrofitService: RetrofitService,
                           private val cgId:String,
                           private val name:String,
                           private val mobile:String,
                           private val email:String,
                           private val paginated:String,
                           private val pageSize:Int) : PagingSource<Int, Customer>() {

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Customer> {
            return try {
                val position = params.key ?: 1
                val response = retrofitService.getAllCustomers(cgId, name, mobile, email, paginated, position, pageSize)

                return LoadResult.Page(
                    data = response.body()?.customerData!!.customers,
                    prevKey = if (position == 1) null else position - 1,
                    nextKey = if (position == response.body()!!.customerData.count/50 || response.body()!!.customerData.count <= 50) null else position + 1
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, Customer>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                val anchorPage = state.closestPageToPosition(anchorPosition)
                anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
            }
        }
}