package com.daimler.biziz.android.ui.residential;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.adapters.MeetingRoomSearchAdapter;
import com.daimler.biziz.android.base.BaseActivity;
import com.daimler.biziz.android.databinding.ActivitySearchRoomBinding;
import com.daimler.biziz.android.network.entity.place.Building;
import com.daimler.biziz.android.network.entity.place.MeetingRoom;
import com.daimler.biziz.android.network.entity.place.Residential;
import com.daimler.biziz.android.ui.about.AboutActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchRoomActivity extends BaseActivity<ActivitySearchRoomBinding> implements MeetingRoomSearchAdapter.RoomCalback {
    ArrayList<Residential> residentials;
    List<MeetingRoom> results = new ArrayList<>();
    MeetingRoomSearchAdapter adapter;

    public static void start(Context context, ArrayList<Residential> residentials) {
        Intent activity = new Intent(context, SearchRoomActivity.class);
        activity.putParcelableArrayListExtra("residentials", residentials);
        context.startActivity(activity);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_search_room;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        residentials = getIntent().getExtras().getParcelableArrayList("residentials");
        adapter = new MeetingRoomSearchAdapter(results, this);
        binding.recyclerSearchRoom.setAdapter(adapter);
        setListener();
    }

    private void setListener() {
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    search(s);
                } else {
                    results.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        binding.btnBack.setOnClickListener(v -> {
            finish();
        });

        binding.btnInfo.setOnClickListener(v -> {
            AboutActivity.start(this);
         });
    }

    private void search(Editable s) {
        results.clear();
        for (Residential residential : residentials) {
            for (Building building : residential.getBuildings()) {
                for (MeetingRoom room : building.getMeetingRoomList()) {
                    if (room.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                        room.setBuildName(building.getName());
                        results.add(room);
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(MeetingRoom room) {
        Building building = null;
        for (Residential residential : residentials) {
            for (Building bd : residential.getBuildings()) {
                if (bd.getMeetingRoomList().contains(room)) {
                    building = bd;
                    break;
                }
            }
        }
        if (building != null)
            ResidentialActivity.start(this, building, room);
    }
}
