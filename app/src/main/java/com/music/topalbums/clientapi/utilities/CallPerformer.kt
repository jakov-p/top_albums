package com.music.topalbums.clientapi.utilities

import androidx.lifecycle.MutableLiveData
import com.music.topalbums.logger.Logger
import com.music.topalbums.logger.Logger.loggable
import retrofit2.Response
import kotlin.system.measureTimeMillis

/**
 * Call performer is a wrapper that performs a http request and logs
 * the result (it also measures the time).
 *
 * It also provides 'isRunning' object to inform about the running status.
 *
 * @param T the payload data of the response
 * @param commandDescription the describing text, just for logging
 * @param methodCall the actual HTTP request to be performed
 */
class CallPerformer<T>(val commandDescription :String, val methodCall: suspend ()-> Response<T>)
{
    val TAG = CallPerformer::class.java.simpleName
    val isRunning = MutableLiveData<Boolean>(false)

    suspend fun perform():T?
    {
        return try
        {
            isRunning.postValue(true)
            //delay(500) //for easier testing

            Logger.printTitle(TAG, commandDescription)

            val response:Response<T>
            measureTimeMillis  {
                response = methodCall.invoke()
            }.also {
                loggable.i(TAG,  "The execution time  = " + it.toFloat()/1000)
            }

            isRunning.postValue(false)

            if (response.isSuccessful)
            {
                loggable.i(TAG,  response.message())
                val data: T? = response.body()
                val code = response.code()

                loggable.i(TAG, "response = $response")
                loggable.i(TAG, "code = $code")
                loggable.i(TAG, "body = " +  data.toString())

                return data
            }
            else
            {
                loggable.e(TAG, "$commandDescription failed")
                loggable.e(TAG, "response = $response")
                loggable.e(TAG, "code = " + response.code().toString())

                val errorBody = response.errorBody()?.string() ?:""
                loggable.e(TAG, "body = " + errorBody)
                throw Exception()
            }
        }
        catch (ex: Exception)
        {
            throw ex
        }
        finally
        {
            loggable.i(TAG,  "****************************************************************************** ")
        }
    }
}
