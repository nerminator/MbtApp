package com.daimler.biziz.android.model;

import com.daimler.biziz.android.network.entity.transportation.ShuttleListEntity;
import com.daimler.biziz.android.network.entity.transportation.StopListEntity;

import java.io.Serializable;

public class ShuttleAdapterModel implements Serializable {
    private ShuttleListEntity shuttleListEntity;
    private StopListEntity stopListEntity;

    public ShuttleAdapterModel(ShuttleListEntity shuttleListEntity, StopListEntity stopListEntity) {
        this.shuttleListEntity = shuttleListEntity;
        this.stopListEntity = stopListEntity;
    }

    public ShuttleListEntity getShuttleListEntity() {
        return shuttleListEntity;
    }

    public void setShuttleListEntity(ShuttleListEntity shuttleListEntity) {
        this.shuttleListEntity = shuttleListEntity;
    }

    public StopListEntity getStopListEntity() {
        return stopListEntity;
    }

    public void setStopListEntity(StopListEntity stopListEntity) {
        this.stopListEntity = stopListEntity;
    }
}
