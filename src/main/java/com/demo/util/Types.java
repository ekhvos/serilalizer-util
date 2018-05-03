package com.demo.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Types {

    public static final String JAVA_LANG_OBJECT = "java.lang.Object";
    public static final String JAVA_UTIL_DATE = "java.util.Date";
    public static final String JAVA_LANG_STRING = "java.lang.String";

    public static final Map<String, String> PRIMITIVES_TO_WRAPPERS = new HashMap<String, String>() {{
        put("byte", "java.lang.Byte");
        put("short", "java.lang.Short");
        put("int", "java.lang.Integer");
        put("long", "java.lang.Long");
        put("float", "java.lang.Float");
        put("double", "java.lang.Double");
        put("char", "java.lang.Character");
        put("boolean", "java.lang.Boolean");
    }};

    public static final Set<String> BASIC_SIMPLE_TYPES = new HashSet<String>() {{
        addAll(PRIMITIVES_TO_WRAPPERS.keySet());
        addAll(PRIMITIVES_TO_WRAPPERS.values());
        add(JAVA_LANG_STRING);
    }};
}
