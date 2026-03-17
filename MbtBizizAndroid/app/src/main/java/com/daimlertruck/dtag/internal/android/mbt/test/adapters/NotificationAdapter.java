package com.daimlertruck.dtag.internal.android.mbt.test.adapters;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundViewHolder;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ItemNotificationBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.notifications.Notification;

import java.util.List;

public class NotificationAdapter extends DataBoundAdapter<ItemNotificationBinding> {
    private final NotificationItemCallback callback;
    List<Notification> list;

    public NotificationAdapter(List<Notification> list, NotificationItemCallback callback) {
        super(R.layout.item_notification);
        this.list = list;
        this.callback = callback;

    }


    @Override
    protected void bindItem(DataBoundViewHolder<ItemNotificationBinding> holder, int position, List<Object> payloads) {

        holder.binding.setData(list.get(position));
        holder.binding.setCallback(callback);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public interface NotificationItemCallback {
        void onNotificationItemClick(Notification notification);
    }
}
