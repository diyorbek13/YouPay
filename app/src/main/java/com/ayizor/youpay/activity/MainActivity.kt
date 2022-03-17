package com.ayizor.youpay.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.ayizor.youpay.R
import com.ayizor.youpay.adapter.CardAdapter
import com.ayizor.youpay.model.Card
import com.ayizor.youpay.networking.RetrofitHttp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var addButton:ImageView
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CardAdapter
    var cards=ArrayList<Card>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        addButton = findViewById(R.id.ivAdd)
        addButton.setOnClickListener {
            val intent = Intent(this,AddCardActivity::class.java)
            startActivity(intent)
        }
        recyclerView=findViewById(R.id.recyclerView)
        adapter= CardAdapter(cards)
        recyclerView.adapter=adapter
        recyclerView.layoutManager= GridLayoutManager(this,1)

        if (isNetworkAvailable(this)) {
            getAllcards()
        } else {
            getAllcardsFromLocal()
        }

    }

    private fun getAllcardsFromLocal() {
//        val db =
//            this.let {
//                Room.databaseBuilder(
//                    it.applicationContext,
//                    AppDatabase::class.java, "cards"
//                ).allowMainThreadQueries().build()
//            }
//        val pinDao = db.cardDao().nukeTable()
        // val pinsList: List<CardRoom> = pinDao!!.nukeTable()
    }

    private fun getAllcards() {
        RetrofitHttp.cardService.getAllCards().enqueue(object : Callback<List<Card>> {
            override fun onResponse(
                call: Call<List<Card>>,
                response: Response<List<Card>>
            ) {
                Log.d("@@@", response.body().toString())
                if (response.body()!=null){
                    cards.clear()
                    cards.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Card>>, t: Throwable) {
                Log.d("@@@", t.message.toString())
            }

        })
    }


    fun isNetworkAvailable(context: Context?): Boolean {
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