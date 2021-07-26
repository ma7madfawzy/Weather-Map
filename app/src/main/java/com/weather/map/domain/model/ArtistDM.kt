package com.weather.map.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.weather.map.utils.extensions.JsonExtensions.getNullableString
import org.json.JSONObject

data class ArtistDM(val name: String?) :Parcelable{
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    constructor(jsonObject: JSONObject?)
            : this(jsonObject?.getNullableString("name"))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ArtistDM> {
        override fun createFromParcel(parcel: Parcel): ArtistDM {
            return ArtistDM(parcel)
        }

        override fun newArray(size: Int): Array<ArtistDM?> {
            return arrayOfNulls(size)
        }
    }
}