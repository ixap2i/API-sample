package com.ixap2i.proto

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ixap2i.proto.adapters.AlbumListAdapter
import com.ixap2i.proto.data.HttpUtil
import com.ixap2i.proto.viewmodels.Album
import com.ixap2i.proto.viewmodels.CookingRecord
import com.ixap2i.proto.viewmodels.CookingRecordViewModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import leakcanary.LeakSentry

import okhttp3.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var textView: TextView
    private lateinit var progressIndicator: ProgressBar

    val urlBuilder =  HttpUrl.parse("api_name")?.newBuilder()!!.addQueryParameter("limit", "100")

    val urlForReq = urlBuilder?.build().toString()

    val client = OkHttpClient()

    fun updateUI() = HttpUtil().getCookingRecords(urlForReq, client)

    private var counter = 0

    val liveData = CookingRecordViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: メモリリーク周り
        LeakSentry.config = LeakSentry.config.copy(watchFragmentViews = false)

        viewManager = GridLayoutManager(this@MainActivity, 2)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        try {
            GlobalScope.launch(Dispatchers.Main) {
            val jsonData = updateUI().await()

            val moshi = Moshi.Builder()
                .add(Album.INSTANCE)
                .build()
            val jsonAdapter: JsonAdapter<Album> = moshi.adapter(Album::class.java)

            val records = jsonAdapter.fromJson(jsonData.toString())!!
            viewManager = GridLayoutManager(this@MainActivity, 2)
            val cookingRecordLists  = records!!.cookingRecords.subList(0, 10)
            viewAdapter = AlbumListAdapter(cookingRecordLists)

            recyclerView = findViewById<RecyclerView>(R.id.main_album_rows).apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = viewAdapter
            }

            progressIndicator = findViewById(R.id.progressBar)
            progressIndicator.visibility = View.GONE

            if(cookingRecordLists.size == 0) {
                textView = findViewById(R.id.textViewWaring)
                textView.visibility = View.VISIBLE
            }
            liveData.cookRecoLiveData.postValue(records!!.cookingRecords)
            counter = 10
        }

        } catch(ex: Exception) {
            print(ex.stackTrace)
        }


        // TODO: 画像ロジックの整合性（現状進むと戻るの差分が噛み合っておらず、進む戻るをランダムに繰り返すとアプリが停止してしまう）
        /* 次に進むボタンをクリックした時 */
        fab.setOnClickListener { view ->

            GlobalScope.launch(Dispatchers.Main) {
                if(counter + 9 - liveData.cookRecoLiveData.value!!.size > 0) {
                    try {
                        val cookingRecordLists = liveData.cookRecoLiveData.value!!.subList(counter - 1,
                            counter + (counter + 9 - liveData.cookRecoLiveData.value!!.size))
                        viewAdapter = AlbumListAdapter(cookingRecordLists)
                        recyclerView = findViewById<RecyclerView>(R.id.main_album_rows).apply {
                            setHasFixedSize(true)
                            layoutManager = viewManager
                            adapter = viewAdapter
                        }
                        counter += counter + 9 - liveData.cookRecoLiveData.value!!.size
                    } catch(ex: Exception) {
                        ex.printStackTrace()
                    }


                } else if(counter + 9 - liveData.cookRecoLiveData.value!!.size == 0) {
                   return@launch
                } else {
                    try {
                        val cookingRecordLists = liveData.cookRecoLiveData.value!!.subList(counter - 1, counter + 9)
                        viewAdapter = AlbumListAdapter(cookingRecordLists)
                        recyclerView = findViewById<RecyclerView>(R.id.main_album_rows).apply {
                            setHasFixedSize(true)
                            layoutManager = viewManager
                            adapter = viewAdapter
                        }
                        counter += 10

                        if(counter > 0) back.visibility = View.VISIBLE
                    } catch(ex: Exception) {
                        ex.printStackTrace()
                    }

                }

            }

        }

        /* 戻るボタンをクリックした時 */
        back.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {

                val cookingRecordLists  = liveData.cookRecoLiveData.value!!

                if(counter % 9 == 0) {
                    try {
                        viewAdapter = AlbumListAdapter(cookingRecordLists.subList(counter - 20, counter - 10))
                    } catch(ex: Exception) {
                        ex.printStackTrace()
                    }
                } else {
                    try {
                        viewAdapter = AlbumListAdapter(cookingRecordLists.subList(counter - 20, counter - 10))
                    } catch(ex: Exception) {
                        ex.printStackTrace()
                    }

                }

                recyclerView = findViewById<RecyclerView>(R.id.main_album_rows).apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                }
                counter -= 10
            }

        }
    }


// TODO:ソート機能
// override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
