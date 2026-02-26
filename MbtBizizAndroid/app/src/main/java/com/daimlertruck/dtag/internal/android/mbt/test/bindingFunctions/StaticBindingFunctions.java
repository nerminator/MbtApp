package com.daimlertruck.dtag.internal.android.mbt.test.bindingFunctions;

import android.content.Context;
import androidx.databinding.BindingAdapter;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.flexibleWorkHours.FlexibleItemEntity;
import com.squareup.picasso.Picasso;

public class StaticBindingFunctions {

  /*  @BindingAdapter(value = {"itemData","textvalue"}, requireAll = false)
    public static void textWorkHours(TextView view, Boolean isYearliy,String textvalue) {
        if (!utils.isNullOrEmptyString(imageUrl))
            Picasso.with(view.getContext()).load(imageUrl).into(view);
    }*/


    @BindingAdapter("textWorkHours")
    public static void textWorkHours(TextView view, FlexibleItemEntity item) {
        if (!TextUtils.isEmpty(item.getDayText())) {
            view.setText(item.getDayText());
        } else {
            String[] custom_months = view.getContext().getResources().getStringArray(R.array.custom_months);
            view.setText(custom_months[item.getMonth() - 1]);
        }
    }

    @BindingAdapter("loadImage")
    public static void textWorkHours(ImageView view, String url) {
        //  if (url==null || url.isEmpty()) return;

        Picasso.get().load(url)
                .placeholder(R.drawable.place_holder_discount)
                //  .fit().centerCrop()
                .into(view);
    }

    @BindingAdapter("loadImageWithoutPlaceHolder")
    public static void loadImageWithoutPlaceHolder(ImageView view, String url) {
        Picasso.get().load(url).into(view);
    }

    @BindingAdapter("loadImageFromRes")
    public static void loadImageFromRes(ImageView view, Integer resId) {
        view.setImageResource(resId);
    }

    @BindingAdapter({"bind:loadTextFromId"})
    public static void loadTextFromId(TextView view, Integer textId) {
        view.setText(view.getContext().getString(textId));
    }

    @BindingAdapter({"bind:loadTextFromDiscountType"})
    public static void loadTextFromDiscountType(TextView view, Integer type) {
        view.setText(getTextByDiscountType(view.getContext(),type).toUpperCase());
    }


    public static String getTextByDiscountType(Context context, Integer type){
 /*       2 categoryNameEducation
        3 categoryNameEventAndEntertainment
        4 categoryNameHomeAndLife
        5 categoryNameGastronomy
        6 categoryNameRealEstate
        7 categoryNameClothing
        8 categoryNameAutomotive
        9 categoryNameHealth
        12 categoryNameTourism
        14 categoryNameTechnology
        13 categoryNameOther*/
        String result = "";
        switch (type) {
            case 2:
                result= context.getResources().getString(R.string.categoryNameEducation);
                break;
            case 3:
                result= context.getResources().getString(R.string.categoryNameEventAndEntertainment);
            break;
            case 4:
                result= context.getResources().getString(R.string.categoryNameHomeAndLife);
            break;
            case 5:
                result= context.getResources().getString(R.string.categoryNameGastronomy);
            break;
            case 6:
                result= context.getResources().getString(R.string.categoryNameRealEstate);
            break;
            case 7:
                result= context.getResources().getString(R.string.categoryNameClothing);
            break;
            case 8:
                result= context.getResources().getString(R.string.categoryNameAutomotive);
            break;
            case 9:
                result= context.getResources().getString(R.string.categoryNameHealth);
            break;
            case 12:
                result= context.getResources().getString(R.string.categoryNameTourism);
            break;
            case 14:
                result= context.getResources().getString(R.string.categoryNameTechnology);
            break;
            case 13:
                result= context.getResources().getString(R.string.categoryNameOther);
            break;
        }
        return result;
    }

}
