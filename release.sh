#!/usr/bin/env bash
set -euo pipefail

# ============================================================================
# MBT App — Build & FTP Upload Script
# ============================================================================
# Usage:
#   ./release.sh [options]
#
# Options:
#   --platform   ios | android | all       (default: all)
#   --env        staging | prod | all      (default: all)
#   --skip-build Skip build, only upload existing artifacts
#   --skip-upload Skip upload, only build
#   --dry-run    Show what would be done without executing
#   -h, --help   Show this help
#
# Examples:
#   ./release.sh --platform android --env staging
#   ./release.sh --platform ios --env prod
#   ./release.sh                                        # build & upload everything
#   ./release.sh --skip-build --env prod                # upload existing artifacts only
# ============================================================================

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ANDROID_DIR="$SCRIPT_DIR/MbtBizizAndroid"
IOS_DIR="$SCRIPT_DIR/MbtBizizIos"
ARTIFACTS_DIR="$SCRIPT_DIR/release_artifacts"

# ── Upload Configuration ─────────────────────────────────────────────────────
# Credentials (host, user, pass) are loaded from .release-credentials.
# Remote directories are defined here per-script.

CREDENTIALS_FILE="$SCRIPT_DIR/.release-credentials"
if [[ ! -f "$CREDENTIALS_FILE" ]]; then
    echo "[ERROR] Missing credentials file: $CREDENTIALS_FILE"
    echo "        Copy .release-credentials.example to .release-credentials and fill in the values."
    exit 1
fi
# shellcheck source=.release-credentials
source "$CREDENTIALS_FILE"

# Staging Server
FTP_STAGING_PROTOCOL="sftp"
FTP_STAGING_HOST="192.168.24.16"
FTP_STAGING_PORT="22"
FTP_STAGING_ANDROID_REMOTE_DIR="/var/www/html/app/bin__2.0"
FTP_STAGING_IOS_REMOTE_DIR="/var/www/html/app/bin__2.0"

# Prod Server
FTP_PROD_PROTOCOL="sftp"
FTP_PROD_HOST="192.168.24.26"
FTP_PROD_PORT="22"
FTP_PROD_ANDROID_REMOTE_DIR="/var/www/html/app/bin__2.0"
FTP_PROD_IOS_REMOTE_DIR="/var/www/html/app/bin__2.0"

# ── Output File Names ──────────────────────────────────────────────────────
# The final file names to use when uploading to the FTP server.
# You can use these variables: {version}, {build}, {date}
# They will be replaced automatically during the script run.

APK_STAGING_FILENAME="mbt_app.apk"
APK_PROD_FILENAME="mbt_app.apk"
IPA_STAGING_FILENAME="MBT_App.ipa"
IPA_PROD_FILENAME="MBT_App.ipa"

# ── iOS Build Settings ─────────────────────────────────────────────────────
IOS_WORKSPACE="MBTBiziz.xcworkspace"
IOS_STAGING_SCHEME="MBTBiziz-Staging"
IOS_PROD_SCHEME="MBTBiziz-Production"
# Export method: enterprise, ad-hoc, app-store, development
IOS_STAGING_EXPORT_METHOD="enterprise"
IOS_PROD_EXPORT_METHOD="enterprise"
# Team ID
IOS_TEAM_ID="J7U9JQP7Q6"
# Optional: Provisioning profile names (leave empty for automatic)
IOS_STAGING_PROVISIONING_PROFILE="MbtApp7"
IOS_PROD_PROVISIONING_PROFILE="MbtApp7"

# ── Colors & Helpers ────────────────────────────────────────────────────────
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

log_info()    { echo -e "${BLUE}[INFO]${NC}  $*"; }
log_success() { echo -e "${GREEN}[OK]${NC}    $*"; }
log_warn()    { echo -e "${YELLOW}[WARN]${NC}  $*"; }
log_error()   { echo -e "${RED}[ERROR]${NC} $*"; }
log_step()    { echo -e "\n${CYAN}━━━ $* ━━━${NC}\n"; }

# ── Parse Arguments ─────────────────────────────────────────────────────────
PLATFORM="all"
ENV="all"
SKIP_BUILD=false
SKIP_UPLOAD=false
DRY_RUN=false

while [[ $# -gt 0 ]]; do
    case "$1" in
        --platform)  PLATFORM="$2";   shift 2 ;;
        --env)       ENV="$2";        shift 2 ;;
        --skip-build)  SKIP_BUILD=true; shift ;;
        --skip-upload) SKIP_UPLOAD=true; shift ;;
        --dry-run)   DRY_RUN=true;    shift ;;
        -h|--help)
            sed -n '2,/^# ====/{ /^# ====/d; s/^# //; s/^#//; p }' "$0"
            exit 0
            ;;
        *) log_error "Unknown option: $1"; exit 1 ;;
    esac
