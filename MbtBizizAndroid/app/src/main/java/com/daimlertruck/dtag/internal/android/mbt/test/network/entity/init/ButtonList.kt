package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.init

import com.google.gson.annotations.SerializedName

data class ButtonList(
    @SerializedName("type")
    val type: Long?,
    @SerializedName("text")
    val text: String?,
    @SerializedName("url")
    val url: String?,
)