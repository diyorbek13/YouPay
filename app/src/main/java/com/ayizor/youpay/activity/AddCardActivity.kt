package com.ayizor.youpay.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.ayizor.youpay.R
import com.ayizor.youpay.helper.Logger
import com.ayizor.youpay.model.Card
import com.ayizor.youpay.networking.RetrofitHttp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class AddCardActivity : AppCompatActivity() {
    private val tag = AddCardActivity::class.simpleName

    private lateinit var etCardNumber: EditText
    private lateinit var etExDateMonth: EditText
    private lateinit var etExDateYear: EditText
    private lateinit var etCvv: EditText
    private lateinit var etCardHolderName: EditText
    private lateinit var btnAddCard: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)
        initViews()
    }

    private fun initViews() {
        etCardNumber = findViewById(R.id.etCardNumber)
        etExDateMonth = findViewById(R.id.etExDateMonth)
        etExDateYear = findViewById(R.id.etExDateYear)
        etCvv = findViewById(R.id.etCvv)
        etCardHolderName = findViewById(R.id.etCardHolderName)
        btnAddCard = findViewById(R.id.btnAddCard)
        val ivCancel: ImageView = findViewById(R.id.ivCancel)
        ivCancel.setOnClickListener {
            finish()
        }

        btnAddCard.setOnClickListener {
            val uuid: UUID = UUID.randomUUID()
            val card_id: String = uuid.toString()
            val card_number = etCardNumber.text.toString()
            val card_cvv = etCvv.text.toString()
            val card_holderName = etCardHolderName.text.toString()
            val expiration_data = etExDateMonth.text.toString() + "/" + etExDateYear.text.toString()
            var synchronized: Int = 0
            if (isNetworkAvailable(this)) {
                synchronized = 1
            }


            val card =
                Card(card_id, card_cvv, card_number, expiration_data, card_holderName, synchronized)
            sendData(card)
        }
    }

    private fun sendData(card: Card) {
        saveDataOnServer(card)
        Intent(this@AddCardActivity, MainActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun saveDataOnServer(card: Card) {
        RetrofitHttp.cardService.createCard(card).enqueue(object : Callback<Card> {
            override fun onResponse(call: Call<Card>, response: Response<Card>) {
                if (!response.isSuccessful) {
                    Logger.d(tag!!, "save: ${response.code()}")
                    return
                }

                Logger.d(tag!!, "save: ${response.toString()}")
            }

            override fun onFailure(call: Call<Card>, t: Throwable) {
                Logger.e(tag!!, "Error: ${t.message}")
            }
        })
    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }
}