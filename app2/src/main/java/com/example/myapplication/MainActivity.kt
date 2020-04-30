package com.example.myapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    val TAG: String = "MainActivity"
    val SIMPLE_INTENT: Int = 1111
    val LOG_INTENT: Int = 2222

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val testVal: Int = 10
        Log.d(TAG, "val testVal는 " + testVal)

        var testVar = 15
        Log.d(TAG, "var testVar는 " + testVar)

        Log.d(TAG, "function sum a & b " + sum(1,3))

        initVar()
        var mainAction = findViewById(R.id.mainMaterialBtn) as MaterialButton

        val nextIntent = Intent(this, HomeActivity::class.java)

        mainAction.setOnClickListener {
            startActivityForResult(nextIntent, SIMPLE_INTENT)
        }

    }

    fun initVar(){

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(resultCode){
                1111 -> {
                    Log.d(TAG, "activity comeback succesfully! title:" +
                            " " + data!!.getStringExtra("title").toString()
                    + " name:" + data!!.getStringExtra("contents").toString())
                }
            }
        }
    }

    fun startActivity(){

    }

    fun sum(a: Int, b: Int): Int{
        if( a > b) return a
        else return b
    }
}
