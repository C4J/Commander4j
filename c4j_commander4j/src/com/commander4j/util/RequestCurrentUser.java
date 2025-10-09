package com.commander4j.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Secur32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;

/**
 * Cross-platform, authoritative current-user resolver.
 *
 * Windows (desktop or service):
 *   - Uses the PROCESS TOKEN, so thread impersonation cannot change the result.
 *   - Returns only the username (domain omitted). Examples:
 *       "MYDOM\\svc_commander4j" -> "svc_commander4j"
 *       "NT AUTHORITY\\SYSTEM"   -> "SYSTEM"
 *
 * macOS/Linux:
 *   - Uses getpwuid(geteuid()) via libc.
 *
 * Fallbacks:
 *   - whoami (with domain prefix stripped on Windows)
 *   - System property user.name as a last resort
 */
public final class RequestCurrentUser {

    private RequestCurrentUser() {}

    /** Returns only the username (domain omitted), using authoritative OS sources. */
    public static String getAuthoritativeUsername() {
        String login = null;

        if (Platform.isWindows()) {
            // 1) Service-safe: read the process token so impersonation can't affect result
            login = getWindowsUsernameFromProcessToken();  // username only
            if (isBlank(login)) {
                // 2) Fallback to SAM-compatible (DOMAIN\\user), then strip domain
                String sam = getWindowsSamCompatible();
                if (!isBlank(sam)) login = stripDomainPrefix(sam);
            }
            if (isBlank(login)) {
                // 3) Plain GetUserNameW
                login = getWindowsUsernameClassic();
            }
            if (isBlank(login)) {
                // 4) whoami (strip domain if present)
                login = stripDomainPrefix(whoAmI());
            }
        } else {
            // POSIX (macOS/Linux/FreeBSD/Unix)
            login = getPosixLogin();
            if (isBlank(login)) login = whoAmI();
        }

        if (isBlank(login)) {
            login = System.getProperty("user.name", "Unknown");
        }

        return login;
    }

    // ------------------------------------------------------------------
    // Windows: PROCESS TOKEN (service-safe, unaffected by impersonation)
    // ------------------------------------------------------------------
// ------------------------------------------------------------------
// Windows: PROCESS TOKEN (service-safe, unaffected by impersonation)
// ------------------------------------------------------------------
// ------------------------------------------------------------------
// Windows: PROCESS TOKEN (service-safe, unaffected by impersonation)
// ------------------------------------------------------------------
private static String getWindowsUsernameFromProcessToken() {
    WinNT.HANDLEByReference hTokenRef = new WinNT.HANDLEByReference();
    boolean ok = Advapi32.INSTANCE.OpenProcessToken(
            Kernel32.INSTANCE.GetCurrentProcess(),
            WinNT.TOKEN_QUERY,
            hTokenRef
    );
    if (!ok) return null;

    try {
        // 1) Query required size (Structure can be null here)
        IntByReference retLen = new IntByReference();
        Advapi32.INSTANCE.GetTokenInformation(
                hTokenRef.getValue(),
                WinNT.TOKEN_INFORMATION_CLASS.TokenUser,
                null,                // <-- pass null Structure (not Pointer.NULL)
                0,
                retLen
        );
        int needed = retLen.getValue();
        if (needed <= 0) return null;

        // 2) Allocate buffer and wrap it as a TOKEN_USER Structure
        Memory mem = new Memory(needed);
        WinNT.TOKEN_USER tokenUser = new WinNT.TOKEN_USER(mem);

        ok = Advapi32.INSTANCE.GetTokenInformation(
                hTokenRef.getValue(),
                WinNT.TOKEN_INFORMATION_CLASS.TokenUser,
                tokenUser,           // <-- pass the Structure
                (int) mem.size(),
                retLen
        );
        if (!ok) return null;

        tokenUser.read();

        // NOTE: field name is "User" (capital U). SID type is PSID, not SID.
        WinNT.PSID sid = tokenUser.User.Sid;

        com.sun.jna.platform.win32.Advapi32Util.Account acc =
                com.sun.jna.platform.win32.Advapi32Util.getAccountBySid(sid);

        return (acc != null) ? acc.name : null; // username only
    } catch (Throwable t) {
        return null;
    } finally {
        Kernel32.INSTANCE.CloseHandle(hTokenRef.getValue());
    }
}



