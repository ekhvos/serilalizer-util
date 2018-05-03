package com.demo.util;

import java.util.HashMap;
import java.util.Map;

public class Tags {
    public static final String DEFAULT_SEPARATOR = "::";
    public static final String SERIALIZATION_PROTOCOL = "#SERPRO";
    public static final String OBJECT_NULL = "#OBJNUL";
    public static final String CLASS_BEGIN = "#CLASBE";
    public static final String CLASS_END = "#CLASEN";
    public static final String CLASS_META_BEGIN = "#METABE";
    public static final String CLASS_META_END = "#METAEN";
    public static final String CLASS_BODY_BEGIN = "#BODYBE";
    public static final String CLASS_BODY_END = "#BODYEN";
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
