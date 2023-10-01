package com.daimler.biziz.android.adapters;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.binding.DataBoundAdapter;
import com.daimler.biziz.android.binding.DataBoundViewHolder;
import com.daimler.biziz.android.databinding.ItemBirthdayBinding;
import com.daimler.biziz.android.databinding.ItemMeetingRoomBinding;
import com.daimler.biziz.android.network.entity.birthday.BirthdayListEntity;
import com.daimler.biziz.android.network.entity.place.MeetingRoom;

import java.util.ArrayList;
import java.util.List;

public class MeetingRoomAdapter extends DataBoundAdapter<ItemMeetingRoomBinding> {
    List<MeetingRoom> list;

    public MeetingRoomAdapter(List<MeetingRoom> list) {
        super(R.layout.item_meeting_room);
        this.list = list;

    }


    @Override
    protected void bindItem(DataBoundViewHolder<ItemMeetingRoomBinding> holder, int position, List<Object> payloads) {

        holder.binding.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list==null ? 0 : list.size();
    }
}