    // Returns DOMAIN\\Username (SAM-compatible) from the current thread token
    // Caller should strip the domain if only the username is needed.
    private static String getWindowsSamCompatible() {
        char[] buf = new char[512];
        IntByReference size = new IntByReference(buf.length);
        boolean ok = Secur32.INSTANCE.GetUserNameEx(
                Secur32.EXTENDED_NAME_FORMAT.NameSamCompatible, buf, size);
        if (!ok) return null;
        return new String(buf, 0, size.getValue());
    }

    // Plain username via GetUserNameW (thread context)
    private static String getWindowsUsernameClassic() {
        char[] buf = new char[256];
        IntByReference size = new IntByReference(buf.length);
        boolean ok = Advapi32.INSTANCE.GetUserNameW(buf, size);
        if (!ok) return null;
        int len = Math.max(0, size.getValue() - 1); // size includes trailing NUL
        return new String(buf, 0, len);
    }

    // -------------------------------------
    // POSIX (macOS/Linux/FreeBSD/Unix)
    // -------------------------------------
    private static String getPosixLogin() {
        try {
            int euid = LibC.INSTANCE.geteuid();
            Pointer p = LibC.INSTANCE.getpwuid(euid);
            if (p == null) return null;

            Structure pw = Platform.isMac() ? new PasswdDarwin(p) : new PasswdLinux(p);
            pw.read();

            if (pw instanceof PasswdLinux pl) {
                return (pl.pw_name != null) ? pl.pw_name.getString(0) : null;
            } else {
                PasswdDarwin pd = (PasswdDarwin) pw;
                return (pd.pw_name != null) ? pd.pw_name.getString(0) : null;
            }
        } catch (Throwable t) {
            return null;
        }
    }

    // -------------------------
    // Fallbacks / helpers
    // -------------------------
    private static String whoAmI() {
        try {
            Process p = new ProcessBuilder("whoami").redirectErrorStream(true).start();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line = br.readLine();
                if (line == null) return null;
                // On Windows: "DOMAIN\\user" or similar. On POSIX: "user".
                return stripDomainPrefix(line.trim());
            }
        } catch (Exception ignored) {}
        return null;
    }

    /** Removes a leading "DOMAIN\\" or "domain/" prefix if present. */
    private static String stripDomainPrefix(String name) {
        if (name == null) return null;
        int backslash = name.indexOf('\\');
        int slash = name.indexOf('/');
        int sep = (backslash >= 0) ? backslash : slash;
        if (sep >= 0 && sep < name.length() - 1) {
            return name.substring(sep + 1);
        }
        return name;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    // -------- libc bindings & passwd structs --------
    public interface LibC extends Library {
        LibC INSTANCE = Native.load("c", LibC.class);
        int geteuid();
        Pointer getpwuid(int uid); // struct passwd*
    }

    /** GNU/Linux passwd layout:
     *  struct passwd {
     *      char *pw_name;   char *pw_passwd; int pw_uid; int pw_gid;
     *      char *pw_gecos;  char *pw_dir;   char *pw_shell;
     *  };
     */
    public static class PasswdLinux extends Structure {
        public Pointer pw_name;
        public Pointer pw_passwd;
        public int pw_uid;
        public int pw_gid;
        public Pointer pw_gecos;
        public Pointer pw_dir;
        public Pointer pw_shell;

        public PasswdLinux() {}
        public PasswdLinux(Pointer p) { super(p); }
        @Override protected List<String> getFieldOrder() {
            return List.of("pw_name","pw_passwd","pw_uid","pw_gid","pw_gecos","pw_dir","pw_shell");
        }
    }

    /** macOS/BSD passwd layout (fields ordered to match Darwin libc):
     *  struct passwd {
     *      char *pw_name;   char *pw_passwd; int pw_uid; int pw_gid;
     *      time_t pw_change; char *pw_class; char *pw_gecos; char *pw_dir;
     *      char *pw_shell;  time_t pw_expire;
     *  };
     */
    public static class PasswdDarwin extends Structure {
        public Pointer pw_name;
        public Pointer pw_passwd;
        public int pw_uid;
        public int pw_gid;
        public NativeLong pw_change; // time_t
        public Pointer pw_class;
        public Pointer pw_gecos;
        public Pointer pw_dir;
        public Pointer pw_shell;
        public NativeLong pw_expire; // time_t

        public PasswdDarwin() {}
        public PasswdDarwin(Pointer p) { super(p); }
        @Override protected List<String> getFieldOrder() {
            return List.of("pw_name","pw_passwd","pw_uid","pw_gid",
                           "pw_change","pw_class","pw_gecos","pw_dir","pw_shell","pw_expire");
        }
    }

    // Tiny demo for command-line use
    public static void main(String[] args) {
        System.out.println(getAuthoritativeUsername());
    }
}
