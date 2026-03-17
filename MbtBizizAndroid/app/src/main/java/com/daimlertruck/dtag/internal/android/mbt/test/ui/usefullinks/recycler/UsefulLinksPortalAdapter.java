package com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinks.recycler;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ItemUsefulLinksPortalMenuBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundViewHolder;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ItemUsefulLinksPortalMenuBinding;

import java.util.ArrayList;
import java.util.List;

public class UsefulLinksPortalAdapter extends DataBoundAdapter<ItemUsefulLinksPortalMenuBinding> {
    private ArrayList<UsefulLinksPortalItem> list;
    private final UsefulLinksPortalItemCallback portalItemCallback;

    public UsefulLinksPortalAdapter(ArrayList<UsefulLinksPortalItem> list, UsefulLinksPortalItemCallback itemCallback) {
        super(R.layout.item_useful_links_portal_menu);
        this.list = list;
        this.portalItemCallback = itemCallback;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    protected void bindItem(DataBoundViewHolder<ItemUsefulLinksPortalMenuBinding> holder, int position, List<Object> payloads) {
        holder.binding.setData(list.get(position));
        holder.binding.setCallback(portalItemCallback);
    }

    public interface UsefulLinksPortalItemCallback {
        void onItemClick(UsefulLinksPortalItemType itemType);
    }
}
