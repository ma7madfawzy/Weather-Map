package com.weather.map.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.weather.map.utils.extensions.JsonExtensions.getNullableString
import org.json.JSONObject

data class CoverDM(
    val tiny: String?,
    val small: String?,
    val medium: String?,
    val large: String?,
    val default: String?,
    val template: String?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    constructor(jsonObject: JSONObject?)
            : this(
        jsonObject?.getNullableString("tiny"),
        jsonObject?.getNullableString("small"),
        jsonObject?.getNullableString("medium"),
        jsonObject?.getNullableString("large"),
        jsonObject?.getNullableString("default"),
        jsonObject?.getNullableString("template")
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(tiny)
        parcel.writeString(small)
        parcel.writeString(medium)
        parcel.writeString(large)
        parcel.writeString(default)
        parcel.writeString(template)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CoverDM> {
        override fun createFromParcel(parcel: Parcel): CoverDM {
            return CoverDM(parcel)
        }

        override fun newArray(size: Int): Array<CoverDM?> {
            return arrayOfNulls(size)
        }
    }

}