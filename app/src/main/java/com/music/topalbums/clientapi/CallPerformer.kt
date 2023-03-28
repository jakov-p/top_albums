package com.music.topalbums.clientapi

import androidx.lifecycle.MutableLiveData
import com.music.topalbums.logger.Logger
import com.music.topalbums.logger.Logger.loggable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class CallPerformer<T>(val commandName :String, val methodCall: suspend ()-> Response<T>)
{
    val TAG = CallPerformer::class.java.simpleName
    val isRunning = MutableLiveData<Boolean>(false)

    suspend fun perform():T?
    {
        return try
        {
            isRunning.postValue(true)
            //delay(500) //for easier testing TODO remove later

            Logger.printTitle(TAG, commandName)

            val response = methodCall.invoke()

            withContext(Dispatchers.Main)
            {
                isRunning.value = false
                if (response.isSuccessful)
                {
                    loggable.i(TAG,  response.message())
                    val data: T? = response.body()
                    val code = response.code()

                    loggable.i(TAG, "response = $response")
                    loggable.i(TAG, "code = $code")
                    loggable.i(TAG, "body = " +  data.toString())

                    return@withContext data
                }
                else
                {
                    loggable.e(TAG, "$commandName failed")
                    loggable.e(TAG, "response = $response")
                    loggable.e(TAG, "code = " + response.code().toString())

                    val errorBody = response.errorBody()?.string() ?:""
                    loggable.e(TAG, "body = " + errorBody) 
                    throw Exception()
                }
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
