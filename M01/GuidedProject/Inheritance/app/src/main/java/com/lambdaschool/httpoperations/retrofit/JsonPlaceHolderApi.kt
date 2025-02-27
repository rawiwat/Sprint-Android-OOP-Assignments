package com.lambdaschool.httpoperations.retrofit


import com.lambdaschool.httpoperations.model.Employee
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface JsonPlaceHolderApi {

    @GET("employee/{id}")
    fun getEmployees(@Path("id") employeeId:String): Call<List<Employee>>

    @GET("employee")
    fun getEmployees(): Call<List<Employee>>

    fun getEmployeesForAge(@Query("age") age:String) : Call<List<Employee>>

    @POST("employee")
    fun addNewEmployee(@Body employee: Employee) : Call<Employee>

    @PUT("employee")
    fun updateEmployee(@Body employee: Employee) : Call<Employee>

    @DELETE("employee/{id}")
    fun deleteEmployee(@Path("id") employeeId:String) : Call<Void>

    class Factory {

        companion object {

            private const val BASE_URL = "http://demo0755375.mockable.io/"

            fun create(): JsonPlaceHolderApi {

                val logger = HttpLoggingInterceptor()
                logger.level = HttpLoggingInterceptor.Level.BASIC
                logger.level = HttpLoggingInterceptor.Level.BODY

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .retryOnConnectionFailure(false)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .build()

                val moshi = Moshi.Builder()
                    .add(EmployeeTypeAdapter())
                    .build()
                // TODO 11: Use moshi with type adapter
                val retrofit = Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build()

                return retrofit.create(JsonPlaceHolderApi::class.java)
            }
        }
    }
}