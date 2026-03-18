package com.daimlertruck.dtag.internal.android.mbt.test.adapters;

import android.content.Context;
import androidx.databinding.BindingAdapter;
import android.view.View;
import android.widget.Switch;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundViewHolder;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ItemSettingsBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.settings.ISettingsChange;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.settings.SettingObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atakankersit on 25/06/2017.
 */

public class SettingsAdapter extends DataBoundAdapter<ItemSettingsBinding> {
    private ArrayList<SettingObject> list = new ArrayList<>();
    private Context context;
    private ISettingsChange iSettingsChange;

    public SettingsAdapter(ArrayList<SettingObject> list, Context context, ISettingsChange iSettingsChange) {
        super(R.layout.item_settings);
        this.list = list;
        this.context = context;
        this.iSettingsChange = iSettingsChange;
    }

    @Override
    protected void bindItem(DataBoundViewHolder<ItemSettingsBinding> holder, int position, List<Object> payloads) {
        holder.binding.setData(list.get(position));
        holder.binding.setSettingchangedcallback(iSettingsChange);

        if (position == list.size() - 1) {
            holder.binding.viewSeperator.setVisibility(View.GONE);
        } else holder.binding.viewSeperator.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @BindingAdapter({"bind:callbackswitcpri", "bind:data"})
    public static void callbackswitcpri(Switch view, final ISettingsChange iSettingObjectSwitch, final SettingObject settingObject) {
        view.setOnCheckedChangeListener((buttonView, isChecked) -> iSettingObjectSwitch.onSwitch(settingObject));
    }
}
