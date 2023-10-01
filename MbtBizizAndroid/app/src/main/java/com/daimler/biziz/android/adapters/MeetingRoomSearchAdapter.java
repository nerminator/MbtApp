package com.daimler.biziz.android.adapters;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.binding.DataBoundAdapter;
import com.daimler.biziz.android.binding.DataBoundViewHolder;
import com.daimler.biziz.android.databinding.ItemMeetingRoomBinding;
import com.daimler.biziz.android.databinding.ItemSearchRoomBinding;
import com.daimler.biziz.android.network.entity.place.MeetingRoom;

import java.util.List;

public class MeetingRoomSearchAdapter extends DataBoundAdapter<ItemSearchRoomBinding> {
    private final RoomCalback callback;
    List<MeetingRoom> list;

    public MeetingRoomSearchAdapter(List<MeetingRoom> list, RoomCalback calback) {
        super(R.layout.item_search_room);
        this.list = list;
        this.callback = calback;
    }


    @Override
    protected void bindItem(DataBoundViewHolder<ItemSearchRoomBinding> holder, int position, List<Object> payloads) {

        holder.binding.setData(list.get(position));
        holder.binding.setCallback(callback);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public interface RoomCalback {
        void onItemClick(MeetingRoom room);
    }
}
