package com.example.onlinestoreapp.view.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.webkit.JsResult
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.onlinestoreapp.common.data.ObyekProduct
import com.example.onlinestoreapp.di.remote.localstorage.SQLKontrol
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import org.json.JSONException
import org.json.JSONObject

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val cntx: Context

    init {
        this.cntx = application
    }

    private var productListener: RequestListener? = null
    private val mProduct = MutableLiveData<ArrayList<ObyekProduct>>()

    fun setProductListener(requestListener: RequestListener) {
        this.productListener = requestListener
    }


    fun getProduct() : MutableLiveData<ArrayList<ObyekProduct>> {
        return mProduct
    }

    fun setProduct() {
        val sqlKontrol = SQLKontrol(cntx)
        productListener?.OnStart()
        val client = AsyncHttpClient()
        client.addHeader("Authorization", sqlKontrol.getToken())
        client.setTimeout(30000)
        client.get(cntx, "https://tes-skill.datautama.com/test-skill/api/v1/product", object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val results = responseBody?.let { String(it) }
                try {
                    val json = JSONObject(results)
                    val code = json.getString("code")
                    val message = json.getString("message")
                    val data = json.getJSONObject("data")
                    val items = data.getJSONArray("items")
                    val obj = ArrayList<ObyekProduct>()

                    for (i in 0 until items.length()) {
                        val jresult = items.getJSONObject(i)
                        val ob = ObyekProduct()
                        ob.id = jresult.getInt("id")
                        ob.title = jresult.getString("title")
                        ob.description = jresult.getString("description")
                        ob.totalVariant = jresult.getInt("total_variant")
                        ob.totalStok = jresult.getInt("total_stok")
                        ob.price = jresult.getInt("price")
                        ob.image = jresult.getString("image")
                        ob.variant = jresult.getString("variants")

                        obj.add(ob)
                    }
                    productListener?.OnData(items.length())
                    mProduct.value = obj
                } catch (e: JSONException) {
                  Toast.makeText(cntx, e.message, Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(cntx, e.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Toast.makeText(cntx, error?.message, Toast.LENGTH_SHORT).show()
            }

            override fun onFinish() {
                super.onFinish()
                productListener?.OnFinish()
            }
        })
    }

    open class RequestListener : IFaceRequestListener {
        override fun OnStart() {}
        override fun OnFinish() {}
        override fun OnData(totData: Int) {

        }
    }

    protected interface IFaceRequestListener {
        fun OnStart()
        fun OnFinish()
        fun OnData(totData: Int)
    }
}