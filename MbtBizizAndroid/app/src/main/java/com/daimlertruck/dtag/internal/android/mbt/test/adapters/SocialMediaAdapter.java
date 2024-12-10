package com.daimlertruck.dtag.internal.android.mbt.test.adapters;

import androidx.databinding.library.baseAdapters.BR;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundViewHolder;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.MultiTypeDataBoundAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.socialmedia.SocialMediaItem;

import java.util.List;

public final class SocialMediaAdapter extends MultiTypeDataBoundAdapter<SocialMediaItem> {

    private SocialMediaItemCallback callback;

    @Override
    public int getItemLayoutId(int position) {
        boolean isHeader = Boolean.TRUE.equals(getItem(position).component3());
        if (isHeader) {
            return R.layout.item_social_media_header;
        } else {
            return R.layout.item_social_media;
        }
    }

    public void setItems(List<SocialMediaItem> socialMediaItems){
        mItems = socialMediaItems;
        notifyDataSetChanged();
    }

    @Override
    protected void bindItem(DataBoundViewHolder holder, int position, List payloads) {
        super.bindItem(holder, position, payloads);
        boolean isHeader = Boolean.TRUE.equals(getItem(position).component3());
        if (!isHeader){
            holder.binding.setVariable(BR.callback, callback);
        }
    }

    public void setCallback(SocialMediaItemCallback callback) {
        this.callback = callback;
    }

    public interface SocialMediaItemCallback {

        void onUrlClick(String url);
    }
}