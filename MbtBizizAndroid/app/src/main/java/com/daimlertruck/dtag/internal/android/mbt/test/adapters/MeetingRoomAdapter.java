package com.daimlertruck.dtag.internal.android.mbt.test.adapters;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundViewHolder;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.ItemMeetingRoomBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.place.MeetingRoom;

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
