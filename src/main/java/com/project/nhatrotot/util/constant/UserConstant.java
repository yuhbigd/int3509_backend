package com.project.nhatrotot.util.constant;

import java.util.HashMap;
import java.util.Map;

public class UserConstant {
    public static final Map<String, Long> USER_ROLES_MAP = new HashMap<>();
    public static final Map<String, Long> USER_TITLE_MAP = new HashMap<>();

    static {
        USER_ROLES_MAP.put("admin", Long.valueOf(1));
        USER_ROLES_MAP.put("sub_admin", Long.valueOf(2));
        USER_ROLES_MAP.put("client", Long.valueOf(3));

        USER_TITLE_MAP.put("admin", Long.valueOf(1));
        USER_TITLE_MAP.put("verified", Long.valueOf(2));
        USER_TITLE_MAP.put("client", Long.valueOf(3));
    }
    public final static String ROLE_ADMIN = "admin";
    public final static String ROLE_SUB_ADMIN = "sub_admin";
    public final static String ROLE_CLIENT = "client";

    public final static String TITLE_ADMIN = "admin";
    public final static String TITLE_VERIFIED = "verified";
    public final static String TITLE_CLIENT = "client";
}