done

# ── Validate ────────────────────────────────────────────────────────────────
if [[ "$PLATFORM" != "ios" && "$PLATFORM" != "android" && "$PLATFORM" != "all" ]]; then
    log_error "Invalid platform: $PLATFORM (use ios, android, or all)"
    exit 1
fi
if [[ "$ENV" != "staging" && "$ENV" != "prod" && "$ENV" != "all" ]]; then
    log_error "Invalid env: $ENV (use staging, prod, or all)"
    exit 1
fi

# ── Resolve Version Info ────────────────────────────────────────────────────
get_android_version() {
    local gradle_file="$ANDROID_DIR/app/build.gradle.kts"
    local version_name version_code
    version_name=$(grep 'versionName' "$gradle_file" | head -1 | sed 's/.*"\(.*\)".*/\1/')
    version_code=$(grep 'versionCode' "$gradle_file" | head -1 | sed 's/[^0-9]*//g')
    echo "$version_name|$version_code"
}

get_ios_version() {
    local pbxproj="$IOS_DIR/MBTBiziz.xcodeproj/project.pbxproj"
    local version_name build_number
    version_name=$(grep 'MARKETING_VERSION' "$pbxproj" | grep -v 'Dev' | head -1 | sed 's/.*= *\(.*\);.*/\1/' | tr -d ' "')
    build_number=$(grep 'CURRENT_PROJECT_VERSION' "$pbxproj" | grep -v 'Dev' | head -1 | sed 's/.*= *\(.*\);.*/\1/' | tr -d ' "')
    echo "$version_name|$build_number"
}

resolve_filename() {
    local template="$1"
    local version="$2"
    local build="$3"
    local date_str
    date_str=$(date +"%Y%m%d")
    echo "$template" | sed "s/{version}/$version/g; s/{build}/$build/g; s/{date}/$date_str/g"
}

# ── Build Functions ─────────────────────────────────────────────────────────

build_android() {
    local env="$1"  # staging or prod
    local flavor task apk_path

    if [[ "$env" == "staging" ]]; then
        flavor="staging"
        task="assembleStagingRelease"
    else
        flavor="prod"
        task="assembleProdRelease"
    fi

    log_step "Building Android ($env)"

    if $DRY_RUN; then
        log_info "[DRY-RUN] Would run: ./gradlew $task"
        return 0
    fi

    pushd "$ANDROID_DIR" > /dev/null
    ./gradlew clean "$task" --no-daemon
    popd > /dev/null

    # Find the output APK
    apk_path=$(find "$ANDROID_DIR/app/build/outputs/apk/$flavor/release" -name "*.apk" 2>/dev/null | head -1)

    if [[ -z "$apk_path" ]]; then
        log_error "APK not found for $env build!"
        return 1
    fi

    # Copy and rename
    local android_info final_name
    android_info=$(get_android_version)
    local ver="${android_info%%|*}"
    local build_num="${android_info##*|}"

    if [[ "$env" == "staging" ]]; then
        final_name=$(resolve_filename "$APK_STAGING_FILENAME" "$ver" "$build_num")
    else
        final_name=$(resolve_filename "$APK_PROD_FILENAME" "$ver" "$build_num")
    fi

    mkdir -p "$ARTIFACTS_DIR/android/$env"
    cp "$apk_path" "$ARTIFACTS_DIR/android/$env/$final_name"
    log_success "APK ready: $ARTIFACTS_DIR/android/$env/$final_name"
}

