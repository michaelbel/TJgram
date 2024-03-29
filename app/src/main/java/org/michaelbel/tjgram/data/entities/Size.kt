package org.michaelbel.tjgram.data.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Size (
    @Expose @SerializedName("width") val width: Long,
    @Expose @SerializedName("height") val height: Long,
    @Expose @SerializedName("ratio") val ratio: Double
): Serializable {

    fun mediaRatio() = width / height
}