package com.daimler.biziz.android.di.components;

import com.daimler.biziz.android.di.module.ApiUtilsModule;
import com.daimler.biziz.android.di.module.NetworkModule;
import com.daimler.biziz.android.di.module.SharedPreferenceManagerModule;
import com.daimler.biziz.android.di.module.SplashActivityModule;
import com.daimler.biziz.android.di.module.TokenManagerModule;
import com.daimler.biziz.android.di.scopes.ApplicationScope;
import com.daimler.biziz.android.manager.SharedPreferenceManager;
import com.daimler.biziz.android.manager.TokenManager;
import com.daimler.biziz.android.network.network.AbstractApiUtils;
import com.daimler.biziz.android.ui.birthday.VMBirthday;
import com.daimler.biziz.android.ui.foodMenu.VMFoodMenu;
import com.daimler.biziz.android.ui.login.LoginActivity;
import com.daimler.biziz.android.ui.login.TermsAndConFragment;
import com.daimler.biziz.android.ui.login.VMLogin;
import com.daimler.biziz.android.ui.login.VMSms;
import com.daimler.biziz.android.ui.main.main.VMMain;
import com.daimler.biziz.android.ui.main.notification.VMNotification;
import com.daimler.biziz.android.ui.main.orchestra.VmOrchestra;
import com.daimler.biziz.android.ui.main.orchestra.flexibleWorkingInfo.VMFlexibleWorking;
import com.daimler.biziz.android.ui.main.portal.PortalFragment;
import com.daimler.biziz.android.ui.main.portal.VMPortal;
import com.daimler.biziz.android.ui.news.VMNews;
import com.daimler.biziz.android.ui.main.orchestra.schedule.VMWorkingScheduleFragment;
import com.daimler.biziz.android.ui.newsDetail.VMNewsDetail;
import com.daimler.biziz.android.ui.residential.VMResidential;
import com.daimler.biziz.android.ui.settings.VMSettings;
import com.daimler.biziz.android.ui.transportation.VMShuttleOption;
import com.daimler.biziz.android.ui.transportation.VMTransportation;
import com.daimler.biziz.android.ui.splash.SplashActivity;
import com.daimler.biziz.android.ui.transportation.search.SearchShuttleActivity;
import com.daimler.biziz.android.ui.transportation.search.VMSearchShuttle;

import dagger.Component;



@ApplicationScope
@Component(modules = {
        SharedPreferenceManagerModule.class,
        TokenManagerModule.class,
        ApiUtilsModule.class,
        NetworkModule.class,
        SplashActivityModule.class
})
public interface AppComponent {
    AbstractApiUtils apiUtils();

    TokenManager tokeManager();

    SharedPreferenceManager sharedPreferenceManager();

    //ViewModel Injection
    void injectVMLogin(VMLogin VMLogin);

    void injectVMSms(VMSms VMSms);

    void injectVMNewsDetail(VMNewsDetail vmNewsDetail);

    void injectVMBirthday(VMBirthday vmBirthday);

    void injectVMFoodMenu(VMFoodMenu vmFoodMenu);

    void injectVMFlexibleWorking(VMFlexibleWorking vmFlexibleWorking);

    void injectVMOrchestra(VmOrchestra vmOrchestra);

    void injectVMNews(VMNews vmNews);

    void injectVMWorkingSchedule(VMWorkingScheduleFragment vmWorkingScheduleFragment);

    void injectVMTransportation(VMTransportation vmTransportation);

    void injectVMShuttleOption(VMShuttleOption vmShuttleOption);

    void injectVMSearchShuttle(VMSearchShuttle vmSearchShuttle);

    void injectVMSettings(VMSettings vmSettings);

    //Activity Injection
    void inject(LoginActivity loginActivity);

    void injectSearchShuttleActivity(SearchShuttleActivity searchShuttleActivity);

    //Activity Injection

    //Fragment Injection
    void inject(TermsAndConFragment TermsAndCondFragment);

    void inject(SplashActivity splashActivity);

    void injectVMResidential(VMResidential vmResidential);

    void injectVMNotifications(VMNotification vmNotification);

    void injectMainVM(VMMain vmMain);

    void injectVMPortal(VMPortal vmPortal);

    void injectPortalFragment(PortalFragment portalFragment);


    //Fragment Injection


    //Glide Injection
    //void injectCustomGlideModule(CustomGlideModule customGlideModule);


}
