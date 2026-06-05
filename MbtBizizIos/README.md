# MbtBizizIos — iOS Application

> **Part of the MBT BizIZ monorepo.** See the [top-level README](../README.md) for the full system overview.

The iOS application is the native iOS client of the **MBT BizIZ** platform, built with **Swift** (and some Obj-C) and distributed as an enterprise in-house app to Mercedes-Benz Türk A.Ş. employees via Apple's Enterprise Program.

---

## Table of Contents

1. [Summary](#1-summary)
2. [Project Structure](#2-project-structure)
3. [Build Targets](#3-build-targets)
4. [Technology Stack & Dependencies (CocoaPods)](#4-technology-stack--dependencies-cocoapods)
5. [Architecture — VIP (Clean Swift)](#5-architecture--vip-clean-swift)
6. [Authentication — MSAL (Azure AD)](#6-authentication--msal-azure-ad)
7. [Screens & Features](#7-screens--features)
8. [Network Layer (Moya)](#8-network-layer-moya)
9. [Push Notifications (APNs)](#9-push-notifications-apns)
10. [Signing & Distribution](#10-signing--distribution)
11. [Building & Running](#11-building--running)

---

## 1. Summary

- Native iOS app in **Swift 4** with some Objective-C bridging
- **5 build targets**: `MBTBiziz-Dev`, `MBTBiziz-Staging`, `MBTBiziz-Production`, `MBTBordro-Staging`, `MBTBordro-Production`
- Authentication via **MSAL 1.2.5** (Microsoft Authentication Library for Azure AD)
- Network layer using **Moya** (abstraction over Alamofire)
- VIP (View-Interactor-Presenter) architecture pattern (Clean Swift)
- Enterprise In-House distribution — **no App Store**
- Minimum iOS 11.0

---

## 2. Project Structure

```
MbtBizizIos/
├── Podfile                              ← CocoaPods dependency definitions
├── MBT-Bridging-Header.h                ← ObjC-Swift bridge
├── create_bordro_targets.rb             ← Ruby script for creating Bordro targets
├── MBTBiziz.xcodeproj/                  ← Xcode project file
├── MBTBiziz.xcworkspace/                ← CocoaPods workspace (use this to open in Xcode)
├── Pods/                                ← CocoaPods dependencies (generated)
└── MBTBiziz/
    ├── AppDelegate.swift                ← App entry point, MSAL setup
    ├── Info.plist                       ← App configuration
    ├── MBTBiziz.entitlements            ← App entitlements (push, keychain groups)
    ├── MBT-Bridging-Header.h            ← Objective-C bridge header
    │
    ├── App/
    │   ├── Storyboards/                 ← Storyboard files
    │   └── ViewControllers/
    │       ├── Base/                    ← BaseViewController and shared base classes
    │       ├── Login/
    │       │   ├── OidcViewController.swift    ← MSAL OIDC login screen
    │       │   ├── ActivationCode/             ← OTP entry screen
    │       │   └── Welcome/                    ← Welcome/onboarding screen
    │       ├── Notification/            ← Notification inbox
    │       ├── Other/
    │       │   ├── About/               ← About Us screen
    │       │   ├── Popups/              ← Update popup dialogs
    │       │   └── TermOfUse/           ← Terms of use / KVKK
    │       ├── Profile/
    │       │   ├── Profile/             ← Employee profile
    │       │   ├── Settings/            ← Notification settings
    │       │   ├── FlexibleWork/        ← Yearly work hours
    │       │   ├── FlexibleWorkDetail/  ← Monthly work hours detail
    │       │   └── WorkCalendar/        ← Work calendar / shift view
    │       └── Portal/
    │           ├── Home/                ← Main portal home (VIP: HomeViewController, HomeInteractor, HomePresenter, HomeRouter, HomeWorker)
    │           ├── Menu/                ← Portal menu screen
    │           ├── News/
    │           │   ├── NewsList/        ← News list screen
    │           │   ├── NewsDetail/      ← News detail screen
    │           │   ├── BirthdayList/    ← Birthday list
    │           │   └── Cells/          ← News cell components
    │           ├── Links/               ← Useful links / phone directory / social clubs
    │           ├── Location/            ← Maps/campus locations
    │           └── Transportation/      ← Shuttle schedule
    │
    ├── Network/
    │   ├── NetworkAPI/
    │   │   ├── NetworkAPI.swift         ← API enum (all endpoints as cases)
    │   │   ├── NetworkAPI+TargetType.swift  ← Moya TargetType conformance (paths, methods, params)
    │   │   └── NetworkAPI+Protocols.swift   ← Protocol definitions
    │   ├── Provider/                    ← Moya provider setup
    │   ├── ResponseClasses/             ← Decodable response models
    │   └── Helpers/                     ← Network helpers
    │
    ├── Utilities/
    │   ├── Constants/                   ← App-wide constants
    │   ├── 3rdParty/                    ← Vendored 3rd-party code
    │   ├── BaseUIs/                     ← Base UI components
    │   ├── CustomUIs/                   ← Custom UI controls
    │   ├── Extensions/                  ← Swift extensions
    │   ├── Helpers/                     ← Utility helpers
    │   ├── Managers/
    │   │   ├── MBTDataManager.swift     ← Shared data manager / state
    │   │   ├── MBTLanguageManager.swift ← Localization manager
    │   │   ├── MBTNotificationManager.swift ← Push notification manager
    │   │   └── MBTUpdateManager.swift   ← App update manager
    │   └── Singletons/                  ← Singleton services
    │
    ├── Assets.xcassets/                 ← Images, colors, app icons
    ├── Resources/                       ← Fonts, JSON mock data, misc resources
    ├── Base.lproj/                      ← Base localization
    └── tr-TR.lproj/                     ← Turkish localization
```

---

## 3. Build Targets

The project has **5 build targets**, all sharing the same source code but with different configurations:

| Target | Bundle ID | Environment | Purpose |
|---|---|---|---|
| `MBTBiziz-Dev` | `com.daimlertruck.dtag.internal.ios.mbt.dev` | Development | Local development (no MSAL, localhost backend) |
| `MBTBiziz-Staging` | `com.daimlertruck.dtag.internal.ios.mbt.test2` | Staging | Pre-release testing |
| `MBTBiziz-Production` | `com.daimlertruck.dtag.internal.ios.mbt.test` | Production | Live app for all employees |
| `MBTBordro-Staging` | `com.daimlertruck.dtag.internal.ios.mbt.test2` | Staging | Standalone payslip app – staging |
| `MBTBordro-Production` | `com.daimlertruck.dtag.internal.ios.mbt.test` | Production | Standalone payslip app – production |

### Environment Detection

The app detects its environment at runtime from the executable name / bundle identifier:

```swift
static var current: AppEnvironment {
    let executableName = Bundle.main.object(forInfoDictionaryKey: "CFBundleExecutable") as? String ?? ""
    if executableName.contains("-Dev") { return .dev }
    if executableName.contains("-Production") || executableName.contains("-Prod") { return .production }
    return .staging
}
```

### Backend URLs by Environment

| Environment | Backend URL |
|---|---|
| Dev | `http://localhost:8000/api/v1/` |
| Staging | `https://biziapp-test.app.daimlertruck.com/bizizBackend/public/index.php/api/v1/` |
| Production | `https://bizizapp.com/bizizBackend/public/index.php/api/v1/` |

---

## 4. Technology Stack & Dependencies (CocoaPods)

All dependencies are managed via **CocoaPods** (shared across all 5 targets).

| Library | Version | Purpose |
|---|---|---|
| **MSAL** | 1.2.5 | Microsoft Authentication Library for Azure AD |
| **Moya** | ~10.0 | Network abstraction layer over Alamofire |
| **Alamofire** | (Moya dependency) | HTTP networking |
| **AlamofireImage** | ~3.3 | Image loading/caching |
| **Kingfisher** | (via ImageSlideshow/Kingfisher) | Image downloading and caching |
| **Marshal** | — | JSON parsing (marshal-based) |
| **ImageSlideshow** | ~1.9.0 | Image slideshow UI component |
| **PureLayout** | — | Programmatic Auto Layout helper |
| **AnyFormatKit** | ~2.5 | Phone number text field formatting |
| **EZSwiftExtensions** | — | Swift utility extensions |

Install or update dependencies:
```bash
cd MbtBizizIos
pod install        # First time
pod update         # Update to latest allowed versions
```

> **Always open `MBTBiziz.xcworkspace`** (not `.xcodeproj`) when using CocoaPods.

---

## 5. Architecture — VIP (Clean Swift)

The iOS app uses the **VIP (View-Interactor-Presenter)** pattern, also known as Clean Swift:

```
ViewController  ←──────────────── Router
     │                               ↑
     │ (user events)                 │
     ↓                            (routing)
 Interactor  ─── Worker (API) ───→  
     │
     │ (response models)
     ↓
 Presenter
     │
     │ (view models)
     ↓
 ViewController (updates UI)
```

Each major screen has its own VIP stack. For example, the Home screen has:
- `HomeViewController.swift`
- `HomeInteractor.swift`
- `HomePresenter.swift`
- `HomeRouter.swift`
- `HomeWorker.swift`
- `HomeModels.swift`

Simpler screens may use a condensed version of this pattern.

---

## 6. Authentication — MSAL (Azure AD)

Authentication uses **MSAL 1.2.5** for Azure Active Directory.

### Configuration

| Parameter | Production | Staging | Dev |
|---|---|---|---|
| Client ID | `41b14095-c137-4f5d-b300-accd37cbb4aa` | `e97a3881-d802-433b-8529-96e0e801e346` | Bypass (no MSAL) |
| OIDC Scope | `api://48252d22-0987-4d84-b1d9-00468ec9d424/Read` | `api://910155c2-0cc9-4d21-a48b-4c49c99f8128/Read` | N/A |
| Redirect URI | `msauth.com.daimlertruck.dtag.internal.ios.mbt.test://auth` | `msauth.com.daimlertruck.dtag.internal.ios.mbt.test2://auth` | N/A |
| Tenant | `505cca53-5750-4134-9501-8d52d5df3cd1` | Same | N/A |

### Login Flow

1. App launches → `AppDelegate` initializes MSAL
2. Check for cached token → if valid, skip interactive login
3. No token → `OidcViewController` displays MSAL login UI (in-app Safari sheet)
4. Azure AD redirects back with access token
5. Token stored in MSAL keychain cache
6. Token sent as `Authorization: Bearer <token>` header via Moya plugin
7. On 401 → MSAL silent refresh attempt → if fails → interactive re-login

### Keychain Groups

The `.entitlements` file configures keychain access groups so MSAL tokens can be shared across app extensions if needed.

---

## 7. Screens & Features

### Login

| Screen | Description |
|---|---|
| `OidcViewController` | MSAL interactive login |
| `Welcome/` | Welcome / onboarding screen |
| `ActivationCode/` | OTP entry screen (legacy login flow, kept for reference) |

### Main Navigation

The app uses a **tab bar controller** (or similar navigation) with:

```
Tab: Portal (Home)       ← News, Menu items, features grid
Tab: Notifications       ← Push notification inbox
Tab: Profile / Orchestra ← Profile, Work Hours, Payslip, Digital Card, Settings
```

### Portal Screens

| Screen | Folder | API |
|---|---|---|
| Home Portal | `Portal/Home/` | Various |
| Menu | `Portal/Menu/` | `POST /menuIncrement` |
| News List | `Portal/News/NewsList/` | `POST /newsList` |
| News Detail | `Portal/News/NewsDetail/` | `GET /newsDetail/{id}` |
| Birthday List | `Portal/News/BirthdayList/` | `GET /birthdayList` |
| Shuttle / Transportation | `Portal/Transportation/` | `GET /shuttleOptionList`, `POST /shuttleList` |
| Phone Directory & Clubs | `Portal/Links/` | `GET /phoneLocs`, `GET /phones/{id}`, `GET /socialClubLocs`, `GET /socialClubs/{id}` |
| Maps / Locations | `Portal/Location/` | `GET /maps` |

### Profile / Orchestra Screens

| Screen | Folder | API |
|---|---|---|
| Employee Profile | `Profile/Profile/` | `GET /profile` |
| Notification Settings | `Profile/Settings/` | `GET /notificationSettings`, `POST /changeNotificationSetting` |
| Yearly Work Hours | `Profile/FlexibleWork/` | `GET /yearlyWorkHours/{year}` |
| Monthly Work Hours Detail | `Profile/FlexibleWorkDetail/` | `GET /monthlyWorkHours/{year}/{month}` |
| Work Calendar | `Profile/WorkCalendar/` | `GET /workCalendar/{date}` |

### Payslip Flow (iOS)

```
User taps Payslip
  → GET /payslip/isActive
  → POST /payslip/request-otp  (send OTP SMS)
  → OTP entry screen (60-second timer, 30-second resend cooldown)
      → POST /payslip/verify-otp
  → Period selection (year/month picker)
      → POST /payslip/fetch  (Base64 PDF)
      → PDF displayed in-app (WebView or PDFKit)
```

### Other Screens

| Screen | Folder | Description |
|---|---|---|
| About Us | `Other/About/` | Corporate about page from server |
| Update Popup | `Other/Popups/` | Forced/optional update dialog from `/init` response |
| Terms of Use / KVKK | `Other/TermOfUse/` | Legal text (in-app WebView) |

---

## 8. Network Layer (Moya)

The app uses **Moya** as a network abstraction layer over Alamofire.

### NetworkAPI Enum

All API endpoints are declared as cases in `NetworkAPI.swift`:

```swift
enum NetworkAPI {
    case initCall(versionNumber: String)
    case login(phoneNumber: String, pin: Int)
    case getNewsList(type: NewsType, discountType: DiscountType?, locId: Int?, pageNumber: Int)
    case payslipRequestOtp
    case payslipIsActive
    case payslipVerifyOtp(code: String)
    case payslipFetch(year: Int, month: Int)
    case getUserBusinessCardState
    case activateDigitalCard
    case deactivateDigitalCard
    case menuIncrement(keyName: String)
    // ... all other cases
}
```

### Moya TargetType

`NetworkAPI+TargetType.swift` implements the `TargetType` protocol, mapping each case to:
- `path` — URL path (e.g. `/newsList`)
- `method` — HTTP method (`GET` or `POST`)
- `task` — request encoding (parameters, multipart, etc.)
- `headers` — HTTP headers including `Authorization: Bearer <token>`

### Response Models

All responses use the common envelope:
```json
{ "statusCode": 200, "responseData": { ... }, "errorMessage": null }
```

Response models are `Decodable` structs in `Network/ResponseClasses/`.

---

## 9. Push Notifications (APNs)

- Uses **Apple Push Notification Service (APNs)**
- Push permission requested at app launch via `UNUserNotificationCenter`
- Device token registered with backend via `POST /saveDeviceInfo`
- Device token deregistered on sign-out via `POST /deleteDeviceInfo`
- Notification manager: `MBTNotificationManager.swift`
- Tapping a notification navigates to the relevant screen (e.g. `NewsDetailViewController`)

### APNs Certificates

P12/PEM certificates are in `MbtBizizPanel/app/`:
- `MbtAppPush22May2025.p12` — created May 2025 (verify expiry)
- `bizizProduction.pem`, `BizizEnt.pem`

> **Action required at handover:** Confirm the APNs certificate expiry date and set a calendar reminder for renewal. An expired certificate silently stops all iOS push notifications.

---

## 10. Signing & Distribution

### Distribution Method

**Enterprise (In-House)** distribution via Apple Developer Enterprise Program.

| Property | Value |
|---|---|
| Team ID | `J7U9JQP7Q6` |
| Organization | Mercedes-Benz Türk A.Ş. |
| Distribution Method | Enterprise (In-House) |
| Provisioning Profile (Prod) | `MbtApp7` |
| Provisioning Profile (Staging) | `MbtApp7_Staging` |
| Export Method | `enterprise` |

### Release Process

```bash
# Build and upload prod IPA
./release.sh --platform ios --env prod

# Build and upload staging IPA
./release.sh --platform ios --env staging

# Build and upload Bordro prod IPA
./release-bordro.sh --platform ios --env prod
```

Output artifacts: `release_artifacts/ios/MBT_App.ipa` and `release_artifacts_bordro/ios/MBT_App.ipa`

The script runs `xcodebuild archive` and `xcodebuild -exportArchive`, then uploads via SFTP.

---

## 11. Building & Running

### Prerequisites

- macOS with Xcode 14+
- CocoaPods (`gem install cocoapods`)
- Valid Apple Enterprise certificates and provisioning profiles installed in Keychain / Xcode
- Azure AD account for MSAL login (staging/prod targets)

### Setup

```bash
cd MbtBizizIos

# Install CocoaPods dependencies
pod install

# Open workspace (NOT .xcodeproj)
open MBTBiziz.xcworkspace
```

### Selecting a Target

In Xcode, select the scheme from the scheme picker:
- `MBTBiziz-Dev` — for local development
- `MBTBiziz-Staging` — for staging testing
- `MBTBiziz-Production` — for production build
- `MBTBordro-Staging` / `MBTBordro-Production` — for standalone payslip app

### Local Development

1. Start the backend: `php artisan serve --port=8000`
2. Use `MBTBiziz-Dev` scheme — backend URL is `http://localhost:8000/api/v1/`
3. Dev target has no MSAL requirement — uses development bypass

### Command-Line Build (for CI)

```bash
# Archive for enterprise distribution
xcodebuild archive \
  -workspace MBTBiziz.xcworkspace \
  -scheme MBTBiziz-Production \
  -archivePath ./build/MBTBiziz.xcarchive \
  -configuration Release

# Export IPA
xcodebuild -exportArchive \
  -archivePath ./build/MBTBiziz.xcarchive \
  -exportPath ./build/export \
  -exportOptionsPlist ExportOptions.plist
```

(`release.sh` handles all of this automatically.)

