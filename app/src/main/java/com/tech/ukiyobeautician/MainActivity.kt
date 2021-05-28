package com.tech.ukiyobeautician

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var url = "http://services.ukiyoservices.com/login"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (isOnline()) {

            webview.settings.javaScriptEnabled = true
            webview.settings.builtInZoomControls = true
            webview.settings.allowContentAccess = true
            webview.settings.allowContentAccess = true
            webview.settings.allowFileAccess = true
            webview.settings.userAgentString =
                    "Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3"

            webview.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    progressBar.visibility = View.VISIBLE
                    // Toast.makeText(this@MainActivity,url, Toast.LENGTH_LONG).show()
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressBar.visibility = View.GONE
                }


            }
            webview.loadUrl(url)
        } else {
            try {
                var alertDialog = AlertDialog.Builder(this).create()
                alertDialog.setTitle("Info");
                alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again")
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                var listener = DialogInterface.OnClickListener { dialog, which ->
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        dialog.dismiss()
                    finish()
                    }
                }

                alertDialog.setButton("ok", listener)
                alertDialog.show();
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show();

            }

        }
    }

    fun isOnline(): Boolean {
        var conMgr = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var netInfo = conMgr.activeNetworkInfo

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }else

            return true

    }


    override fun onBackPressed() {

        if (webview.canGoBack()) {
            webview.goBack()
        } else {
            Toasty.error(
                    this,
                    "please,press yes to exit from app.",
                    Toast.LENGTH_SHORT,
                    true
            ).show();
            var builder = AlertDialog.Builder(this)
            builder.setTitle(" Alert!")
            builder.setMessage("Do you want to exit Ukiyo?").setCancelable(false)
            builder.setIcon(R.drawable.ic_baseline_exit_to_app_24)

            var listener = DialogInterface.OnClickListener { dialog, which ->
                if (which == DialogInterface.BUTTON_NEUTRAL) {
                    dialog.dismiss()
                } else if (which == DialogInterface.BUTTON_POSITIVE) {
                    val intent = Intent(Intent.ACTION_MAIN)
                    intent.addCategory(Intent.CATEGORY_HOME)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //***Change Here***
                    startActivity(intent)
                    finish()
                    System.exit(0)
                    dialog.dismiss()

                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                    dialog.dismiss()
                }

            }


            builder.setPositiveButton("Yes", listener)
            builder.setNegativeButton("No", listener)
            builder.setNeutralButton("cancel", listener)
            builder.show()
        }


    }
}