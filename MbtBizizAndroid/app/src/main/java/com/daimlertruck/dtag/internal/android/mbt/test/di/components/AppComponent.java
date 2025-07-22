package com.daimlertruck.dtag.internal.android.mbt.test.di.components;

import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.di.module.ApiUtilsModule;
import com.daimlertruck.dtag.internal.android.mbt.test.di.module.NetworkModule;
import com.daimlertruck.dtag.internal.android.mbt.test.di.module.SharedPreferenceManagerModule;
import com.daimlertruck.dtag.internal.android.mbt.test.di.module.SplashActivityModule;
import com.daimlertruck.dtag.internal.android.mbt.test.di.module.TokenManagerModule;
import com.daimlertruck.dtag.internal.android.mbt.test.di.scopes.ApplicationScope;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.SharedPreferenceManager;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.TokenManager;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.about.AboutDetailFragment;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.about.AboutFragment;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.about.appfeedback.AppFeedbackFragment;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.about.appfeedback.VMAppFeedback;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.birthday.VMBirthday;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.foodMenu.VMFoodMenu;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.login.LoginActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.login.LoginFragment;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.login.TermsAndConFragment;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.login.VMLogin;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.login.VMSms;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.main.VMMain;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.notification.VMNotification;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.OrchestraFragment;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.VmOrchestra;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.flexibleWorkingInfo.VMFlexibleWorking;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.portal.PortalFragment;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.portal.VMPortal;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.news.VMNews;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.schedule.VMWorkingScheduleFragment;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.newsDetail.VMNewsDetail;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.residential.VMResidential;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.settings.VMSettings;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.socialmedia.VMSocialMedia;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.splash.VMSplash;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.transportation.VMShuttleOption;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.transportation.VMTransportation;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.splash.SplashActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.transportation.search.SearchShuttleActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.transportation.search.VMSearchShuttle;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinkscategory.UsefulLinksCategoryFragment;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinkscategory.VMUsefulLinksCategory;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinks.UsefulLinksPortalFragment;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinks.VMUsefulLinksPortal;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinkscategorydetail.UsefulLinksCategoryDetailFragment;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinkscategorydetail.VMUsefulLinksCategoryDetail;

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

    void inject(OrchestraFragment orchestraFragment);

    void inject(AboutDetailFragment aboutDetailFragment);

    void inject(AboutFragment aboutFragment);

    void injectVMResidential(VMResidential vmResidential);

    void injectVMNotifications(VMNotification vmNotification);

    void injectMainVM(VMMain vmMain);

    void injectVMPortal(VMPortal vmPortal);

    void injectPortalFragment(PortalFragment portalFragment);

    void injectSplashVM(VMSplash vmSplash);

    void injectUsefulLinksPortalFragment(UsefulLinksPortalFragment usefulLinksPortalFragment);

    void injectVMUsefulLinks(VMUsefulLinksPortal vmPortal);

    void injectUsefulLinksCategoryFragment(UsefulLinksCategoryFragment usefulLinksCategoryFragment);
    void injectVMUsefulLinksCategory(VMUsefulLinksCategory vmUsefulLinksCategory);

    void injectUsefulLinksCategoryDetailFragment(UsefulLinksCategoryDetailFragment usefulLinksCategoryDetailFragment);
    void injectVMUsefulLinksCategoryDetail(VMUsefulLinksCategoryDetail vmUsefulLinksCategoryDetail);

    void injectAppFeedbackFragment(AppFeedbackFragment appFeedbackFragment);

    void injectVMAppFeedback(VMAppFeedback vmAppFeedback);

    void injectVMSocialMedia(VMSocialMedia vmSocialMedia);

    void inject(LoginFragment loginFragment);
    //Fragment Injection

    void inject(BaseApplication application);  // Add this method

    //Glide Injection
    //void injectCustomGlideModule(CustomGlideModule customGlideModule);


}
