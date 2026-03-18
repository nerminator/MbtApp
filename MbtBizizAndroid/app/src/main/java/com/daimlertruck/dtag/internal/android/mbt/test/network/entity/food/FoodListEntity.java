
package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FoodListEntity implements Parcelable {

    @SerializedName("densityList")
    private List<DensityInfo> DensityInfo;
    @SerializedName("foodInfo")
    private List<FoodInfo> FoodInfo;
    @SerializedName("shuttleInfo")
    private ShuttleInfo ShuttleInfo;

    public List<com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.DensityInfo> getDensityInfo() {
        return DensityInfo;
    }

    public void setDensityInfo(List<com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.DensityInfo> densityInfo) {
        DensityInfo = densityInfo;
    }

    public List<com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.FoodInfo> getFoodInfo() {
        return FoodInfo;
    }

    public void setFoodInfo(List<com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.FoodInfo> foodInfo) {
        FoodInfo = foodInfo;
    }

    public com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.ShuttleInfo getShuttleInfo() {
        return ShuttleInfo;
    }

    public void setShuttleInfo(com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.ShuttleInfo shuttleInfo) {
        ShuttleInfo = shuttleInfo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.DensityInfo);
        dest.writeTypedList(this.FoodInfo);
        dest.writeParcelable(this.ShuttleInfo, flags);
    }

    public FoodListEntity() {
    }

    protected FoodListEntity(Parcel in) {
        this.DensityInfo = in.createTypedArrayList(com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.DensityInfo.CREATOR);
        this.FoodInfo = in.createTypedArrayList(com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.FoodInfo.CREATOR);
        this.ShuttleInfo = in.readParcelable(com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.ShuttleInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator<FoodListEntity> CREATOR = new Parcelable.Creator<FoodListEntity>() {
        @Override
        public FoodListEntity createFromParcel(Parcel source) {
            return new FoodListEntity(source);
        }

        @Override
        public FoodListEntity[] newArray(int size) {
            return new FoodListEntity[size];
        }
    };
}