build_ios() {
    local env="$1"  # staging or prod
    local scheme export_method provisioning_profile
    local archive_path export_path

    local code_sign_style="Automatic"
    local sign_args=()

    if [[ "$env" == "staging" ]]; then
        scheme="$IOS_STAGING_SCHEME"
        export_method="$IOS_STAGING_EXPORT_METHOD"
        provisioning_profile="$IOS_STAGING_PROVISIONING_PROFILE"
        code_sign_style="Manual"
    else
        scheme="$IOS_PROD_SCHEME"
        export_method="$IOS_PROD_EXPORT_METHOD"
        provisioning_profile="$IOS_PROD_PROVISIONING_PROFILE"
        code_sign_style="Manual"
    fi

    sign_args=(CODE_SIGN_STYLE="$code_sign_style" DEVELOPMENT_TEAM="$IOS_TEAM_ID")
    if [[ "$code_sign_style" == "Manual" && -n "$provisioning_profile" ]]; then
        sign_args+=(PROVISIONING_PROFILE_SPECIFIER="$provisioning_profile")
    fi

    log_step "Building iOS ($env) — Scheme: $scheme (signing: $code_sign_style)"

    archive_path="$ARTIFACTS_DIR/ios/$env/$scheme.xcarchive"
    export_path="$ARTIFACTS_DIR/ios/$env/export"

    if $DRY_RUN; then
        log_info "[DRY-RUN] Would archive: $scheme → $archive_path"
        log_info "[DRY-RUN] Would export IPA to: $export_path"
        return 0
    fi

    mkdir -p "$ARTIFACTS_DIR/ios/$env"

    # Generate ExportOptions.plist
    local export_plist="$ARTIFACTS_DIR/ios/$env/ExportOptions.plist"
    generate_export_plist "$export_method" "$provisioning_profile" "$export_plist" "$env"

    # Archive
    pushd "$IOS_DIR" > /dev/null
    xcodebuild archive \
        -workspace "$IOS_WORKSPACE" \
        -scheme "$scheme" \
        -configuration Release \
        -archivePath "$archive_path" \
        -destination "generic/platform=iOS" \
        "${sign_args[@]}" \
        | xcbeautify 2>/dev/null || xcodebuild archive \
        -workspace "$IOS_WORKSPACE" \
        -scheme "$scheme" \
        -configuration Release \
        -archivePath "$archive_path" \
        -destination "generic/platform=iOS" \
        "${sign_args[@]}"
    popd > /dev/null

    # Export IPA
    xcodebuild -exportArchive \
        -archivePath "$archive_path" \
        -exportPath "$export_path" \
        -exportOptionsPlist "$export_plist"

    # Find the IPA
    local ipa_path
    ipa_path=$(find "$export_path" -name "*.ipa" 2>/dev/null | head -1)

    if [[ -z "$ipa_path" ]]; then
        log_error "IPA not found for $env build!"
        return 1
    fi

    # Rename
    local ios_info final_name
    ios_info=$(get_ios_version)
    local ver="${ios_info%%|*}"
    local build_num="${ios_info##*|}"

    if [[ "$env" == "staging" ]]; then
        final_name=$(resolve_filename "$IPA_STAGING_FILENAME" "$ver" "$build_num")
    else
        final_name=$(resolve_filename "$IPA_PROD_FILENAME" "$ver" "$build_num")
    fi

    cp "$ipa_path" "$ARTIFACTS_DIR/ios/$env/$final_name"
    log_success "IPA ready: $ARTIFACTS_DIR/ios/$env/$final_name"
}

generate_export_plist() {
    local method="$1"
    local provisioning="$2"
    local output="$3"
    local env="$4"

    local bundle_id
    if [[ "$env" == "staging" ]]; then
        bundle_id="com.daimlertruck.dtag.internal.ios.mbt.test2"
    else
        bundle_id="com.daimlertruck.dtag.internal.ios.mbt.test"
    fi

    cat > "$output" << PLIST
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>method</key>
    <string>${method}</string>
    <key>teamID</key>
    <string>${IOS_TEAM_ID}</string>
    <key>compileBitcode</key>
    <false/>
    <key>stripSwiftSymbols</key>
    <true/>
    <key>thinning</key>
    <string>&lt;none&gt;</string>
PLIST

    # Add provisioning profile mapping if specified
    if [[ -n "$provisioning" ]]; then
        cat >> "$output" << PLIST
    <key>provisioningProfiles</key>
    <dict>
        <key>${bundle_id}</key>
        <string>${provisioning}</string>
    </dict>
PLIST
    fi

    cat >> "$output" << PLIST
</dict>
</plist>
PLIST
}

# ── Upload Functions ─────────────────────────────────────────────────────────

sftp_upload() {
    local host="$1"
    local port="$2"
    local user="$3"
    local pass="$4"
    local remote_dir="$5"
    local local_file="$6"
    local remote_filename
    remote_filename=$(basename "$local_file")

    if $DRY_RUN; then
        log_info "[DRY-RUN] Would upload: $local_file → sftp://$host:$port$remote_dir/$remote_filename"
        return 0
    fi

    if ! command -v sshpass &> /dev/null; then
        log_error "sshpass is required for SFTP upload but not found!"
        log_error "Install with: brew install hudochenkov/sshpass/sshpass"
        return 1
    fi

    log_info "Uploading $remote_filename → sftp://$host:$port$remote_dir/"

    # Ensure remote directory exists, then upload
    sshpass -p "$pass" sftp -oBatchMode=no -oStrictHostKeyChecking=no -oPort="$port" "${user}@${host}" <<SFTP_BATCH
-mkdir ${remote_dir}
put ${local_file} ${remote_dir}/${remote_filename}
bye
SFTP_BATCH

    if [[ $? -eq 0 ]]; then
        log_success "Uploaded: $remote_filename"
    else
        log_error "Failed to upload: $remote_filename"
        return 1
    fi
}

