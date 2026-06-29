# MbtBizizAndroid — Android Application

> **Part of the MBT BizIZ monorepo.** See the [top-level README](../README.md) for the full system overview.

The Android application is the native Android client of the **MBT BizIZ** platform, built with **Java/Kotlin** and distributed as an enterprise in-house app to Mercedes-Benz Türk A.Ş. employees.

---

## Table of Contents

1. [Summary](#1-summary)
2. [Project Structure](#2-project-structure)
3. [Build Configuration & Flavors](#3-build-configuration--flavors)
4. [Technology Stack & Dependencies](#4-technology-stack--dependencies)
5. [Architecture](#5-architecture)
6. [Authentication — MSAL (Azure AD)](#6-authentication--msal-azure-ad)
7. [Screens & Features](#7-screens--features)
8. [Network Layer](#8-network-layer)
9. [Push Notifications (Firebase)](#9-push-notifications-firebase)
10. [Signing & Distribution](#10-signing--distribution)
11. [Building & Running](#11-building--running)

---

## 1. Summary

- Native Android app in **Java** (main code) and **Kotlin** (build scripts)
- Three build flavors: `dev`, `staging`, `prod`
- Authentication via **MSAL 5.6.0** (Microsoft Authentication Library for Azure AD)
- Network layer using **Retrofit2 + OkHttp3**
- Three main navigation tabs: **Portal** (news/content), **Notifications**, **Orchestra** (profile/work hours/payslip)
- **Firebase Cloud Messaging** for push notifications
- Minimum Android 7.0 (SDK 24), Target SDK 33

---

## 2. Project Structure

```
MbtBizizAndroid/
├── settings.gradle.kts
├── build.gradle.kts                   ← Root Gradle config
├── gradle.properties
├── gradlew / gradlew.bat
├── app/
│   ├── build.gradle.kts               ← App-level Gradle with flavors, signing, dependencies
│   ├── proguard-rules.pro
│   ├── signKey                        ← Enterprise keystore file
│   ├── google-services.json           ← Firebase config (not shown in tree)
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/daimlertruck/dtag/internal/android/mbt/test/
│       │   │   ├── adapters/          ← RecyclerView adapters
│       │   │   ├── base/              ← Base classes (BaseActivity, BaseApplication, etc.)
│       │   │   ├── binding/           ← Data binding adapters
│       │   │   ├── bindingFunctions/  ← BindingAdapter utilities
│       │   │   ├── di/                ← Dagger 2 dependency injection
│       │   │   │   ├── components/    ← AppComponent
│       │   │   │   ├── module/        ← NetworkModule, AppModule
│       │   │   │   ├── qualifiers/
│       │   │   │   └── scopes/
│       │   │   ├── firebase/          ← FirebaseMessagingService
│       │   │   ├── interfaces/        ← Common interfaces
│       │   │   ├── manager/           ← Singleton managers
│       │   │   ├── model/             ← Data models
│       │   │   ├── network/
│       │   │   │   ├── entity/        ← Request/response entities (one folder per API)
│       │   │   │   └── network/       ← APIService, ApiUtils, interceptors
│       │   │   ├── receiver/          ← BroadcastReceivers
│       │   │   ├── ui/                ← All UI screens (activities/fragments)
│       │   │   │   ├── about/
│       │   │   │   ├── birthday/
│       │   │   │   ├── dialogs/
│       │   │   │   ├── foodMenu/
│       │   │   │   ├── fullscreenimage/
│       │   │   │   ├── htmlview/
│       │   │   │   ├── login/
│       │   │   │   ├── main/
│       │   │   │   │   ├── main/       ← MainActivity, tab setup
│       │   │   │   │   ├── notification/ ← NotificationFragment
│       │   │   │   │   ├── orchestra/  ← OrchestraFragment, Payslip OTP/Period
│       │   │   │   │   └── portal/     ← PortalFragment (main news/content hub)
│       │   │   │   ├── news/
│       │   │   │   ├── newsDetail/
│       │   │   │   ├── residential/
│       │   │   │   ├── settings/
│       │   │   │   ├── socialmedia/
│       │   │   │   ├── splash/
│       │   │   │   ├── toolbar/
│       │   │   │   ├── transportation/ ← Shuttle schedule
│       │   │   │   ├── usefullinks/
│       │   │   │   ├── usefullinkscategory/
│       │   │   │   └── usefullinkscategorydetail/
│       │   │   ├── uiControls/        ← Custom UI views
│       │   │   ├── utils/             ← Utilities/helpers
│       │   │   └── viewmodel/         ← Shared ViewModels
│       │   └── res/                   ← Layouts, drawables, strings, etc.
│       ├── dev/                       ← Dev flavor overrides
│       ├── staging/                   ← Staging flavor overrides
│       └── prod/                      ← Prod flavor overrides
└── gradle/wrapper/
```

---

## 3. Build Configuration & Flavors

### Flavors

| Flavor | Application ID | App Name | Backend |
|---|---|---|---|
| `dev` | `com.daimlertruck.dtag.internal.android.mbt.test2` | MBT App Dev | `http://10.0.2.2:8000/api/v1/` (Android emulator localhost) |
| `staging` | `com.daimlertruck.dtag.internal.android.mbt.test2` | MBT App Staging | `https://biziapp-test.app.daimlertruck.com/bizizBackend/public/index.php/api/v1/` |
| `prod` | `com.daimlertruck.dtag.internal.android.mbt.test` | MBT App | `https://bizizapp.com/bizizBackend/public/index.php/api/v1/` |

### Version

| Property | Value |
|---|---|
| Version Name | `1.6.0` |
| Version Code | `48` |
| Min SDK | 24 (Android 7.0 Nougat) |
| Target SDK | 33 (Android 13) |
| Compile SDK | 34 |
| Java Compatibility | Java 1.8 |

### Build Types

| Build Type | Minify | Shrink | Signing |
|---|---|---|---|
| `debug` | No | No | `signKey` (enterprise) |
| `release` | No | No | `signKey` (enterprise) |

> **Note:** Code minification and resource shrinking are both disabled. ProGuard rules file exists (`proguard-rules.pro`) but is not applied.

### OIDC Dev Bypass

In the `dev` flavor, MSAL can be bypassed with a hardcoded token:
```
OIDC_BYPASS_ENABLED = true
OIDC_BYPASS_TOKEN = "mbtbiziz-local-dev-bypass-token"
```
This allows local development without an Azure AD account.

---

## 4. Technology Stack & Dependencies

| Category | Library | Version |
|---|---|---|
| **Authentication** | MSAL (com.microsoft.identity.client) | 5.6.0 |
| **Network** | Retrofit2 | 2.3.0 |
| **Network** | OkHttp3 | 3.9.0 |
| **Serialization** | Gson | 2.7 |
| **Dependency Injection** | Dagger 2 (dagger-android) | 2.14.1 |
| **Architecture** | ViewModel + LiveData (lifecycle-extensions) | 2.2.0 |
| **UI Data Binding** | ViewBinding + DataBinding | Built-in |
| **Image Loading** | Picasso | 2.71828 |
| **Image Slideshow** | denzcoskun/ImageSlideshow | 0.1.2 |
| **Photo Zoom** | chrisbanes/PhotoView | 2.0.0 |
| **Maps** | Google Play Services Maps | 18.2.0 |
| **Push Notifications** | Firebase BOM | 33.2.0 |
| **Push Notifications** | firebase-messaging | (from BOM) |
| **Analytics** | firebase-analytics | (from BOM) |
| **Splash Screen** | androidx.core:core-splashscreen | 1.0.1 |
| **UI** | Material Components | 1.11.0 |

---

## 5. Architecture

The app follows a **MVVM (Model-View-ViewModel)** pattern with **Dagger 2** for dependency injection.

### Navigation

The main app has a **3-tab bottom navigation** using a `ViewPager` + `TabLayout`:

```
MainActivity (ViewPager with TabLayout)
├── Tab 0: PortalFragment       ← News, Discounts, Food, Shuttle, Maps, etc.
├── Tab 1: NotificationFragment ← Push notification inbox
└── Tab 2: OrchestraFragment    ← Profile, Work Hours, Calendar, Payslip, Digital Card
```

### Key Layers

| Layer | Package | Description |
|---|---|---|
| **UI** | `ui/` | Activities and Fragments (View layer) |
| **ViewModel** | `viewmodel/`, `ui/*/VM*.java` | Business logic & LiveData |
| **Network** | `network/network/` | Retrofit service + interceptors |
| **Entity** | `network/entity/` | Request/response POJOs |
| **DI** | `di/` | Dagger components and modules |
| **Base** | `base/` | BaseActivity, BaseFragment, BaseApplication |

### Dependency Injection (Dagger 2)

- `AppComponent` — root component
- `NetworkModule` — provides `Retrofit`, `OkHttpClient`, `APIService`
- `AppModule` — provides application-level dependencies
- `@PerActivity` / `@PerFragment` scopes for lifecycle-aware bindings

---

## 6. Authentication — MSAL (Azure AD)

Authentication uses Microsoft Authentication Library (MSAL) 5.6.0 for Azure Active Directory.

### MSAL Configuration

| Parameter | Prod | Staging | Dev |
|---|---|---|---|
| Client ID | From prod MSAL config | From staging config | Bypassed |
| OIDC Scope | `api://48252d22-0987-4d84-b1d9-00468ec9d424/Read` | `api://910155c2-0cc9-4d21-a48b-4c49c99f8128/Read` | N/A |
| Azure Tenant | `505cca53-5750-4134-9501-8d52d5df3cd1` | Same | N/A |
| Android Client ID | `bc50644f-7099-4737-bcd1-a2791ba4930b` | — | — |

### Auth Flow

1. App launches → `SplashActivity` checks for cached MSAL token
2. If no valid token → redirect to `LoginActivity` → MSAL interactive login
3. Azure AD returns access token → stored in MSAL token cache
4. Token sent as `Authorization: Bearer <token>` with every API call via `LoginInterceptor`
5. On token expiry, MSAL silently refreshes the token

### Login Interceptor (`LoginInterceptor.java`)

OkHttp interceptor that attaches the Bearer token from MSAL cache to every outgoing request.

---

## 7. Screens & Features

### Portal Tab (`PortalFragment`)

The main home screen. Shows a menu grid/list linking to all features. Every menu item click is tracked via `POST /api/v1/menuIncrement`.

| Feature Screen | Activity/Fragment | API Endpoint |
|---|---|---|
| News | `NewsActivity` | `POST /newsList` |
| News Detail | `NewsDetailActivity` | `GET /newsDetail/{id}` |
| Food Menu | `FoodMenuActivity` | `GET /foodMenu` |
| Birthdays | `BirthdayActivity` | `GET /birthdayList` |
| Transportation/Shuttle | `TransportationActivity` | `GET /shuttleOptionList`, `POST /shuttleList` |
| Useful Links / Phones | `UsefulLinksActivity` | `GET /phoneLocs`, `GET /phones/{id}` |
| Social Clubs | `UsefulLinksCategoryActivity` | `GET /socialClubLocs`, `GET /socialClubs/{id}` |
| Social Media | `SocialMediaActivity` | `GET /medias` |
| Residential/Maps | `ResidentialActivity` | `GET /maps` |
| HTML View | `HtmlActivity` | (in-app browser) |
| KVKK | `KvkkActivity` | (static HTML) |

### Notification Tab (`NotificationFragment`)

- Displays paginated list of push notifications
- Supports pull-to-refresh
- Tapping a notification with `newsId` navigates to `NewsDetailActivity`
- Badge count fetched via `GET /notificationBadgeCount`
- Notifications can be deleted (soft-delete)

### Orchestra Tab (`OrchestraFragment`)

Employee self-service features:

| Feature | Screen | Description |
|---|---|---|
| Profile | In fragment | Employee info (name, title, department, location) |
| Work Hours | `FlexibleWorkingActivity` | Yearly work hours from SAP |
| Work Hours Detail | `FlexibleWorkingDetailActivity` | Monthly breakdown |
| Work Calendar | `WorkingScheduleActivity` | Work schedule calendar view |
| Payslip (Bordro) | `PayslipOtpActivity` + `PayslipPeriodActivity` | OTP flow → period selection → PDF viewer |
| Digital Business Card | In fragment | Activate/deactivate, copy share URL |
| Settings | `SettingsActivity` | Notification preferences toggle |

### Payslip Flow (Android)

```
User taps Payslip
  → GET /payslip/isActive  (check if feature is enabled)
  → POST /payslip/request-otp  (send OTP SMS)
  → PayslipOtpActivity
      → User enters 6-digit OTP (60-second countdown timer)
      → POST /payslip/verify-otp
  → PayslipPeriodActivity
      → User selects year/month
      → POST /payslip/fetch  (returns Base64 PDF)
      → PDF rendered in-app
```

OTP resend cooldown: **30 seconds**.

---

## 8. Network Layer

### APIService (Retrofit interface)

All API calls are defined in `APIService.java`. Key calls:

```java
@POST("checkPhone")           Call<BaseResponse> checkPhone(...)
@POST("login")                Call<BaseResponse<LoginEntity>> login(...)
@POST("newsList")             Call<BaseResponse<NewsEntity>> getNewsList(...)
@GET("newsDetail/{id}")       Call<BaseResponse<NewsDetailEntity>> getNewsDetail(...)
@GET("foodMenu")              Call<BaseResponse<FoodListEntity>> getFoodMenu(...)
@GET("profile")               Call<BaseResponse<ProfileEntity>> getProfile(...)
@GET("yearlyWorkHours/{year}")  Call<BaseResponse<FlexibleWorkEntity>> getYearlyWorkHours(...)
@POST("payslip/request-otp")  Call<BaseResponse> payslipRequestOtp()
@POST("payslip/verify-otp")   Call<BaseResponse> payslipVerifyOtp(...)
@POST("payslip/fetch")        Call<BaseResponse<PayslipEntity>> payslipFetch(...)
```

### Response Envelope

All API responses follow a consistent envelope:
```json
{
  "statusCode": 200,
  "responseData": { ... },
  "errorMessage": null
}
```
Mapped to `BaseResponse<T>`.

### Network Utilities

- `ApiUtils.java` — Creates the Retrofit instance with `BASE_URL` from `BuildConfig`
- `AbstractApiUtils.java` — Base class for making calls with a `NetworkCallback`
- `MockApiUtils.java` — Mock implementation for offline/testing
- `LoginInterceptor.java` — Adds `Authorization: Bearer` header to all requests

---

## 9. Push Notifications (Firebase)

- Uses **Firebase Cloud Messaging (FCM)**
- `FirebaseMessagingService` handles incoming messages
- Device token is registered with backend via `POST /saveDeviceInfo` after MSAL login
- Device token is deregistered via `POST /deleteDeviceInfo` on sign-out
- Tapping a notification with a `newsId` payload navigates to `NewsDetailActivity`
- Android 13+ notification permission is requested at runtime (`POST_NOTIFICATIONS`)

`google-services.json` must be present in `app/` for Firebase to work (file not committed to repo).

---

## 10. Signing & Distribution

### Keystore

| Property | Value |
|---|---|
| Keystore File | `app/signKey` |
| Key Alias | `keyMbt` |
| Passwords | Configured in `build.gradle.kts` signing configs |

The same signing config (`enterprise`) is used for both `debug` and `release` build types and all flavors.

> **Security Note:** Keystore passwords are embedded in `build.gradle.kts`. For production, move these to environment variables or a secrets manager.

### Distribution

The APK is built with Gradle and uploaded via SFTP using `release.sh`:

```bash
# Build and upload prod APK
./release.sh --platform android --env prod

# Build and upload staging APK
./release.sh --platform android --env staging
```

Output artifact: `release_artifacts/android/mbt_app.apk`

---

## 11. Building & Running

### Prerequisites

- Android Studio (Hedgehog or later recommended)
- JDK 8+
- Android SDK 34
- Google Services file (`google-services.json`) placed in `app/`
- (For MSAL) Azure AD app registration configured for the device

### Build Commands

```bash
cd MbtBizizAndroid

# Build debug APK (dev flavor)
./gradlew assembleDevDebug

# Build release APK (staging flavor)
./gradlew assembleStagingRelease

# Build release APK (prod flavor)
./gradlew assembleProdRelease

# Install on connected device
./gradlew installDevDebug
```

### Local Development Setup

1. Start the backend locally on port 8000 (`php artisan serve --port=8000`)
2. Use the `dev` flavor — it points to `http://10.0.2.2:8000/api/v1/` (Android emulator → host machine)
3. OIDC bypass is enabled in `dev` flavor — no Azure AD account needed for basic testing

### Running Tests

```bash
./gradlew test                    # Unit tests
./gradlew connectedAndroidTest    # Instrumented tests (device required)
```
