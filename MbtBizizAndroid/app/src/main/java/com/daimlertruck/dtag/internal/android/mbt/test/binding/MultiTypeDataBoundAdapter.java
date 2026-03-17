package com.daimlertruck.dtag.internal.android.mbt.test.binding;

import com.daimlertruck.dtag.internal.android.mbt.BR;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atakankersit on 07/02/2017.
 */

abstract public class MultiTypeDataBoundAdapter<T> extends BaseDataBoundAdapter {

    protected List<T> mItems = new ArrayList<>();

    public MultiTypeDataBoundAdapter(List<T> items) {
        mItems = items;
    }

    protected MultiTypeDataBoundAdapter() {
    }

    @Override
    protected void bindItem(DataBoundViewHolder holder, int position, List payloads) {
        //Todo: Multiple adapter'i kullanırsan burayı aç
        holder.binding.setVariable(BR.data, mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public T getItem(int position) {
        return mItems.get(position);
    }

    public void addItem(T item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public void addItem(int position, T item) {
        mItems.add(position, item);
        notifyItemInserted(position);
    }
}

