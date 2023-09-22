package com.example.onlinestoreapp.common.utils

import android.app.Dialog
import android.content.Context
import android.media.Image
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.onlinestoreapp.R

object PopUp {

    fun popUpConfirmOk(cntx: Context, appOk: Boolean, message: String?, taskPopUpOk: TaskPopUpOk) {
        val dialog = Dialog(cntx)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val li = (cntx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        val v: View = li.inflate(R.layout.poup_dialog, null, false)
        dialog.setContentView(v)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        val window = dialog.window
        window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
        val ivBanner = dialog.findViewById<ImageView>(R.id.iv_banner)
        val tvMessage = dialog.findViewById<TextView>(R.id.tv_message)

        tvMessage.setTextColor(cntx.resources.getColor(R.color.green_700))
        tvMessage.text = message

        val btnOk = dialog.findViewById<Button>(R.id.btn_ok)
        if (appOk) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                ivBanner.setImageDrawable(cntx.resources.getDrawable(R.drawable.baseline_cancel_24, cntx.applicationContext.theme))
            } else {
                ivBanner.setImageDrawable(cntx.resources.getDrawable(R.drawable.baseline_cancel_24))
            }
        }
        btnOk.setOnClickListener {
            if (taskPopUpOk != null) {
                taskPopUpOk.onClickOk(appOk)
            }
            dialog.dismiss()
        }
    }

}