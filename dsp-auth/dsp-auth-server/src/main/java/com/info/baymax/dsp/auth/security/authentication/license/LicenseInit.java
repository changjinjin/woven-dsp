package com.info.baymax.dsp.auth.security.authentication.license;

import java.util.HashMap;
import java.util.Map;

public class LicenseInit {
    private static String PUBLICALIAS = "publiccert";
    private static String STOREPWD = "inforefiner123";
    private static String SUBJECT = "license";
    private static String pubPath = "/publicCerts.store";

    public static Map<String, String> getParamers() {
        Map<String, String> paramerts = new HashMap<>();
        paramerts.put("PUBLICALIAS", PUBLICALIAS);
        paramerts.put("STOREPWD", STOREPWD);
        paramerts.put("SUBJECT", SUBJECT);
        paramerts.put("pubPath", pubPath);
        return paramerts;
    }

}
