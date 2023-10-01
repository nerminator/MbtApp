package com.daimler.biziz.android.ui.main.notification;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.adapters.NotificationAdapter;
import com.daimler.biziz.android.databinding.FragmentNotificationBinding;
import com.daimler.biziz.android.network.entity.notifications.Notification;
import com.daimler.biziz.android.ui.about.AboutActivity;
import com.daimler.biziz.android.ui.newsDetail.NewsDetailActivity;
import com.daimler.biziz.android.ui.toolbar.VMToolbar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment implements NotificationAdapter.NotificationItemCallback {
    Integer page = 0;
    private List<Notification> notifications = new ArrayList<>();
    private VMNotification vmNotification;
    private NotificationAdapter adapter;
    private FragmentNotificationBinding binding;
    private int totalItemCount;
    private int lastVisibleItem;
    private int visibleThreshold = 10;
    private boolean isFinish = false;

    public static NotificationFragment newInstance() {

        Bundle args = new Bundle();

        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false);
        vmNotification = ViewModelProviders.of(getActivity()).get(VMNotification.class);
        binding.setViewmodel(vmNotification);
        binding.setLifecycleOwner(this);

        if (notifications.size() == 0)
            vmNotification.getNotifications(page);
        setAdapter();
        listenRecycler();
        observe();
        binding.imgInfo.setOnClickListener(v -> AboutActivity.start(getContext()));
        return binding.getRoot();
    }


    private void setAdapter() {
        if (adapter == null) {
            adapter = new NotificationAdapter(notifications, this);
            binding.recyclerNotification.setAdapter(adapter);
        } else if (binding.recyclerNotification.getAdapter() == null) {
            binding.recyclerNotification.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void observe() {
        vmNotification.data.observe(this, notifications -> {
            if (notifications != null) {
                if (notifications.size() < 20) isFinish = true;
                this.notifications.addAll(notifications);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onNotificationItemClick(Notification notification) {

        NewsDetailActivity.start(getContext(), notification.getNewsId().toString());
    }

    private void listenRecycler() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) binding.recyclerNotification
                .getLayoutManager();


        binding.recyclerNotification
                .addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView,
                                           int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        if (isFinish) return;

                        totalItemCount = linearLayoutManager.getItemCount();
                        lastVisibleItem = linearLayoutManager
                                .findLastVisibleItemPosition();
                        if (!vmNotification.loading.getValue()
                                && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                            // End has been reached
                            // Do something
                            page++;
                            vmNotification.getNotifications(page);
                        }
                    }
                });
    }
}
