package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.socialmedia;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SocialMediaEntity {

    @SerializedName("medias")
    private List<MediasItem> medias;

    public void setMedias(List<MediasItem> medias){
        this.medias = medias;
    }

    public List<MediasItem> getMedias(){
        return medias;
    }
}
