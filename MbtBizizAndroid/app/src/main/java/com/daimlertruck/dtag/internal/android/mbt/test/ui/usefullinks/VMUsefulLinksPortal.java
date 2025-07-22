package com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinks;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinks.recycler.UsefulLinksPortalItem;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinks.recycler.UsefulLinksPortalItemType;

import java.util.ArrayList;

public class VMUsefulLinksPortal extends AndroidViewModel {

    public VMUsefulLinksPortal(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMUsefulLinks(this);

    }

    public ArrayList<UsefulLinksPortalItem> getPortalList() {
        ArrayList<UsefulLinksPortalItem> portalItems = new ArrayList<>();
        portalItems.add(new UsefulLinksPortalItem(R.drawable.ic_ul_social_clubs, R.drawable.img_ul_social_clubs, R.string.TXT_LINKS_TITLE1, UsefulLinksPortalItemType.SOCIAL_CLUBS));
        portalItems.add(new UsefulLinksPortalItem(R.drawable.ic_ul_internal_ads, R.drawable.img_ul_internal_ads, R.string.TXT_LINKS_TITLE2, UsefulLinksPortalItemType.INTERNAL_ADS));
        portalItems.add(new UsefulLinksPortalItem(R.drawable.ic_ul_bday, R.drawable.img_ul_bday, R.string.TXT_LINKS_TITLE3, UsefulLinksPortalItemType.BIRTHDAYS));
        portalItems.add(new UsefulLinksPortalItem(R.drawable.ic_ul_sapconcur, R.drawable.img_ul_sapconcur, R.string.TXT_LINKS_TITLE4, UsefulLinksPortalItemType.SAP_CONCUR));
        portalItems.add(new UsefulLinksPortalItem(R.drawable.ic_ul_mealbox, R.drawable.img_ul_mealbox, R.string.TXT_LINKS_TITLE5, UsefulLinksPortalItemType.MEALBOX));
        portalItems.add(new UsefulLinksPortalItem(R.drawable.ic_ul_orchestra, R.drawable.img_ul_orchestra, R.string.TXT_LINKS_TITLE6, UsefulLinksPortalItemType.ORCHESTRA));
        portalItems.add(new UsefulLinksPortalItem(R.drawable.ic_ul_improvement, R.drawable.img_ul_improvement, R.string.TXT_LINKS_TITLE7, UsefulLinksPortalItemType.IMPROVEMENT_SYSTEM));
        portalItems.add(new UsefulLinksPortalItem(R.drawable.ic_ul_oracle_hcm, R.drawable.img_ul_oracle_hcm, R.string.TXT_LINKS_TITLE8, UsefulLinksPortalItemType.ORACLE_HCM));
        portalItems.add(new UsefulLinksPortalItem(R.drawable.ic_ul_social_media, R.drawable.img_ul_social_media, R.string.TXT_LINKS_TITLE9, UsefulLinksPortalItemType.SOCIAL_MEDIA));
        return portalItems;
    }
}
