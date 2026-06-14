package com.daimlertruck.dtag.internal.android.mbt.test.utils;

import android.os.Debug;

import com.daimlertruck.dtag.internal.android.mbt.BuildConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class RootUtil {
    private static final Set<String> ROOT_PATHS = new HashSet<>(Arrays.asList(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su",
            "/system/bin/busybox",
            "/system/xbin/busybox",
            "/system/bin/magisk",
            "/system/xbin/magisk",
            "/sbin/magisk",
            "/data/adb/magisk",
            "/data/local/tmp/frida-server",
            "/system/bin/frida-server",
            "/system/xbin/frida-server"
    ));

    private static final String[] ROOT_COMMANDS = {"su", "busybox", "magisk"};
    private static final String[] RW_PATHS = {
            "/system",
            "/system/bin",
            "/system/sbin",
            "/system/xbin",
            "/vendor/bin",
            "/vendor",
            "/sbin",
            "/odm"
    };

    private RootUtil() {
    }

    public static boolean isDeviceCompromised() {
        if (BuildConfig.DEBUG) {
            return false;
        }

        return isDeviceRooted() || isTamperingDetected();
    }

    public static boolean isDeviceRooted() {
        return checkBuildTags()
                || checkRootFiles()
                || checkRootCommands()
                || checkDangerousProps()
                || checkRwMounts();
    }

    public static boolean isTamperingDetected() {
        return checkDebuggerAttached()
                || checkTracerPid()
                || checkFridaServerPort();
    }

    private static boolean checkBuildTags() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRootFiles() {
        for (String path : ROOT_PATHS) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkRootCommands() {
        for (String command : ROOT_COMMANDS) {
            if (canResolveBinary(command)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkDangerousProps() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("getprop");
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if ((line.contains("[ro.debuggable]") && line.contains("[1]"))
                        || (line.contains("[ro.secure]") && line.contains("[0]"))) {
                    return true;
                }
            }
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
        return false;
    }

    private static boolean checkRwMounts() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("mount");
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if (isRwMountLine(line)) {
                    return true;
                }
            }
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
        return false;
    }

    private static boolean isRwMountLine(String line) {
        String lowerLine = line.toLowerCase(Locale.US);
        for (String path : RW_PATHS) {
            if (lowerLine.contains(" " + path.toLowerCase(Locale.US) + " ")
                    && (lowerLine.contains(" rw,") || lowerLine.endsWith(" rw") || lowerLine.contains("(rw,"))) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkDebuggerAttached() {
        return Debug.isDebuggerConnected() || Debug.waitingForDebugger();
    }

    private static boolean checkTracerPid() {
        try (BufferedReader reader = new BufferedReader(new FileReader("/proc/self/status"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("TracerPid:")) {
                    String value = line.substring("TracerPid:".length()).trim();
                    return !"0".equals(value);
                }
            }
        } catch (Exception ignored) {
            return false;
        }
        return false;
    }

    private static boolean checkFridaServerPort() {
        return isLocalPortOpen(27042) || isLocalPortOpen(27043);
    }

    private static boolean isLocalPortOpen(int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("127.0.0.1", port), 200);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private static boolean canResolveBinary(String command) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"which", command});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return in.readLine() != null;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }
}