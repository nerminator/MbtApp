package com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinkscategory;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundViewHolder;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ItemUsefulLinksCategoryBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.usefullinks.UsefulLinksCategoryLocation;

import java.util.ArrayList;
import java.util.List;

public class UsefulLinksCategoryAdapter extends DataBoundAdapter<ItemUsefulLinksCategoryBinding> {
    private ArrayList<UsefulLinksCategoryLocation> list;
    private final UsefulLinksCategoryItemCallback categoryItemCallback;

    public UsefulLinksCategoryAdapter(ArrayList<UsefulLinksCategoryLocation> list, UsefulLinksCategoryItemCallback itemCallback) {
        super(R.layout.item_useful_links_category);
        this.list = list;
        this.categoryItemCallback = itemCallback;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    protected void bindItem(DataBoundViewHolder<ItemUsefulLinksCategoryBinding> holder, int position, List<Object> payloads) {
        holder.binding.setData(list.get(position));
        holder.binding.setCallback(categoryItemCallback);
    }

    public interface UsefulLinksCategoryItemCallback {
        void onItemClick(Integer id);
    }
}
