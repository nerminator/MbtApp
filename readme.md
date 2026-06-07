# MbtBizizPanel — Admin Panel

> **Part of the MBT BizIZ monorepo.** See the [top-level README](../README.md) for the full system overview.

The Admin Panel is a web application used by **authorized IT/content administrators** to manage all dynamic content served to the mobile apps and to control feature flags. It is built on **PHP 8.1 / Laravel 10** with **Livewire 3** for reactive UI components and Blade for templating.

---

## Table of Contents

1. [Summary](#1-summary)
2. [Project Structure](#2-project-structure)
3. [Technology Stack](#3-technology-stack)
4. [Authentication — Azure AD (OIDC)](#4-authentication--azure-ad-oidc)
5. [Features & Pages](#5-features--pages)
6. [Routes Reference](#6-routes-reference)
7. [Dashboard & Analytics](#7-dashboard--analytics)
8. [Payslip Management](#8-payslip-management)
9. [Push Notification System](#9-push-notification-system)
10. [Livewire Components](#10-livewire-components)
11. [Setup & Local Development](#11-setup--local-development)
12. [Environment Variables](#12-environment-variables)

---

## 1. Summary

The Admin Panel provides:

- **Content management** — Create, edit, and delete news/announcements with image uploads and push notification dispatch
- **Payslip control** — Toggle the payslip feature on/off, set error messages, and manage payslip period availability windows (by collar type and date range)
- **Directory management** — Manage social clubs, internal phone directory, and corporate media accounts
- **Digital card settings** — Configure the digital business card base URL
- **User feedback** — View employee feedback and configure notification email routing
- **Analytics dashboard** — Active user charts, most-viewed news, most-used menu items (from Redis)
- **App description / About Us** — Edit app description and About Us content shown in the mobile app

---

## 2. Project Structure

```
MbtBizizPanel/
├── app/
│   ├── Constants.php              ← Shared constants (same as backend)
│   ├── User.php                   ← Eloquent User model
│   ├── Http/
│   │   ├── Controllers/           ← Feature controllers
│   │   ├── Middleware/
│   │   │   └── AppAzure.php       ← Azure AD OIDC middleware (login/callback/logout)
│   │   └── Requests/
│   ├── Livewire/                  ← Livewire reactive components
│   │   ├── About.php
│   │   ├── AppDescription.php
│   │   ├── Clubs.php
│   │   ├── Medias.php
│   │   └── Phones.php
│   ├── Jobs/                      ← Queue jobs (push notifications)
│   └── Providers/
├── resources/
│   └── views/                     ← Blade templates
├── routes/
│   └── web.php                    ← All web routes
├── config/                        ← Laravel configuration files
├── database/
│   ├── migrations/
│   └── seeds/
├── public/                        ← Web root (index.php, assets)
├── storage/                       ← Logs, uploaded files
├── package.json                   ← Frontend build (Grunt/webpack)
├── webpack.mix.js                 ← Laravel Mix config
├── Gruntfile.js                   ← Grunt task config
└── composer.json
```

---

## 3. Technology Stack

| Component | Detail |
|---|---|
| Language | PHP 8.1 |
| Framework | Laravel 10 |
| Reactive UI | Livewire 3.5 |
| Templating | Blade |
| CSS Framework | Bootstrap 4 |
| Azure AD Auth | `rootinc/laravel-azure-middleware` ^0.9.10 |
| Push Notifications | `edujugon/push-notification` 5.5 |
| Queue / Workers | Laravel Horizon 5.32 |
| Excel Import | `shuchkin/simplexlsx` (payslip period import) |
| Redis | `predis/predis` |
| File Upload | `blueimp/jquery-file-upload` |

---

## 4. Authentication — Azure AD (OIDC)

All panel routes (except `/login`, `/loginOIDC`, `/connection`) are protected by the `azure` middleware.

### Login Flow

```
Admin → GET /login
      → Redirect to Azure AD OIDC consent screen
      → Azure redirects back to /loginOIDC (callback)
      → Session established
      → Redirect to /home
```

### Configuration

| Parameter | Value |
|---|---|
| Middleware | `rootinc/laravel-azure-middleware` |
| Login Route | `GET /login` → `AppAzure@azure` |
| Callback Route | `GET /loginOIDC` → `AppAzure@azurecallback` |
| Logout Route | `GET /logout` → `AppAzure@azurelogout` |
| Tenant ID | `505cca53-5750-4134-9501-8d52d5df3cd1` |
| Resource | `https://graph.microsoft.com/` |
| Scope | `openid` |

Azure AD credentials (`AZURE_CLIENT_ID`, `AZURE_CLIENT_SECRET`, `AZURE_TENANT`) are set in `.env`.

---

## 5. Features & Pages

| Route | Page | Description |
|---|---|---|
| `/home` | Home | Landing page after login |
| `/dashboard` | Dashboard | Analytics: active users, most-viewed news, menu usage |
| `/addnewsEmpty` | Add News | Creates a new empty news record and redirects to edit |
| `/editnews-{id}` | Edit News | Full news editor (title, images, type, targeting, discount codes) |
| `/socialclubs` | Social Clubs | Livewire-powered club directory management |
| `/phones` | Phones | Livewire-powered phone directory management |
| `/medias` | Medias | Livewire-powered corporate media account management |
| `/feedback` | Feedback | View app feedback submissions; manage notification email list |
| `/aboutus` | About Us | Edit About Us content |
| `/about` | About (App) | Livewire component for app info |
| `/appDescription` | App Description | Livewire component for app description text |
| `/digitalcard` | Digital Card | Configure digital business card base URL |
| `/payslip` | Payslip Management | Toggle payslip feature, set error messages, manage periods |
| `/connection` | DB Connection | Database connectivity health check |

---

## 6. Routes Reference

### News Management

| Method | Route | Description |
|---|---|---|
| `GET` | `/addnewsEmpty` | Create new empty news record |
| `GET` | `/editnews-{id}` | Open news editor |
| `POST` | `/savenews` | Create news |
| `POST` | `/editnews` | Update existing news |
| `POST` | `/deleteimage` | Remove image from news |
| `POST` | `/getnewslist` | Get paginated news list (AJAX) |
| `POST` | `/getnews` | Get single news (AJAX) |
| `POST` | `/deletenews` | Delete news |
| `POST` | `/enablenews` | Activate news |
| `POST` | `/disablenews` | Deactivate news |
| `POST` | `/sendnotification` | Send push notification for news |
| `GET` | `/getEmployeeTypeList` | Employee types for filtering |
| `GET` | `/getCompanyList` | Company locations for targeting |
| `GET` | `/getLocationList` | Employee locations for targeting |

### Documents & Discount Codes

| Method | Route | Description |
|---|---|---|
| `POST` | `/addpdf` | Attach PDF document to news |
| `POST` | `/deletepdf` | Remove PDF document |
| `POST` | `/addDiscountCodes` | Upload discount code batch |
| `POST` | `/deleteDiscountCodes` | Remove discount codes |

### Social Clubs

| Method | Route | Description |
|---|---|---|
| `GET` | `/socialclubs` | Livewire clubs management page |
| `POST` | `/addClubLoc` | Add club location |
| `POST` | `/deleteClubLoc` | Delete club location |
| `POST` | `/addClub` | Add club |
| `POST` | `/deleteClub` | Delete club |
| `POST` | `/addClubDetail` | Add club detail/contact |
| `POST` | `/deleteClubDetail` | Delete club detail |
| `POST` | `/updateClubLoc` | Update club location |
| `POST` | `/updateClub` | Update club name |
| `POST` | `/updateClubPersonName` | Update club contact name |
| `POST` | `/updateClubPersonContact` | Update club contact info |

### Phone Directory

| Method | Route | Description |
|---|---|---|
| `GET` | `/phones` | Livewire phones management page |
| `POST` | `/addPhoneLoc` | Add phone location |
| `POST` | `/deletePhoneLoc` | Delete phone location |
| `POST` | `/addPhone` | Add phone entry |
| `POST` | `/deletePhone` | Delete phone entry |
| `POST` | `/addPhoneDetail` | Add phone detail |
| `POST` | `/deletePhoneDetail` | Delete phone detail |
| `POST` | `/updatePhoneLoc` | Update phone location |
| `POST` | `/updatePhone` | Update phone number |
| `POST` | `/updatePhoneUnit` | Update organizational unit |
| `POST` | `/updatePhoneNote` | Update phone note |
| `POST` | `/updatePhoneInternal` | Update internal extension |
| `POST` | `/updateSantral` | Update switchboard info |

### Media

| Method | Route | Description |
|---|---|---|
| `GET` | `/medias` | Livewire media management page |
| `POST` | `/addMedia` | Add media category |
| `POST` | `/deleteMedia` | Delete media category |
| `POST` | `/addMediaDetail` | Add media account |
| `POST` | `/deleteMediaDetail` | Delete media account |
| `POST` | `/updateMedia` | Update media category |
| `POST` | `/updateMediaAccount` | Update media account name |
| `POST` | `/updateMediaUrl` | Update media URL |

### Payslip

| Method | Route | Description |
|---|---|---|
| `GET` | `/payslip` | Payslip management page |
| `POST` | `/payslip/toggle` | Toggle payslip feature on/off |
| `POST` | `/payslip/deactivation-message` | Set deactivation error message |
| `POST` | `/payslip/period-not-open-message` | Set period-not-open error message |
| `POST` | `/payslip/import` | Import payslip period dates from Excel |

### Feedback

| Method | Route | Description |
|---|---|---|
| `GET` | `/feedback` | View app feedback |
| `POST` | `/getFeedbacks` | Get feedback list (AJAX) |
| `POST` | `/updateEmails` | Update feedback notification emails |

---

## 7. Dashboard & Analytics

The **Dashboard** (`/dashboard`) shows real-time analytics from MySQL and Redis:

### KPI Cards

- **Total logged-in users** (since `2025-09-17` — configurable in `DashboardController`)
- **White-collar logged in** / total white-collar
- **Blue-collar logged in** / total blue-collar
- **Other type logged in** / total other

### Charts & Tables

| Widget | Data Source | Description |
|---|---|---|
| Active Users Chart | `user_logins` table | Daily distinct active users over last 30 days |
| Most Viewed News | Redis `viewCountForNews*` keys | Top 20 news items by view count |
| Most Used Menu Items | Redis `menu:view:categories` sorted set | Top 20 menu items, split by employee type |

---

## 8. Payslip Management

The Payslip page (`/payslip`) provides full control over the payslip feature:

### Global Toggle

A Redis key `payslip_active` controls whether the payslip feature is available to all users. The toggle button on this page sets it to `'1'` (active) or `'0'` (inactive).

### Error Messages

Two customizable messages stored in Redis:

| Redis Key | Purpose |
|---|---|
| `payslip_deactivation_error_message` | Shown when payslip is globally off |
| `payslip_period_not_open_error_message` | Shown when selected period is not yet available |

### Payslip Periods

The `payslip_months` table stores per-collar-type availability windows:

| Column | Description |
|---|---|
| `donem` | Period date (first day of month) |
| `yaka_turu` | `'Mavi'` (blue-collar) or `'Beyaz'` (white-collar) |
| `baslangic_tarihi` | When this period becomes available |
| `bitis_tarihi` | When this period closes |

**Import:** Period dates can be bulk-imported via an Excel file upload (`/payslip/import`). Parsed using `shuchkin/simplexlsx`.

---

## 9. Push Notification System

News push notifications are dispatched via a Laravel queue job.

- **Android:** Firebase Cloud Messaging (FCM) — via `edujugon/push-notification` with the FCM server key
- **iOS:** Apple Push Notification Service (APNs) — via P12 certificates stored in `app/` folder

| File | Purpose |
|---|---|
| `app/MbtAppPush22May2025.p12` | APNs push certificate (expires — check renewal date) |
| `app/bizizProduction.pem` | APNs production PEM |
| `app/BizizEnt.pem` | APNs enterprise PEM |
| `app/bizizOld.pem` | Legacy APNs PEM |
| `app/MbtApp.pem` | APNs PEM |

> **Important:** APNs P12/PEM certificates expire. The `MbtAppPush22May2025.p12` name indicates it was created in May 2025 — verify its expiry date and renewal schedule.

Push notifications are sent when:
1. Admin creates/edits news and clicks **Send Notification**
2. The `sendnotification` route triggers the `SendNewsNotification` queue job

---

## 10. Livewire Components

| Component | Route | Features |
|---|---|---|
| `Clubs` | `/socialclubs` | Full CRUD for social club locations, clubs, details, contacts |
| `Phones` | `/phones` | Full CRUD for phone directory locations, entries, extensions |
| `Medias` | `/medias` | Full CRUD for corporate media accounts and URLs |
| `About` | `/about` | Edit About Us content |
| `AppDescription` | `/appDescription` | Edit app description text |

Livewire update route is configured to use the `APP_SUBFOLDER` env variable:
```
POST /{APP_SUBFOLDER}/livewire/update
```

---

## 11. Setup & Local Development

### Prerequisites

- PHP 8.1+
- Composer
- Node.js + npm (for frontend assets)
- MySQL (shared with backend)
- Redis

### Steps

```bash
cd MbtBizizPanel

# Install PHP dependencies
composer install

# Install frontend dependencies
npm install

# Copy and configure environment
cp .env.example .env
php artisan key:generate

# Run migrations
php artisan migrate

# Build frontend assets
npm run dev
# or for production:
npm run prod

# Start server
php artisan serve

# Start queue worker (for push notifications)
php artisan queue:work
# or use Horizon:
php artisan horizon
```

---

## 12. Environment Variables

Key `.env` variables:

```env
APP_ENV=local|staging|production
APP_KEY=...
APP_SUBFOLDER=bizizPanel   # Used in Livewire update route

DB_HOST=...
DB_DATABASE=...
DB_USERNAME=...
DB_PASSWORD=...

REDIS_HOST=127.0.0.1
REDIS_PORT=6379

# Azure AD (OIDC login for panel admins)
AZURE_CLIENT_ID=...
AZURE_CLIENT_SECRET=...
AZURE_TENANT=505cca53-5750-4134-9501-8d52d5df3cd1

# Panel base URLs (for generating image URLs in news)
PANEL_PRODUCTION_BASE_URL=https://bizizapp.com/bizizPanel/public
PANEL_STAGING_BASE_URL=https://biziapp-test.app.daimlertruck.com/bizizPanel/public
PANEL_LOCAL_BASE_URL=http://localhost:8000

# FCM Push (Android)
FCM_SERVER_KEY=...

# APNs Push (iOS) — certificate paths
APNS_CERTIFICATE=...
APNS_CERTIFICATE_PASSPHRASE=...
```
