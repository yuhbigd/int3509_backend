package com.project.nhatrotot.util.constant;

import java.util.HashMap;
import java.util.Map;

public class UserConstant {
    public static final Map<String, Integer> USER_ROLES = new HashMap<>();
    public static final Map<String, Integer> USER_TITLE = new HashMap<>();
    static {
        USER_ROLES.put("ADMIN", 1);
        USER_ROLES.put("SUB_ADMIN", 2);
        USER_ROLES.put("CLIENT", 3);

        USER_TITLE.put("admin", 1);
        USER_TITLE.put("verified", 2);
        USER_TITLE.put("client", 3);
    }
}
