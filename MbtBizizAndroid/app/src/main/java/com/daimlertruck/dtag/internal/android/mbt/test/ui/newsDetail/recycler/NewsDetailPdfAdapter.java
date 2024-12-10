package com.daimlertruck.dtag.internal.android.mbt.test.ui.newsDetail.recycler;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundViewHolder;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.ItemPdfListBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.newsDetail.NewsDetailPdf;

import java.util.ArrayList;
import java.util.List;

public class NewsDetailPdfAdapter extends DataBoundAdapter<ItemPdfListBinding> {
    private List<NewsDetailPdf> list = new ArrayList<>();
    private final NewsDetailPdfItemCallback pdfItemCallback;

    public NewsDetailPdfAdapter(List<NewsDetailPdf> list, NewsDetailPdfItemCallback itemCallback) {
        super(R.layout.item_pdf_list);
        this.list = list;
        this.pdfItemCallback = itemCallback;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    protected void bindItem(DataBoundViewHolder<ItemPdfListBinding> holder, int position, List<Object> payloads) {
        holder.binding.setData(list.get(position));
        holder.binding.setCallback(pdfItemCallback);
    }

    public interface NewsDetailPdfItemCallback {
        void onItemClick(String pdfUrl);
    }
}
