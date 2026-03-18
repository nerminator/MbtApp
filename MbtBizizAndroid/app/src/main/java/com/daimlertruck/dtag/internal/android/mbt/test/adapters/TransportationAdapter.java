package com.daimlertruck.dtag.internal.android.mbt.test.adapters;

import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundViewHolder;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ItemTransportationBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation.ShuttleListEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.transportation.TransportationFragment;

import java.util.List;

public class TransportationAdapter extends DataBoundAdapter<ItemTransportationBinding> {

    private List<ShuttleListEntity> shuttleList;
    private TransportationFragment.IDriverInfo iDriverInfo;

    public TransportationAdapter(List<ShuttleListEntity> shuttleList, TransportationFragment.IDriverInfo iDriverInfo) {
        super(R.layout.item_transportation);
        this.shuttleList = shuttleList;
        this.iDriverInfo = iDriverInfo;
    }

    @Override
    protected void bindItem(DataBoundViewHolder<ItemTransportationBinding> holder, int position, List<Object> payloads) {
        ItemTransportationBinding tbinding = holder.binding;

        tbinding.setData(shuttleList.get(position));
        tbinding.setClick(iDriverInfo);

        View.OnClickListener clickListener = view -> {
            int visibility = tbinding.recyclerRoute.getVisibility();
            if (visibility == View.VISIBLE) {
                tbinding.recyclerRoute.setVisibility(View.GONE);
                tbinding.imgArrow.animate().setInterpolator(new OvershootInterpolator()).rotation(0).setDuration(200).start();
            } else {
                tbinding.recyclerRoute.setVisibility(View.VISIBLE);
                tbinding.imgArrow.animate().setInterpolator(new OvershootInterpolator()).rotation(180).setDuration(200).start();
            }
        };
        tbinding.imgArrow.setOnClickListener(clickListener);
        tbinding.btnRoute.setOnClickListener(clickListener);

        StopListAdapter stopListAdapter = new StopListAdapter(shuttleList.get(position).getStopList());
        tbinding.recyclerRoute.setAdapter(stopListAdapter);
    }

    @Override
    public int getItemCount() {
        return shuttleList.size();
    }
}