ftp_upload() {
    local host="$1"
    local port="$2"
    local user="$3"
    local pass="$4"
    local remote_dir="$5"
    local local_file="$6"
    local remote_filename
    remote_filename=$(basename "$local_file")

    if $DRY_RUN; then
        log_info "[DRY-RUN] Would upload: $local_file → ftp://$host:$port$remote_dir/$remote_filename"
        return 0
    fi

    if ! command -v curl &> /dev/null; then
        log_error "curl is required for FTP upload but not found!"
        return 1
    fi

    log_info "Uploading $remote_filename → ftp://$host:$port$remote_dir/"

    curl --ftp-create-dirs \
         -T "$local_file" \
         "ftp://${host}:${port}${remote_dir}/${remote_filename}" \
         --user "${user}:${pass}" \
         --connect-timeout 30 \
         --max-time 600 \
         --retry 3 \
         --retry-delay 5 \
         --progress-bar

    if [[ $? -eq 0 ]]; then
        log_success "Uploaded: $remote_filename"
    else
        log_error "Failed to upload: $remote_filename"
        return 1
    fi
}

upload_artifact() {
    local platform="$1"  # android or ios
    local env="$2"       # staging or prod
    local protocol host port user pass remote_dir artifact_dir pattern

    if [[ "$env" == "staging" ]]; then
        protocol="$FTP_STAGING_PROTOCOL"
        host="$FTP_STAGING_HOST"
        port="$FTP_STAGING_PORT"
        user="$FTP_STAGING_USER"
        pass="$FTP_STAGING_PASS"
        if [[ "$platform" == "android" ]]; then
            remote_dir="$FTP_STAGING_ANDROID_REMOTE_DIR"
        else
            remote_dir="$FTP_STAGING_IOS_REMOTE_DIR"
        fi
    else
        protocol="$FTP_PROD_PROTOCOL"
        host="$FTP_PROD_HOST"
        port="$FTP_PROD_PORT"
        user="$FTP_PROD_USER"
        pass="$FTP_PROD_PASS"
        if [[ "$platform" == "android" ]]; then
            remote_dir="$FTP_PROD_ANDROID_REMOTE_DIR"
        else
            remote_dir="$FTP_PROD_IOS_REMOTE_DIR"
        fi
    fi

    artifact_dir="$ARTIFACTS_DIR/$platform/$env"

    if [[ "$platform" == "android" ]]; then
        pattern="*.apk"
    else
        pattern="*.ipa"
    fi

    local file
    file=$(find "$artifact_dir" -maxdepth 1 -name "$pattern" -type f 2>/dev/null | sort -r | head -1)

    if [[ -z "$file" ]]; then
        log_error "No $pattern found in $artifact_dir. Build first!"
        return 1
    fi

    if [[ "$protocol" == "sftp" ]]; then
        sftp_upload "$host" "$port" "$user" "$pass" "$remote_dir" "$file"
    else
        ftp_upload "$host" "$port" "$user" "$pass" "$remote_dir" "$file"
    fi
}

# ── Main ────────────────────────────────────────────────────────────────────

main() {
    log_step "MBT App Release Script"
    log_info "Platform : $PLATFORM"
    log_info "Env      : $ENV"
    log_info "Skip Build  : $SKIP_BUILD"
    log_info "Skip Upload : $SKIP_UPLOAD"
    log_info "Dry Run     : $DRY_RUN"

    local envs=()
    if [[ "$ENV" == "all" ]]; then
        envs=(staging prod)
    else
        envs=("$ENV")
    fi

    local platforms=()
    if [[ "$PLATFORM" == "all" ]]; then
        platforms=(android ios)
    else
        platforms=("$PLATFORM")
    fi

    # ── Build Phase ──
    if ! $SKIP_BUILD; then
        for platform in "${platforms[@]}"; do
            for env in "${envs[@]}"; do
                if [[ "$platform" == "android" ]]; then
                    build_android "$env"
                else
                    build_ios "$env"
                fi
            done
        done
    else
        log_warn "Skipping build phase (--skip-build)"
    fi

    # ── Upload Phase ──
    if ! $SKIP_UPLOAD; then
        for platform in "${platforms[@]}"; do
            for env in "${envs[@]}"; do
                log_step "Uploading $platform ($env)"
                upload_artifact "$platform" "$env"
            done
        done
    else
        log_warn "Skipping upload phase (--skip-upload)"
    fi

    log_step "Done!"
    log_success "All operations completed."
}

main
