package com.demo.util;

import java.util.HashMap;
import java.util.Map;

public class Tags {
    public static final String DEFAULT_SEPARATOR = "::";
    public static final String SERIALIZATION_PROTOCOL = "CS_SERPRO";
    public static final String OBJECT_NULL = "CS_OBJNUL";
    public static final String CLASS_BEGIN = "CS_CLASSB";
    public static final String CLASS_END = "CS_CLASSE";
    public static final String CLASS_META_BEGIN = "CS_CLMETB";
    public static final String CLASS_META_END = "CS_CLMETE";
    public static final String CLASS_BODY_BEGIN = "CS_CLBODB";
    public static final String CLASS_BODY_END = "CS_CLBODE";
    public static final String ARRAY_BEGIN = "[";
    public static final String ARRAY_END = "]";
    public static final String ARRAY_VALUE_SEPARATOR = ",";
    public static final String KEY_VALUE_SEPARATOR = "||";
    public static final String COLLECTION_INITIALIZED = "!";

    public static final Map<String, String> BEGIN_END_PAIRS = new HashMap<String, String>() {{
        put(CLASS_BEGIN, CLASS_END);
        put(CLASS_META_BEGIN, CLASS_META_END);
        put(CLASS_BODY_BEGIN, CLASS_BODY_END);
        put(ARRAY_BEGIN, ARRAY_END);
    }};
}
