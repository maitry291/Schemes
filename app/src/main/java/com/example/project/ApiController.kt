package com.example.project

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

class ApiController {

    companion object{

        private const val url="https://well-coupled-fruits.000webhostapp.com/test/"

        val conn= OkHttpClient.Builder()
                  .build()

        private val gson: Gson =GsonBuilder().setLenient().create()
        var retrofit: Retrofit? =null

        fun getClient():Retrofit{
            if(retrofit==null)
            {
                retrofit = Retrofit.Builder().baseUrl(url).client(conn)
                    .addConverterFactory(NullOnEmptyConverterFactory())
                    .addConverterFactory(
                    GsonConverterFactory.create(gson)
                ).build()
                return retrofit as Retrofit
            }
            return retrofit as Retrofit
        }

    }


}
class NullOnEmptyConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val delegate: Converter<ResponseBody, *> =
            retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
        return Converter { body -> if (body.contentLength() == 0L) null else delegate.convert(body) }
    }
}