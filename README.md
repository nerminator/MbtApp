# MbtBizizBackend — Backend REST API

> **Part of the MBT BizIZ monorepo.** See the [top-level README](../README.md) for the full system overview.

This is the central REST API that serves both the iOS and Android mobile apps. It is built on **PHP 8.1 with Laravel/Lumen 10** (the lightweight micro-framework variant of Laravel).

---

## Table of Contents

1. [Summary](#1-summary)
2. [Project Structure](#2-project-structure)
3. [Technology Stack](#3-technology-stack)
4. [Authentication](#4-authentication)
5. [API Endpoints Reference](#5-api-endpoints-reference)
6. [Controllers Overview](#6-controllers-overview)
7. [Middleware](#7-middleware)
8. [Scheduled Jobs (Cron)](#8-scheduled-jobs-cron)
9. [External Integrations](#9-external-integrations)
10. [Database Schema Overview](#10-database-schema-overview)
11. [Server Topology](#11-server-topology)
12. [Setup & Local Development](#12-setup--local-development)
13. [Environment Variables](#13-environment-variables)

---

## 1. Summary

The backend is responsible for:

- Authenticating mobile users via **Azure AD JWT** (MSAL-issued tokens)
- Serving all content to the mobile apps (news, food menu, shuttle schedules, etc.)
- Importing SAP HCM data (employees, work hours, calendars) via scheduled artisan commands
- Delivering **payslips** by calling the SAP BTP SOAP API (OTP-gated)
- Sending **OTP SMS** via Posta Güvercini for payslip verification
- Tracking menu view analytics in **Redis**

---

## 2. Project Structure

```
MbtBizizBackend/
├── app/
│   ├── Constants.php              ← Company location, shuttle type, employee type constants
│   ├── User.php                   ← Eloquent User model
│   ├── Console/
│   │   ├── Kernel.php             ← Cron schedule definitions
│   │   └── Commands/              ← Artisan commands (data import jobs)
│   ├── Http/
│   │   ├── Controllers/           ← All API controllers (one per feature)
│   │   └── Middleware/
│   │       ├── Authenticate.php   ← JWT auth + old-app detection
│   │       ├── SecurityHeaders.php
│   │       ├── HttpRedirect.php
│   │       └── ThrottleRequests.php
│   ├── Events/
│   ├── Jobs/                      ← Queue jobs (device info save/delete)
│   ├── Listeners/
│   ├── Mail/
│   ├── Providers/
│   └── Services/
│       └── MenuViewService.php    ← Centralized Redis-based menu view analytics
├── bootstrap/app.php              ← App bootstrap, middleware registration
├── config/                        ← App configuration (database, cache, mail, etc.)
├── database/
│   ├── migrations/                ← Database schema history
│   └── seeds/
├── routes/
│   └── web.php                    ← All API route definitions
├── storage/                       ← Logs, framework cache, uploaded files
├── resources/lang/                ← i18n strings (Turkish + English)
├── tests/
├── wsdl/                          ← WSDL files for SAP BTP SOAP payslip API
│   ├── payslip.wsdl
│   ├── payslip_btp.wsdl
│   └── payslip1.wsdl
└── composer.json
```

---

## 3. Technology Stack

| Component | Detail |
|---|---|
| Language | PHP 8.1 |
| Framework | Laravel/Lumen 10 |
| Database | MySQL (via Eloquent + raw DB queries) |
| Cache / Session | Redis (via `predis/predis`) |
| HTTP Client | Guzzle 7.9 |
| JWT Auth | `firebase/php-jwt` 6.10 |
| Excel Import | `rap2hpoutre/fast-excel` 5.5 |
| Mail | `illuminate/mail` + Mailgun (`symfony/mailgun-mailer`) |
| CSP Headers | `spatie/laravel-csp` |
| LINQ-style queries | `athari/yalinqo` |

---

## 4. Authentication

### Overview

Authentication uses **Azure AD JWT tokens** issued via MSAL on the mobile clients.

**Flow:**
1. Mobile app authenticates with Azure AD via MSAL → receives `access_token`
2. App sends `Authorization: Bearer <token>` header with every API request
3. Backend validates the token against Azure's JWKS public keys
4. On success, user is looked up in the local `users` table by their Azure subject claim

### Azure Configuration

| Parameter | Value |
|---|---|
| Tenant ID | `505cca53-5750-4134-9501-8d52d5df3cd1` |
| JWKS URL | `https://login.microsoftonline.com/505cca53-5750-4134-9501-8d52d5df3cd1/discovery/v2.0/keys` |

### Legacy App Detection

The `Authenticate` middleware checks for a `token` HTTP header, which was used by old app versions (pre-MSAL). Any request with this header is immediately rejected with:
```json
{ "statusCode": 200, "responseData": null, "errorMessage": "Lütfen yeni uygulamayı indiriniz." }
```

### Rate Limiting

The `checkPhone` endpoint is throttled to **5 requests per 5 minutes** (`throttle:5,5`).

---

## 5. API Endpoints Reference

All endpoints are prefixed with `/api/v1/`. A `securityHeaders` middleware is applied to all API routes.

### Public Endpoints (No Auth Required)

| Method | Path | Controller | Description |
|---|---|---|---|
| `GET` | `/` | — | Health check / root |
| `GET` | `/dc/{uuid}` | `DijitalKartvizit@show` | View digital business card (public) |
| `GET` | `/dc/{uuid}/downloadVcf` | `DijitalKartvizit@downloadVcf` | Download vCard file |
| `GET` | `/digitalCard/{uuid}` | `DijitalKartvizit@show` | Alt path for digital card |
| `GET` | `/digitalCard/{uuid}/downloadVcf` | `DijitalKartvizit@downloadVcf` | Alt path for vCard download |
| `POST` | `/api/v1/init` | `InitController@init` | App version check & config |
| `GET` | `/api/v1/test` | `InitController@test` | Server health ping |
| `POST` | `/api/v1/checkPhone` | `LoginController@checkPhone` | Check if phone is registered (throttled 5/5min) |
| `POST` | `/api/v1/login` | `LoginController@login` | MSAL token login / user auth |
| `POST` | `/api/v1/appStartup` | `LoginController@appStartup` | App startup signal |
| `POST` | `/api/v1/sendCrashLog` | `CrashLogController@sendCrashLog` | Client crash report |

### Authenticated Endpoints (Bearer Token Required)

#### News & Content

| Method | Path | Controller | Description |
|---|---|---|---|
| `POST` | `/api/v1/newsList` | `NewsController@newsList` | Paginated news list (by type, discount type) |
| `GET` | `/api/v1/newsDetail/{id}` | `NewsController@newsDetail` | News detail |
| `GET` | `/api/v1/birthdayList` | `NewsController@birthdayList` | Colleague birthdays |
| `GET` | `/api/v1/getDiscountCode/{newsId}` | `NewsController@getDiscountCode` | Get discount code for news item |

#### Employee & Profile

| Method | Path | Controller | Description |
|---|---|---|---|
| `GET` | `/api/v1/profile` | `ProfileController@profile` | Employee profile info |
| `GET` | `/api/v1/workCalendar/{date}` | `ProfileController@workCalendar` | Work calendar for a date |
| `GET` | `/api/v1/yearlyWorkHours/{year}` | `ProfileController@yearlyWorkHours` | Yearly work hours |
| `GET` | `/api/v1/monthlyWorkHours/{year}/{month}` | `ProfileController@monthlyWorkHours` | Monthly work hours |

#### Payslip (Bordro)

| Method | Path | Controller | Description |
|---|---|---|---|
| `GET` | `/api/v1/payslip/isActive` | `PayslipController@isActive` | Check if payslip feature is enabled |
| `POST` | `/api/v1/payslip/request-otp` | `PayslipController@requestOtp` | Generate & send OTP via SMS |
| `POST` | `/api/v1/payslip/verify-otp` | `PayslipController@verifyOtp` | Verify OTP (5-min verified session) |
| `POST` | `/api/v1/payslip/fetch` | `PayslipController@fetchPayslip` | Fetch payslip PDF (Base64) from SAP BTP |

#### Food, Shuttle & Maps

| Method | Path | Controller | Description |
|---|---|---|---|
| `GET` | `/api/v1/foodMenu` | `FoodMenuController@foodMenu` | Weekly cafeteria menu + shuttle info |
| `GET` | `/api/v1/maps` | `MapController@maps` | Campus maps |
| `GET` | `/api/v1/shuttleOptionList` | `ShuttleController@shuttleOptionList` | Shuttle types and locations |
| `POST` | `/api/v1/shuttleList` | `ShuttleController@shuttleList` | Shuttle schedule for given type/location |

#### Notifications & Settings

| Method | Path | Controller | Description |
|---|---|---|---|
| `POST` | `/api/v1/notificationList` | `NotificationController@notificationList` | User notification list |
| `POST` | `/api/v1/deleteNotification` | `NotificationController@deleteNotification` | Soft-delete a notification |
| `GET` | `/api/v1/notificationBadgeCount` | `NotificationController@notificationBadgeCount` | Unread count for badge |
| `POST` | `/api/v1/saveDeviceInfo` | `DeviceInfoController@saveDeviceInfo` | Register device token (FCM/APNs) |
| `POST` | `/api/v1/deleteDeviceInfo` | `DeviceInfoController@deleteDeviceInfo` | Deregister device token |
| `GET` | `/api/v1/notificationSettings` | `SettingController@notificationSettings` | Get notification preferences |
| `POST` | `/api/v1/changeNotificationSetting` | `SettingController@changeNotificationSetting` | Update notification preference |

#### Directory & Media

| Method | Path | Controller | Description |
|---|---|---|---|
| `GET` | `/api/v1/socialClubLocs` | `SocialClubsController@getSocialClubLocs` | Social club locations |
| `GET` | `/api/v1/socialClubs/{loc_id}` | `SocialClubsController@getSocialClubs` | Clubs at a location |
| `GET` | `/api/v1/phoneLocs` | `PhonesController@getPhoneLocs` | Phone directory locations |
| `GET` | `/api/v1/phones/{loc_id}` | `PhonesController@getPhones` | Phones at a location |
| `GET` | `/api/v1/medias` | `MediasController@getMedias` | Social media / media links |

#### Digital Business Card

| Method | Path | Controller | Description |
|---|---|---|---|
| `GET` | `/api/v1/getUserBusinessCardState` | `DijitalKartvizit@getUserBusinessCardState` | Card activation status + URL |
| `GET` | `/api/v1/activateDigitalCard` | `DijitalKartvizit@activateDigitalCard` | Activate card & generate UUID |
| `GET` | `/api/v1/deactivateDigitalCard` | `DijitalKartvizit@deactivateDigitalCard` | Deactivate card |

#### Miscellaneous

| Method | Path | Controller | Description |
|---|---|---|---|
| `GET` | `/api/v1/signOut` | `LoginController@signOut` | Sign out / clear device token |
| `GET` | `/api/v1/userConfig` | `InitController@userConfig` | User-specific config |
| `POST` | `/api/v1/submitFeedback` | `AppFeedbackController@submitFeedback` | Submit in-app feedback |
| `POST` | `/api/v1/menuIncrement` | `MenuViewController@increment` | Increment menu view counter |

---

## 6. Controllers Overview

| Controller | Feature Area | Key Notes |
|---|---|---|
| `InitController` | App version check | Checks `ANDROID_VERSION` / `IOS_VERSION` env vars; returns update popups |
| `LoginController` | Authentication | MSAL JWT validation, `appStartup`, sign-out |
| `NewsController` | News & Discounts | Filtered by employee type, location, company location |
| `ProfileController` | Employee profile | Employee info from DB; work hours from SAP data |
| `PayslipController` | Payslip (Bordro) | OTP via SMS → Redis session → SAP BTP SOAP call |
| `NotificationController` | Push notifications | Uses stored procedure `getNotificationList` |
| `DeviceInfoController` | Device tokens | FCM/APNs token registration/deregistration |
| `FoodMenuController` | Cafeteria menu | Reads from Redis cache (populated by cron from Excel) |
| `ShuttleController` | Shuttle schedules | Reads from Redis cache (populated by cron from Excel) |
| `MapController` | Campus maps | Static map data |
| `SocialClubsController` | Social clubs directory | Hierarchical: locations → clubs → details |
| `PhonesController` | Phone directory | Hierarchical: locations → departments → phones |
| `MediasController` | Social media links | List of corporate media accounts |
| `DijitalKartvizit` | Digital business card | UUID-based vCard, public share URL |
| `SettingController` | Notification settings | Per-category notification toggle |
| `AppFeedbackController` | App feedback | Emails feedback to configured recipients |
| `MenuViewController` | Analytics | Increments Redis view counters |
| `QRCodeController` | QR codes | Writes QR data to file for processing |
| `CrashLogController` | Crash reporting | Receives mobile crash logs |

---

## 7. Middleware

| Middleware | Alias | Applied To |
|---|---|---|
| `Authenticate` | `auth` | All authenticated routes |
| `ThrottleRequests` | `throttle` | `checkPhone` (5 req/5 min) |
| `SecurityHeaders` | `securityHeaders` | All `/api/v1` routes |
| `HttpRedirect` | — | HTTP → HTTPS redirect |

---

## 8. Scheduled Jobs (Cron)

The cron scheduler must be configured to run `php artisan schedule:run` every minute. All times are `Europe/Istanbul`.

```cron
* * * * * cd /path-to-backend && php artisan schedule:run >> /dev/null 2>&1
```

| Command | Schedule | Source File | Target |
|---|---|---|---|
| `set:users` | Daily 07:05 | `/var/www/html/bizizFiles/users/mbt_persdata_{ddmmYYYY}.txt` | `users` table |
| `set:birthday_list` | Daily 07:07 | Derived from users data | `users` table |
| `set:working_hours_info` | Daily 07:08 | `/var/www/html/bizizFiles/working_hours_info/mbt_my_timedata_{ddmmYYYY}.txt` | `working_hours_info` table |
| `set:working_calendar` | Daily 07:08 | `/var/www/html/bizizFiles/working_calendar/mbt_dvm_dvms_{ddmmYYYY}.txt` | `working_calendar` table |
| `set:food_menu` | Daily 07:09 + Hourly | `/var/www/html/bizizFiles/food/Yemek_{dd.mm.YYYY}.xlsx` | Redis cache |
| `set:food_shuttle` | Daily 07:09 + Hourly | `/var/www/html/bizizFiles/food/Yemek_Servis.xlsx` | Redis cache |
| `set:employee_shuttles` | Hourly | `/var/www/html/bizizFiles/shuttle/` | `shuttles` table |
| `set:ring_shuttles` | Hourly | `/var/www/html/bizizFiles/shuttle/` | `shuttles` table |
| `set:other_shuttles` | Hourly | `/var/www/html/bizizFiles/shuttle/` | `shuttles` table |
| `set:interlocation_shuttles` | Hourly | `/var/www/html/bizizFiles/shuttle/` | `shuttles` table |

---

## 9. External Integrations

### 9.1 SAP HCM — File-Based Import

SAP HCM deposits flat files (tab-separated `.txt` and `.xlsx`) to `/var/www/html/bizizFiles/` on the server. Artisan commands parse these and update the database. File encoding is auto-detected and converted to UTF-8.

### 9.2 SAP BTP — Payslip SOAP API

Payslip PDFs are retrieved from SAP BTP via SOAP. The WSDL files are in `wsdl/`.

- **SOAP Action:** `http://sap.com/xi/WebService/soap1.1`
- **Authentication:** Basic auth credentials via environment variables
- **Response:** Base64-encoded PDF returned to mobile app

**Payslip flow:**
```
User → POST /payslip/request-otp  → OTP generated, stored in Redis (1 min), sent via SMS
User → POST /payslip/verify-otp   → OTP verified, Redis flag set (5 min)
User → POST /payslip/fetch        → Checks Redis flag → calls SAP BTP SOAP → returns Base64 PDF
```

Payslip availability is controlled by a Redis key (`payslip_active = '1'`). Toggled from the Admin Panel.

Blue-collar vs white-collar employees use different `payslip_months` table records (`yaka_turu = 'Mavi'` or `'Beyaz'`).

### 9.3 SMS — Posta Güvercini

OTP messages are sent via the Posta Güvercini SMS gateway. The `sendOtpSms()` method in `PayslipController` handles the HTTP call. Credentials are in environment variables.

### 9.4 Azure AD — JWT Validation

Access tokens from MSAL are validated using the public keys from the Azure JWKS endpoint. The `firebase/php-jwt` library handles the RS256 signature verification.

### 9.5 Menu View Analytics — Redis

Every major feature endpoint calls `MenuViewService::increment($key)` which writes to:
- `menu:view:categories` (sorted set, all employees)
- `menu:view:categories:white` (sorted set, white-collar)
- `menu:view:categories:blue` (sorted set, blue-collar)
- `menu:view:categories:other` (sorted set, other types)

These are read by the Admin Panel Dashboard.

---

## 10. Database Schema Overview

Key tables (from migration history):

| Table | Description |
|---|---|
| `users` | Employee records (synced from SAP). Has `type` (1=white, 2=blue), `company_location_id`, `mobile_phone`, `register_number`, `dk_uuid`, `dk_enabled`, `last_login_at` |
| `company_locations` | Office locations (Genel Müdürlük=1, Hoşdere=2, Aksaray=3) |
| `news` | News/announcements. Has `type`, `discount_type`, `employee_type`, `start_time`, `end_time`, `status` |
| `news_images` | Images attached to news items |
| `news_company_location` | Location targeting for news |
| `user_devices` | FCM/APNs device tokens |
| `user_notifications` | Per-user notification records |
| `user_notification_settings` | Per-user, per-type notification toggles |
| `user_pin_codes` | Legacy PIN codes (pre-MSAL) |
| `working_hours_info` | Flexible work hours from SAP |
| `working_calendar` | Work calendar from SAP |
| `working_calendar_types` | Calendar entry types |
| `buildings` | Building information |
| `stops` | Shuttle stop information |
| `shuttles` | Shuttle schedule entries |
| `to_company_shuttle_stops` | To-company shuttle stop mappings |
| `from_company_shuttle_stops` | From-company shuttle stop mappings |
| `payslip_months` | Payslip availability periods (donem, yaka_turu, baslangic_tarihi, bitis_tarihi) |
| `captcha` | Legacy captcha records (no longer active) |
| `user_logins` | Login audit log |

---

## 11. Server Topology

The backend runs on two separate on-premises servers per environment. The database server is **not reachable from the public internet** — only from the app server over the internal private network.

### Production

| Role | IP / Host | Detail |
|---|---|---|
| **App Server** | `192.168.24.26` | Runs Laravel/Lumen, Admin Panel, Redis |
| **Database Server** | `192.168.24.25:3306` | MySQL — internal LAN only |
| **Redis** | `127.0.0.1:6379` | Localhost on app server |

### Staging / Test

| Role | IP / Host | Detail |
|---|---|---|
| **App Server** | `192.168.24.16` | Runs Laravel/Lumen, Admin Panel, Redis |
| **Database Server** | `192.168.24.15:3306` | MySQL — internal LAN only |
| **Redis** | `127.0.0.1:6379` | Localhost on app server |

### Security Note

The `DB_HOST` in each environment's `.env` points to the **database server IP**, not localhost. The database server firewall only allows MySQL connections from the paired app server. No public port is open.

---

## 12. Setup & Local Development

### Prerequisites

- PHP 8.1+
- Composer
- MySQL
- Redis
- (Optional) Local SAP file drops for full data import testing

### Steps

```bash
cd MbtBizizBackend

# Install dependencies
composer install

# Copy env file and configure
cp .env.example .env

# Generate app key
php artisan key:generate  # Note: Lumen may not use this, configure APP_KEY in .env directly

# Run migrations
php artisan migrate

# Start development server
php artisan serve --port=8000
```

For Android emulator, the backend will be accessible at `http://10.0.2.2:8000/api/v1/`.

---

## 13. Environment Variables

Key `.env` variables to configure:

```env
APP_ENV=local|staging|production
APP_KEY=...
APP_TIMEZONE=Europe/Istanbul

# DB_HOST points to the SEPARATE database server (not localhost)
# Production:  192.168.24.25
# Staging:     192.168.24.15
DB_HOST=192.168.24.25
DB_DATABASE=...
DB_USERNAME=...
DB_PASSWORD=...

# Redis runs on the app server itself
REDIS_HOST=127.0.0.1
REDIS_PORT=6379

# App version check
ANDROID_VERSION=1.5.0
IOS_VERSION=...
DOWNLOAD_URL=...

# Azure AD JWT validation
AZURE_TENANT_ID=505cca53-5750-4134-9501-8d52d5df3cd1

# SAP BTP Payslip SOAP
BTP_PAYSLIP_URL=...
BTP_PAYSLIP_USERNAME=...
BTP_PAYSLIP_PASSWORD=...

# SMS OTP (Posta Güvercini)
SMS_API_URL=...
SMS_API_KEY=...
SMS_SENDER=...

# Digital Business Card
DK_URL=...

# Mail (Mailgun)
MAIL_DRIVER=mailgun
MAILGUN_DOMAIN=...
MAILGUN_SECRET=...
```
