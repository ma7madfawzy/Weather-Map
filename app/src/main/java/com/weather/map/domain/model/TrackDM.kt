package com.weather.map.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.weather.map.utils.extensions.JsonExtensions.getNullableLong
import com.weather.map.utils.extensions.JsonExtensions.getNullableObject
import com.weather.map.utils.extensions.JsonExtensions.getNullableString
import org.json.JSONObject

data class TrackDM(
    val type: String?,
    val title: String?,
    val publishingDate: String?,
    val licensorName: String?,
    val duration: Long?,
    val mainArtist: ArtistDM?,
    val cover: CoverDM?
) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readParcelable(ArtistDM::class.java.classLoader),
        parcel.readParcelable(CoverDM::class.java.classLoader)
    ) {
    }

    constructor(jsonObject: JSONObject) : this(
        jsonObject.getNullableString("type"),
        jsonObject.getNullableString("name"),
        jsonObject.getNullableString("publishingDate"),
        jsonObject.getNullableString("licensorName"),
        jsonObject.getNullableLong("duration"),
        ArtistDM(jsonObject.getNullableObject("mainArtist")),
        CoverDM(jsonObject.getNullableObject("cover"))
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(title)
        parcel.writeString(publishingDate)
        parcel.writeString(licensorName)
        parcel.writeValue(duration)
        parcel.writeParcelable(mainArtist, flags)
        parcel.writeParcelable(cover, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TrackDM> {
        override fun createFromParcel(parcel: Parcel): TrackDM {
            return TrackDM(parcel)
        }

        override fun newArray(size: Int): Array<TrackDM?> {
            return arrayOfNulls(size)
        }
    }

}