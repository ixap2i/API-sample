package com.ixap2i.proto.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.squareup.moshi.*
import se.ansman.kotshi.JsonSerializable
import se.ansman.kotshi.KotshiJsonAdapterFactory

@JsonSerializable
data class Pagination(
    val total: String,
    val offet: String,
    val limit: String
)

@KotshiJsonAdapterFactory
data class Album(
    @field:Json(name = "pagination") var pagination: Pagination,
    @field:Json(name = "cooking_records") var cookingRecords: List<CookingRecord>
) {

    companion object {
    val INSTANCE: Companion = Album
        @ToJson fun toJson(writer: JsonWriter, value: CookingRecord, stringAdapter: JsonAdapter<CookingRecord>) {
            stringAdapter.toJson(writer, value)
        }
    }
}

data class CookingRecord(
    val image_url: String?,
    val comment: String,
    @Json(name = "recipe_type") val recipeType: String?,
    val recorded_at: String?
)

class CookingRecordViewModel: ViewModel() {
    val cookRecoLiveData = MediatorLiveData<List<CookingRecord>>()
}
