package com.daimler.biziz.android.adapters;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.binding.DataBoundAdapter;
import com.daimler.biziz.android.binding.DataBoundViewHolder;
import com.daimler.biziz.android.databinding.ItemMeetingRoomBinding;
import com.daimler.biziz.android.databinding.ItemNotificationBinding;
import com.daimler.biziz.android.network.entity.notifications.Notification;
import com.daimler.biziz.android.network.entity.place.MeetingRoom;

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
