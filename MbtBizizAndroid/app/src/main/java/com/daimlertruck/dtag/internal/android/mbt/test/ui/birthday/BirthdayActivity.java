package com.daimlertruck.dtag.internal.android.mbt.test.ui.birthday;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.adapters.BirthdayAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseActivity;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ActivityBirthdayBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.birthday.BirthdayListEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.about.AboutActivity;

import java.util.ArrayList;

public class BirthdayActivity extends BaseActivity<ActivityBirthdayBinding> {

    private com.daimlertruck.dtag.internal.android.mbt.test.ui.birthday.VMBirthday viewModel;
    private BirthdayAdapter adapter;
    private ArrayList<BirthdayListEntity> listEntities = new ArrayList<>();

    public static void start(Context context) {
        Intent starter = new Intent(context, BirthdayActivity.class);
        context.startActivity(starter);
    }


    @Override
    public int getLayoutRes() {
        return R.layout.activity_birthday;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(com.daimlertruck.dtag.internal.android.mbt.test.ui.birthday.VMBirthday.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        init();

    }

    private void init() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.TXT_LOGIN_NEWS_BIRTHDAY);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.btn_back);

        setToolbar();
        setAdapterRecycler();
        observe();
    }

    private void setAdapterRecycler() {
        adapter = new BirthdayAdapter(listEntities);
        binding.recycBirthday.setAdapter(adapter);
    }

    private void observe() {
        viewModel.getBirthdayLiveData().observe(this, birthdayEntity -> {
            listEntities.clear();
            listEntities.addAll(birthdayEntity.getBirthdayListEntity());
            adapter.notifyDataSetChanged();
        });

    }

    private void setToolbar() {
        /** Burada collapsingToolbarLayout ile ilgili düzenlemeleri yaptım
         * CollapsingToolbarLayout'un buton renklerini style'ına styles dosyasındaki 'ToolbarColoredBackArrow' satırlarını vererek Ev */
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            /** Toolbar text*/
            //binding.collapsingToolbar.setTitle(header);

            binding.collapsingToolbar.setTitleEnabled(false);

            binding.toolbar.setTitleTextColor(Color.WHITE);

            /** Toolbar text size ve text rengini styles dosyasını kullanarak ayarladım */
            binding.collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
            binding.collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

            /** Toolbar text fontu */
            final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/DaimlerCA-Regular.ttf");
            binding.collapsingToolbar.setCollapsedTitleTypeface(tf);
            binding.collapsingToolbar.setExpandedTitleTypeface(tf);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_detail_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_info:
                AboutActivity.start(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
