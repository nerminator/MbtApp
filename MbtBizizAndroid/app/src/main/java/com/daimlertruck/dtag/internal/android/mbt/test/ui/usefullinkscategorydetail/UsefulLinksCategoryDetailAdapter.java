package com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinkscategorydetail;

import com.daimlertruck.dtag.internal.android.mbt.BR;
import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundViewHolder;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.MultiTypeDataBoundAdapter;

import java.util.List;

public class UsefulLinksCategoryDetailAdapter extends MultiTypeDataBoundAdapter<UsefulLinksCategoryItem> {
    private final PhoneNumberClickCallback callback;

    public UsefulLinksCategoryDetailAdapter(List<UsefulLinksCategoryItem> items, PhoneNumberClickCallback callback) {
        super(items);
        this.callback = callback;
    }

    @Override
    protected void bindItem(DataBoundViewHolder holder, int position, List payloads) {
        super.bindItem(holder, position, payloads);
        holder.binding.setVariable(BR.data, mItems.get(position));
        holder.binding.setVariable(BR.callback, callback);
    }

    @Override
    public int getItemLayoutId(int position) {
        if (mItems.get(position).component1() != null) {
            return R.layout.item_useful_links_category_detail_header;
        } else {
            return R.layout.item_useful_links_category_detail;
        }
    }

    public interface PhoneNumberClickCallback {
        void onItemClick(UsefulLinksCategoryItem item);
    }
}
