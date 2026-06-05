# MBT BizIZ Application — Repository Overview

> **Purpose:** This document is the top-level entry point for the MBT BizIZ monorepo.
> It is written for the **handover meeting** and provides a bird's-eye view of the entire system with links to each sub-project's detailed README.

---

## Table of Contents

1. [What is MBT BizIZ?](#1-what-is-mbt-biziz)
2. [Repository Structure](#2-repository-structure)
3. [Documentation Tree](#3-documentation-tree)
4. [System Architecture at a Glance](#4-system-architecture-at-a-glance)
5. [Environments & Servers](#5-environments--servers)
6. [Authentication Model (Azure AD / MSAL)](#6-authentication-model-azure-ad--msal)
7. [External Integrations Summary](#7-external-integrations-summary)
8. [Release & Deployment](#8-release--deployment)
9. [Key Configuration Values (Quick Reference)](#9-key-configuration-values-quick-reference)
10. [Known Quirks & Notes for Handover](#10-known-quirks--notes-for-handover)

---

## 1. What is MBT BizIZ?

**MBT BizIZ** (also written *MBT Biziz*) is an enterprise mobile application developed for **Mercedes-Benz Türk A.Ş.** employees. It provides a centralized, authenticated HR and corporate services app available on both iOS and Android.

### Core Features

| Feature | Description |
|---|---|
| **Employee Authentication** | Phone-number + OTP login, backed by Azure AD (MSAL / OIDC) |
| **Corporate News & Announcements** | Targeted by employee type (white-collar / blue-collar) and location |
| **Discount Codes** | Employee discount campaigns with one-time-use codes |
| **Payslip (Bordro)** | OTP-gated payslip viewer integrated with SAP BTP via SOAP |
| **Work Hours & Calendar** | Yearly/monthly work hours and shift/leave calendar from SAP HCM |
| **Meal Menu** | Weekly cafeteria menu with calorie/nutrition info imported from Excel |
| **Shuttle Schedules** | Employee, ring, inter-location, and other shuttle timetables |
| **Maps** | Campus/office maps |
| **Birthday List** | Colleagues' upcoming birthdays |
| **Social Clubs** | Corporate social club directory |
| **Internal Phone Directory** | Company-wide phone book |
| **Social Media / Medias** | Corporate social media channels |
| **Digital Business Card** | Employee-activated shareable vCard/URL |
| **Push Notifications** | Firebase (Android) & APNs (iOS) notification delivery |
| **Settings** | Notification preferences per category |
| **App Feedback** | In-app feedback submission routed to configured email addresses |
| **QR Code** | QR-based internal feature |

There is also a **separate "Bordro" (Payslip-only) app** for iOS — it shares the same codebase/targets but is distributed as a standalone app.

---

## 2. Repository Structure

```
MbtAppRepo/
├── README.md                        ← You are here (top-level overview)
├── release.sh                       ← Build & deploy script for main app (Android + iOS)
├── release-bordro.sh                ← Build & deploy script for Bordro (payslip-only iOS app)
├── generate_arch_doc.py             ← Generates architecture Word document for EPA security check
├── MBT_App_Architecture_Diagram.drawio  ← Editable architecture diagram
│
├── MbtBizizBackend/                 ← Backend REST API (Laravel / Lumen)
│   └── README.md  →  docs/backend/README.md
│
├── MbtBizizPanel/                   ← Admin Panel (Laravel + Livewire)
│   └── readme.md  →  docs/panel/README.md
│
├── MbtBizizAndroid/                 ← Android App (Java/Kotlin, Gradle)
│   └── README.md  →  docs/android/README.md
│
├── MbtBizizIos/                     ← iOS App (Swift, CocoaPods)
│   └── README.md  →  docs/ios/README.md
│
├── release_artifacts/               ← Output APKs & IPAs for main app releases
│   ├── android/
│   └── ios/
│
└── release_artifacts_bordro/        ← Output IPAs for Bordro (payslip app) releases
    └── ios/
```

---

## 3. Documentation Tree

```
MbtAppRepo/
├── README.md                          ← Top-level overview (this file)
│
├── MbtBizizBackend/
│   └── README.md                      ← Backend API: routes, jobs, auth, integrations
│       └── [See: Backend README]
│
├── MbtBizizPanel/
│   └── readme.md                      ← Admin Panel: features, Azure AD login, routes
│       └── [See: Panel README]
│
├── MbtBizizAndroid/
│   └── README.md                      ← Android App: architecture, flavors, MSAL, screens
│       └── [See: Android README]
│
└── MbtBizizIos/
    └── README.md                      ← iOS App: targets, pods, MSAL, screens
        └── [See: iOS README]
```

| Component | README |
|---|---|
| Backend API | [MbtBizizBackend/README.md](MbtBizizBackend/README.md) |
| Admin Panel | [MbtBizizPanel/readme.md](MbtBizizPanel/readme.md) |
| Android App | [MbtBizizAndroid/README.md](MbtBizizAndroid/README.md) |
| iOS App | [MbtBizizIos/README.md](MbtBizizIos/README.md) |

---

## 4. System Architecture at a Glance

```
┌──────────────────────────────────────────────────────────┐
│                    EXTERNAL SYSTEMS                      │
│  SAP HCM (files)  │  SAP BTP (SOAP)  │  Azure AD (OIDC) │
│  Posta Güvercini  │  Firebase (FCM)  │  APNs (iOS push) │
└────────┬──────────────────┬──────────────────┬───────────┘
         │ SFTP/files        │ HTTPS/SOAP       │ JWKS
         ▼                  ▼                  ▼
┌─────────────────────────────────────────────────────────┐
│               APPLICATION SERVER (on-prem)              │
│                                                         │
│  ┌───────────────────────┐   ┌─────────────────────┐   │
│  │   Backend API         │   │    Admin Panel       │   │
│  │   (Laravel/Lumen)     │   │   (Laravel 10 +      │   │
│  │   /bizizBackend/      │   │    Livewire 3)        │   │
│  └──────────┬────────────┘   └──────────┬──────────┘   │
│             │                           │               │
│             ▼                           ▼               │
│       ┌──────────┐              ┌──────────────┐        │
│       │  MySQL   │              │  Redis Cache │        │
│       │ Database │              │  (localhost) │        │
│       └──────────┘              └──────────────┘        │
└───────────────────────────┬─────────────────────────────┘
                            │ HTTPS REST API
               ┌────────────┴────────────┐
               ▼                         ▼
    ┌──────────────────┐      ┌──────────────────────┐
    │   iOS App        │      │   Android App         │
    │   (Swift/ObjC)   │      │   (Java/Kotlin)       │
    │   MSAL 1.2.5     │      │   MSAL 5.6.0          │
    │   Enterprise     │      │   3 flavors           │
    │   5 targets      │      │   (dev/staging/prod)  │
    └──────────────────┘      └──────────────────────┘
```

### Component Summary

| Component | Tech Stack | Role |
|---|---|---|
| **Backend API** | PHP 8.1, Laravel/Lumen 10 | REST API for mobile apps |
| **Admin Panel** | PHP 8.1, Laravel 10, Livewire 3, Blade | Content & config management UI |
| **MySQL DB** | MySQL (utf8mb4_unicode_ci) | Primary persistent data store |
| **Redis** | Redis (via Predis) | Session cache, OTP, food menu, shuttle, payslip flags |
| **iOS App** | Swift 4 / Obj-C, CocoaPods, MSAL 1.2.5 | Native iOS app (5 build targets) |
| **Android App** | Java/Kotlin, Gradle, MSAL 5.6.0 | Native Android app (3 flavors) |

---

## 5. Environments & Servers

| Environment | Server IP | Purpose |
|---|---|---|
| **Staging** | `192.168.24.16` | Test environment for QA and pre-release validation |
| **Production** | `192.168.24.26` | Live environment serving real employees |
| **Database** | `192.168.24.25:3306` | MySQL (no public internet access) |
| **Redis** | `127.0.0.1:6379` | Localhost on each app server |

### Backend URL Patterns

| Environment | Base URL |
|---|---|
| Staging | `https://biziapp-test.app.daimlertruck.com/bizizBackend/public/index.php/api/v1/` |
| Production | `https://bizizapp.com/bizizBackend/public/index.php/api/v1/` |
| Local Dev | `http://10.0.2.2:8000/api/v1/` (Android emulator) / `http://localhost:8000/api/v1/` (iOS) |

---

## 6. Authentication Model (Azure AD / MSAL)

All authentication flows through **Microsoft Azure Active Directory**. The apps use the MSAL library for token acquisition. The backend validates JWT tokens against Azure's JWKS public key endpoint.

```
Employee → MSAL Login (Azure AD) → Access Token (JWT)
         → Bearer token sent with each API request
         → Backend verifies token against JWKS endpoint
         → User identified and authorized
```

### Azure Tenant

- **Tenant ID:** `505cca53-5750-4134-9501-8d52d5df3cd1`
- **JWKS URL:** `https://login.microsoftonline.com/505cca53-5750-4134-9501-8d52d5df3cd1/discovery/v2.0/keys`

### App Registrations

| App | Environment | Client ID |
|---|---|---|
| iOS App | Production | `41b14095-c137-4f5d-b300-accd37cbb4aa` |
| iOS App | Staging | `e97a3881-d802-433b-8529-96e0e801e346` |
| Android App | All | `bc50644f-7099-4737-bcd1-a2791ba4930b` |
| Admin Panel | All | Via `AZURE_CLIENT_ID` env var |

### Old App Compatibility

The backend detects requests from old (pre-MSAL) app versions using the presence of a `token` header (legacy JWT). These requests are immediately rejected with a message to download the new app. The old login flow (phone + OTP SMS → custom JWT) is no longer active for production.

---

## 7. External Integrations Summary

| Integration | Purpose | Protocol |
|---|---|---|
| **SAP HCM** | Employee data, work hours, work calendar files | SFTP (daily file drop to `/var/www/html/bizizFiles/`) |
| **SAP BTP** | Payslip PDF retrieval | SOAP over HTTPS |
| **Azure AD** | Authentication (MSAL/OIDC/JWKS) | HTTPS/OIDC |
| **Posta Güvercini** | OTP SMS delivery for payslip | HTTPS REST |
| **Firebase (FCM)** | Android push notifications | HTTPS |
| **Apple APNs** | iOS push notifications | HTTPS/P12 cert |
| **Telegram Bot** | Server-side operational logging & alerts | HTTPS Bot API |

### SAP File Import Schedule (Backend Cron Jobs)

| Artisan Command | Schedule | Data |
|---|---|---|
| `set:users` | Daily 07:05 | Employee records from SAP HCM |
| `set:working_hours_info` | Daily 07:08 | Flexible work hours data |
| `set:working_calendar` | Daily 07:08 | Work calendar / shift data |
| `set:food_menu` | Daily 07:09 + hourly | Weekly cafeteria menu |
| `set:food_shuttle` | Daily 07:09 + hourly | Cafeteria shuttle info |
| `set:employee_shuttles` | Hourly | Employee shuttle routes |
| `set:ring_shuttles` | Hourly | Ring shuttle routes |
| `set:other_shuttles` | Hourly | Other shuttle routes |
| `set:interlocation_shuttles` | Hourly | Inter-location shuttle routes |

---

## 8. Release & Deployment

### Main App (`release.sh`)

Builds and uploads the main MBT BizIZ app for both Android and iOS to the respective staging or production servers.

```bash
# Build and upload everything
./release.sh

# Specific platform and environment
./release.sh --platform android --env staging
./release.sh --platform ios --env prod

# Only upload already-built artifacts
./release.sh --skip-build --env prod
```

**Artifacts written to:** `release_artifacts/android/` and `release_artifacts/ios/`

### Bordro (Payslip-Only) App (`release-bordro.sh`)

Builds and uploads the standalone Bordro app for iOS only. Same syntax as `release.sh`.

```bash
./release-bordro.sh --platform ios --env prod
```

**Artifacts written to:** `release_artifacts_bordro/ios/`

### Upload Destinations

| Environment | Protocol | Host | Remote Dir |
|---|---|---|---|
| Staging | SFTP (port 22) | `192.168.24.16` | `/var/www/html/app/bin__2.0` |
| Production | SFTP (port 22) | `192.168.24.26` | `/var/www/html/app/bin__2.0` |
| Bordro Staging | SFTP (port 22) | `192.168.24.16` | `/var/www/html/app/bordro` |
| Bordro Prod | SFTP (port 22) | `192.168.24.26` | `/var/www/html/app/bordro` |

### Android Build
- Version: `1.5.0` (versionCode: 46)
- Min SDK: 24 (Android 7.0), Target SDK: 33
- Signed with enterprise keystore (`signKey`, alias `keyMbt`)
- Built with Gradle

### iOS Build
- Enterprise In-House distribution (Team ID: `J7U9JQP7Q6`)
- Provisioning Profile: `MbtApp7` (prod), `MbtApp7_Staging` (staging)
- CocoaPods dependency management
- 5 targets: `MBTBiziz-Dev`, `MBTBiziz-Staging`, `MBTBiziz-Production`, `MBTBordro-Staging`, `MBTBordro-Production`

---

## 9. Key Configuration Values (Quick Reference)

| Key | Value |
|---|---|
| Azure Tenant ID | `505cca53-5750-4134-9501-8d52d5df3cd1` |
| iOS Prod Client ID | `41b14095-c137-4f5d-b300-accd37cbb4aa` |
| iOS Staging Client ID | `e97a3881-d802-433b-8529-96e0e801e346` |
| Android Client ID | `bc50644f-7099-4737-bcd1-a2791ba4930b` |
| iOS Team ID | `J7U9JQP7Q6` |
| Android App ID (Prod) | `com.daimlertruck.dtag.internal.android.mbt.test` |
| iOS Bundle ID (Prod) | `com.daimlertruck.dtag.internal.ios.mbt.test` |
| SAP BTP SOAP Action | `http://sap.com/xi/WebService/soap1.1` |
| OTP Expiry | 1 minute (stored in Redis) |
| Payslip Verified Session | 5 minutes (stored in Redis) |
| Food Menu Cache | 6 hours (Laravel cache via Redis) |
| Android Version | `1.5.0` (build 46) |
| iOS Platform Min | iOS 11.0 |
| Android Min SDK | 24 (Android 7.0 Nougat) |

---

## 10. Known Quirks & Notes for Handover

> These are important operational notes gathered from reading the source code.

1. **Old app detection:** The backend `Authenticate` middleware checks for a `token` HTTP header (used by legacy app versions) and immediately rejects those requests with a Turkish message to update the app.

2. **Payslip "always-allowed" user IDs:** In `PayslipController::verifyOtp`, user IDs `7701`, `7697`, `103559`, `103560` bypass OTP verification — these appear to be test/admin accounts. Review after handover.

3. **Food menu depends on file naming:** The Excel file must be named `Yemek_{dd.mm.YYYY}.xlsx` (start of week date) placed in `/var/www/html/bizizFiles/food/` on the server before the daily cron job runs.

4. **Blue-collar vs white-collar:** Employee `type` field (1 = white-collar, 2 = blue-collar) controls payslip date ranges, work hour labels, and news targeting. `company_code = 1402` disables work hours for certain white-collar users.

5. **Register number substitution:** In work hours and payslip code, register number `1234563` is silently replaced with `100038` and user ID `7701` uses register number `114550`. These appear to be development/test substitutions.

6. **Payslip availability is Redis-flag controlled:** The Admin Panel can toggle payslip on/off globally via a Redis key (`payslip_active`). Custom error messages for deactivation and period-not-open are also stored in Redis.

7. **Digital Business Card:** Requires `dk_uuid` and `dk_enabled` columns on the `users` table. The public card URL uses `config('services.dk.url')` + `/d/{uuid}`.

8. **Menu view tracking:** Almost every feature call logs usage to Redis via `MenuViewService::increment()`. The Dashboard reads these for analytics. Keys are namespaced `menu:view:categories`, `menu:view:categories:white`, etc.

9. **OIDC dev bypass:** In Android dev flavor and iOS dev target, MSAL can be bypassed with a hardcoded token (`mbtbiziz-local-dev-bypass-token`) for local development without Azure AD.

10. **Push notification P12 cert files** are stored in `MbtBizizPanel/app/` (`.pem`, `.p12`). These need to be renewed periodically based on Apple cert expiry.

---

*For detailed component documentation, see the linked READMEs in the [Documentation Tree](#3-documentation-tree) above.*
