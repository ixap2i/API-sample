package com.ixap2i.proto

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import okhttp3.*
import org.json.JSONObject

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.io.IOException

//
//@RunWith(AndroidJUnit4::class)
//@KotshiJsonAdapterFactory
//class ParseAlbumToJson(
//    val pagination: PaginationParams,
//    val cookingRecords: List<Album>
//){
//    companion object {
//
//        @FromJson fun fromJson(jsonReader: com.squareup.moshi.JsonReader, stringAdapter: JsonAdapter<ParseAlbumToJson>): ParseAlbumToJson? {
//            return stringAdapter.fromJson(jsonReader)
//        }
//        val INSTANCE: ParseAlbumToJson.Companion = ParseAlbumToJson
//    }
//}


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.yamashita.proto", appContext.packageName)
    }

    @Test
    fun getAlbumResponces() {
        val urlBuilder =  HttpUrl.parse("api_name")?.newBuilder()

        urlBuilder?.addQueryParameter("limit", "10")
        urlBuilder?.addQueryParameter("offset", "0")

        val urlForReq = urlBuilder?.build().toString()
        val request = Request.Builder()
            .url(urlForReq)
            .build()
        val client = OkHttpClient()

        var jsonData: String?

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                println("Request was succeed $response")

                jsonData = response.body()?.string()
                val jsonObject = JSONObject(jsonData)
                jsonObject.getJSONArray("cooking_records")

               response.apply {
                   assertEquals(200, response.body())
                   assertFalse(response.body().toString()!!.contains("not found"))
                   assertTrue(response.body()!!.toString().contains("image_url"))
                   assertTrue(response.body()!!.toString().contains("comment"))
                   assertTrue(response.body()!!.toString().contains("recipe_type"))
                   assertTrue(response.body()!!.toString().contains("recorded_at"))
               }
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Error Occurred as follow reason: $e")
            }

        })
    }

//    @Test
//    fun parseToJson() {
//        val album = """
//            {
//              pagination: {
//                total: 100,  # 総レコード数
//                offset: 50,  # オフセット
//                limit: 5     # リクエスト時指定の取得数
//              },
//              cooking_records: [
//                {
//                  "image_url": "api_name/hoge.jpg",
//                  "comment": "test",
//                  "recipe_type": "main_dish",
//                  "recorded_at": "2018-04-21 14:04:22"
//                },
//                {
//                  "image_url": "api_name/images/52.jpg",
//                  "comment": "test",
//                  "recipe_type": "main_dish",
//                  "recorded_at": "2018-04-20 14:04:42"
//                },
//                {
//                  "image_url": "api_name/images/53.jpg",
//                  "comment": "test",
//                  "recipe_type": "soup",
//                  "recorded_at": "2018-04-19 14:05:41"
//                },
//                {
//                  "image_url": "api_name/images/54.jpg",
//                  "comment": "test",
//                  "recipe_type": "main_dish",
//                  "recorded_at": "2018-04-18 14:06:12"
//                },
//                {
//                  "image_url": "api_name/images/55.jpg",
//                  "comment": "test",
//                  "recipe_type": "main_dish",
//                  "recorded_at": "2018-04-17 14:07:40"
//                }
//              ]
//            }
//        """.trimIndent()
//
//
//        val jsonObject = JSONObject(album)
//        println(jsonObject.getJSONArray("cooking_records"))
//
//        val cookingRecords = JSONObject(album).get("cooking_records")
//
//        val jsonArr = cookingRecords as JSONArray
//        val moshi = Moshi.Builder()
//            .add(ParseAlbumToJson.INSTANCE)
//            .build()
//
//        val jsonAdapter: JsonAdapter<Album> = moshi.adapter(Album::class.java)
//
//
//        val list = arrayListOf(jsonArr)[0]
//        arrayListOf(list).forEachIndexed { i, el ->
//            var el = jsonAdapter.fromJson(el[i].toString())
//            println("comment "+el?.comment+
//                    " image url "+el?.image_url+
//                    " recipe type "+el?.recipeType+
//                    " recorded at"+el?.recorded_at)
//        }
//
//    }


//    @Test
//    fun parseToJson2() {
//        val jsonData: Json = JsonWriter.of(
//            """
//            {
//              pagination: {
//                total: 100,  # 総レコード数
//                offset: 50,  # オフセット
//                limit: 5     # リクエスト時指定の取得数
//              },
//              cooking_records: [
//                {
//                  "image_url": "api_name/images/1.jpg",
//                  "comment": "test",
//                  "recipe_type": "main_dish",
//                  "recorded_at": "2018-04-21 14:04:22"
//                },
//                {
//                  "image_url": "api_name/images/2.jpg",
//                  "comment": "test",
//                  "recipe_type": "main_dish",
//                  "recorded_at": "2018-04-20 14:04:42"
//                },
//                {
//                  "image_url": "api_name/images/3.jpg",
//                  "comment": "test",
//                  "recipe_type": "soup",
//                  "recorded_at": "2018-04-19 14:05:41"
//                },
//                {
//                  "image_url": "api_name/images/4.jpg",
//                  "comment": "test",
//                  "recipe_type": "main_dish",
//                  "recorded_at": "2018-04-18 14:06:12"
//                },
//                {
//                  "image_url": "api_name/images/5.jpg",
//                  "comment": "test",
//                  "recipe_type": "main_dish",
//                  "recorded_at": "2018-04-17 14:07:40"
//                }
//              ]
//            }
//        """)
//
//        val moshi = Moshi.Builder()
//            .build()
//
//        val sample = KotshiSampleJsonAdapter(moshi)
//    }
}
