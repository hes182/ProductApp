package com.example.onlinestoreapp.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.onlinestoreapp.R
import com.example.onlinestoreapp.common.utils.FungsiLain
import com.example.onlinestoreapp.common.utils.PopUp
import com.example.onlinestoreapp.common.utils.TaskPopUpOk
import com.example.onlinestoreapp.databinding.ActivityRegisterBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import cz.msebera.android.httpclient.message.BasicHeader
import cz.msebera.android.httpclient.protocol.HTTP
import org.json.JSONException
import org.json.JSONObject
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imbBack.setOnClickListener {
            finish()
        }

        binding.tilPass.setEndIconOnClickListener {
            val selection = binding.tilPass.editText?.selectionEnd ?: 0
            val hasPasswordTransform = binding.tilPass.editText?.transformationMethod is PasswordTransformationMethod
            if (hasPasswordTransform) {
                binding.tilPass.editText?.transformationMethod = null
                binding.tilPass.endIconDrawable = resources.getDrawable(R.mipmap.seepass2)
            }else {
                binding.tilPass.editText?.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.tilPass.endIconDrawable = resources.getDrawable(R.mipmap.hiddenpass2)
            }

            binding.tilPass.editText?.setSelection(selection)
        }

        binding.tilPassconfirm.setEndIconOnClickListener {
            val selection = binding.tilPassconfirm.editText?.selectionEnd ?: 0
            val hasPasswordTransform = binding.tilPassconfirm.editText?.transformationMethod is PasswordTransformationMethod
            if (hasPasswordTransform) {
                binding.tilPassconfirm.editText?.transformationMethod = null
                binding.tilPassconfirm.endIconDrawable = resources.getDrawable(R.mipmap.seepass2)
            }else {
                binding.tilPassconfirm.editText?.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.tilPassconfirm.endIconDrawable = resources.getDrawable(R.mipmap.hiddenpass2)
            }

            binding.tilPassconfirm.editText?.setSelection(selection)
        }

        binding.tilName.editText?.filters = arrayOf( FungsiLain.inputFilter("[a-zA-z]"))

        binding.tilEmail.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length!! > 0) {
                    if (!FungsiLain.isValidEmail(p0.toString())) {
                        binding.tilEmail.editText?.error = "Invalid email format"
                    } else {
                        binding.tilEmail.editText?.error = null
                    }
                } else {
                    binding.tilEmail.editText?.error = null
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.tilPass.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length > 0) binding.tilPass.editText?.error = FungsiLain.isPassValid(s.toString())
                else binding.tilPass.editText?.error = null
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.btnRegistrasi.setOnClickListener { cekValueInput() }
    }

    private fun cekValueInput() {
        binding.tilName.editText?.error = null
        binding.tilPass.editText?.error = null
        binding.tilEmail.editText?.error = null
        binding.tilPassconfirm.editText?.error = null

        val confirmPass = binding.tilPassconfirm.editText?.text.toString()
        val pass = binding.tilPass.editText?.text.toString()

        var focusView: View? = null
        var cancel = false

        if (binding.tilName.editText?.text.toString().isEmpty() || TextUtils.isEmpty(binding.tilName.editText?.text.toString())) {
            cancel = true
            focusView = binding.tilName
            binding.tilName.editText?.error = "Input is required"
        } else if (binding.tilEmail.editText?.text.toString().isEmpty() || TextUtils.isEmpty(binding.tilEmail.editText?.text.toString())) {
            cancel = true
            focusView = binding.tilEmail
            binding.tilEmail.editText?.error = "Input is required"
        } else if (binding.tilPass.editText?.text.toString().isEmpty() || TextUtils.isEmpty(binding.tilPass.editText?.text.toString())) {
            cancel = true
            focusView = binding.tilPass
            binding.tilPass.editText?.error = "Input is required"
        } else if (binding.tilPassconfirm.editText?.text.toString().isEmpty() || TextUtils.isEmpty(binding.tilPassconfirm.editText?.text.toString())) {
            cancel = true
            focusView = binding.tilPassconfirm
            binding.tilPassconfirm.editText?.error = "Input is required"
        } else if (!confirmPass.equals(pass) && confirmPass.length != pass.length) {
            cancel = true
            focusView = binding.tilPassconfirm
            binding.tilPassconfirm.editText?.error = "Confirm wrong password"
        } else if (!FungsiLain.isValidEmail(binding.tilEmail.editText?.text.toString())) {
            cancel = true
            focusView = binding.tilEmail
            binding.tilEmail.editText?.error = "Invalid Email Format"
        } else if (binding.tilPass.editText?.text!!.isNotEmpty()) {
            if (FungsiLain.isPassValid(binding.tilPass.editText?.text.toString()) != "") {
                cancel = true
                focusView = binding.tilPass
                binding.tilPass.editText?.error = FungsiLain.isPassValid(binding.tilPass.editText?.text.toString())
            }
        }

        if (cancel) {
            if (focusView != null) {
                focusView.requestFocus()
            }
        } else {
            val params = JSONObject()
            params.put("name", binding.tilName.editText?.text.toString())
            params.put("email", binding.tilEmail.editText?.text.toString())
            params.put("password", binding.tilPassconfirm.editText?.text.toString())

//            setRegis(params)
        }
    }

    private fun setRegis(params: JSONObject) {
        val client = AsyncHttpClient()
        var entitiy = StringEntity(params.toString())
        entitiy.contentEncoding = BasicHeader(HTTP.CONTENT_TYPE, "application/json")
        client.setTimeout(30000)
        client.post(this@RegisterActivity, "https://tes-skill.datautama.com/test-skill/api/v1/auth/register", entitiy, "application/json", object : AsyncHttpResponseHandler() {
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
                    if (code == "20000") {
                        PopUp.popUpConfirmOk(this@RegisterActivity, false, message, object :TaskPopUpOk {
                            override fun onClickOk(isOk: Boolean) {
                                finish()
                            }
                        })
                    } else {
                        PopUp.popUpConfirmOk(this@RegisterActivity, true, message, object :TaskPopUpOk {
                            override fun onClickOk(isOk: Boolean) {

                            }
                        })
                    }
                } catch (e: JSONException) {
                    Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT).show()
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

                    PopUp.popUpConfirmOk(this@RegisterActivity, true, message, object :TaskPopUpOk {
                        override fun onClickOk(isOk: Boolean) {

                        }
                    })
                }  catch (e: JSONException) {
                    Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT).show()
                }

            }

        })
    }

}