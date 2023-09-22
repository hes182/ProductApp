package com.example.onlinestoreapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.onlinestoreapp.MainActivity
import com.example.onlinestoreapp.R
import com.example.onlinestoreapp.common.utils.FungsiLain
import com.example.onlinestoreapp.common.utils.PopUp
import com.example.onlinestoreapp.common.utils.TaskPopUpOk
import com.example.onlinestoreapp.databinding.ActivityLoginBinding
import com.example.onlinestoreapp.di.remote.localstorage.SQLKontrol
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import cz.msebera.android.httpclient.message.BasicHeader
import cz.msebera.android.httpclient.protocol.HTTP
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var sqlKontrol: SQLKontrol

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sqlKontrol = SQLKontrol(this)

        binding.imgBacklogin.setOnClickListener {
            val inten = Intent(this, MainActivity::class.java)
            startActivity(inten)
            finish()
        }

        binding.tilEmaillogin.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length > 0) {
                    if (!FungsiLain.isValidEmail(s.toString())) {
                        binding.tilEmaillogin.editText?.error = "Invalid Email Format "
                    } else {
                        binding.tilEmaillogin.editText?.error = null
                    }
                } else {
                    binding.tilEmaillogin.editText?.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.tilPasslogin.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               if (s!!.length > 0) binding.tilPasslogin.editText?.error = FungsiLain.isPassValid(s.toString())
               else binding.tilPasslogin.editText?.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.tilPasslogin.setEndIconOnClickListener {
            val selection = binding.tilPasslogin.editText?.selectionEnd ?: 0
            val hasPasswordTransform = binding.tilPasslogin.editText?.transformationMethod is PasswordTransformationMethod
            if (hasPasswordTransform) {
                binding.tilPasslogin.editText?.transformationMethod = null
                binding.tilPasslogin.endIconDrawable = resources.getDrawable(R.mipmap.seepass2)
            }else {
                binding.tilPasslogin.editText?.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.tilPasslogin.endIconDrawable = resources.getDrawable(R.mipmap.hiddenpass2)
            }

            binding.tilPasslogin.editText?.setSelection(selection)
        }

        binding.btnLogin.setOnClickListener { cekValLogin() }
    }

    private fun cekValLogin() {
        binding.tilPasslogin.editText?.error = null
        binding.tilEmaillogin.editText?.error = null

        var viewFocus: View? = null
        var cancel = false

        if (binding.tilEmaillogin.editText?.text!!.isEmpty()) {
            cancel = true
            viewFocus = binding.tilEmaillogin
            binding.tilEmaillogin.editText?.error = "Input is required"
        } else if (!FungsiLain.isValidEmail(binding.tilEmaillogin.editText?.text!!.toString())) {
            cancel = true
            viewFocus = binding.tilEmaillogin
            binding.tilEmaillogin.editText?.error = "Invalid format email"
        } else if (binding.tilPasslogin.editText?.text!!.isEmpty()) {
            cancel = true
            viewFocus = binding.tilPasslogin
            binding.tilPasslogin.editText?.error = "Input is required"
        } else if (binding.tilPasslogin.editText?.text!!.isNotEmpty()) {
            if (FungsiLain.isPassValid(binding.tilPasslogin.editText?.text.toString()) != "") {
                cancel = true
                viewFocus = binding.tilPasslogin
                binding.tilPasslogin.editText?.error = FungsiLain.isPassValid(binding.tilPasslogin.editText?.text.toString())
            }
        }

        if (cancel) {
            if (viewFocus != null) {
                viewFocus.requestFocus()
            }
        } else {
            val params = JSONObject()
            params.put("email", binding.tilEmaillogin.editText?.text.toString())
            params.put("password", binding.tilPasslogin.editText?.text.toString())
            setLogin(params)
        }
    }

    private fun setLogin(param: JSONObject) {
        val client = AsyncHttpClient()
        val entity = StringEntity(param.toString())
        entity.contentEncoding = BasicHeader(HTTP.CONTENT_TYPE, "application/json")
        client.setTimeout(30000)
        client.post(this, "https://tes-skill.datautama.com/test-skill/api/v1/auth/login", entity, "application/json", object : AsyncHttpResponseHandler() {
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
                    val token = data.getString("token")
                    if (code == "20000") {
                        sqlKontrol.setSaveUser(token)
                        val inten = Intent(this@LoginActivity, ListProductActivity::class.java)
                        startActivity(inten)
                        finish()
                    } else {
                        PopUp.popUpConfirmOk(this@LoginActivity, true, message, object :TaskPopUpOk {
                            override fun onClickOk(isOk: Boolean) {

                            }
                        })
                    }
                } catch (e: JSONException) {
                    Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val results = responseBody?.let { String(it) }
                try {
                    val json = JSONObject(results)
                    val code = json.getString("code")
                    val message = json.getString("message")

                    val data = json.getJSONObject("data")
                    val token = data.getString("token")
                    if (code == "20000") {
                        sqlKontrol.setSaveUser(token)
                        val inten = Intent(this@LoginActivity, ListProductActivity::class.java)
                        startActivity(inten)
                        finish()
                    } else {
                        PopUp.popUpConfirmOk(this@LoginActivity, true, message, object :TaskPopUpOk {
                            override fun onClickOk(isOk: Boolean) {

                            }
                        })
                    }
                }  catch (e: JSONException) {
                    Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

}