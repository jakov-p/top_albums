package com.music.topalbums.clientapi

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException


class LogJsonInterceptor : Interceptor
{
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response
    {
        val request: Request = chain.request()
        val response: Response = chain.proceed(request)
        val rawJson = response.body!!.string()

        try
        {
            val obj = JSONTokener(rawJson).nextValue()
            val jsonLog = if (obj is JSONObject)
            {
                obj.toString(4)
            }
            else
            {
                (obj as JSONArray).toString(4)
            }
            println(jsonLog)
        }
        catch (e: JSONException)
        {
            e.printStackTrace()
        }

        //val text1 = """{"id":{"label":"https://music.apple.com/us/album/gettin-old/1666738524?uo=2", "attributes":{"im:id":"1666738524"}}}"""
        //val text1 = """{"attributes":{"im_id":"1666738524"}}"""
        //val responseBody = ResponseBody.create(response.body!!.contentType(), text1)

        // Re-create the response before returning it because body can be read only once
        val responseBody = ResponseBody.create(response.body!!.contentType(), rawJson)
        return response.newBuilder().body(responseBody).build()
    }
}