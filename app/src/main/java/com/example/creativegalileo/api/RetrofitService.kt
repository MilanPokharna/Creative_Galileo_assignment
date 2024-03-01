package com.example.creativegalileo.api

import com.example.creativegalileo.models.CustomerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    @GET("filter")
    suspend fun getAllCustomers(@Query ("cgId") cgId:String,
                                @Query ("name") name:String,
                                @Query ("mobile") mobile:String,
                                @Query ("email") email:String,
                                @Query ("paginated") paginated:String,
                                @Query ("pageNo") pageNo:Int,
                                @Query ("pageSize") pageSize:Int) : Response<CustomerResponse>

}