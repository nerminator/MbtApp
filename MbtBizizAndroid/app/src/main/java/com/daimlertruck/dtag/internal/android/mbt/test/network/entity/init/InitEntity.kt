package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.init

import com.google.gson.annotations.SerializedName

data class InitEntity(
    @SerializedName("title")
    val title: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("buttonList")
    val buttonList: List<ButtonList>?,
)