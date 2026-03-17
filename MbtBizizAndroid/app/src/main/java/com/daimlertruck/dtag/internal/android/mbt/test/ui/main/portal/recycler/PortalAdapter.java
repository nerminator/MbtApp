package com.daimlertruck.dtag.internal.android.mbt.test.ui.main.portal.recycler;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundViewHolder;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ItemPortalMenuBinding;

import java.util.ArrayList;
import java.util.List;

public class PortalAdapter extends DataBoundAdapter<ItemPortalMenuBinding> {
    private ArrayList<PortalItem> list = new ArrayList<>();
    private final PortalItemCallback portalItemCallback;

    public PortalAdapter(ArrayList<PortalItem> list, PortalItemCallback itemCallback) {
        super(R.layout.item_portal_menu);
        this.list = list;
        this.portalItemCallback = itemCallback;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    protected void bindItem(DataBoundViewHolder<ItemPortalMenuBinding> holder, int position, List<Object> payloads) {
        holder.binding.setData(list.get(position));
        holder.binding.setCallback(portalItemCallback);
    }

    public interface PortalItemCallback {
        void onItemClick(PortalItemType itemType);
    }
}
