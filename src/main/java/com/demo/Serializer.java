package com.demo;

import static com.demo.util.Tags.ARRAY_BEGIN;
import static com.demo.util.Tags.ARRAY_END;
import static com.demo.util.Tags.ARRAY_VALUE_SEPARATOR;
import static com.demo.util.Tags.CLASS_BEGIN;
import static com.demo.util.Tags.CLASS_BODY_BEGIN;
import static com.demo.util.Tags.CLASS_BODY_END;
import static com.demo.util.Tags.CLASS_END;
import static com.demo.util.Tags.CLASS_META_BEGIN;
import static com.demo.util.Tags.CLASS_META_END;
import static com.demo.util.Tags.COLLECTION_INITIALIZED;
import static com.demo.util.Tags.DEFAULT_SEPARATOR;
import static com.demo.util.Tags.KEY_VALUE_SEPARATOR;
import static com.demo.util.Tags.OBJECT_NULL;
import static com.demo.util.Tags.SERIALIZATION_PROTOCOL;
import static com.demo.util.Types.BASIC_SIMPLE_TYPES;
import static com.demo.util.Types.JAVA_LANG_OBJECT;
import static com.demo.util.Types.JAVA_UTIL_DATE;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Serializer used for serializing objects.
 */
public class Serializer {

    private static final String OBJECT_CANOT_BE_SERIALIZED = "Object canot be serialized";

    /**
     * Serialization of object
     *
     * @param o the object
     * @return byte array
     */
    public byte[] serialize(Object o) {
        try {
            return serializeInternal(o).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(OBJECT_CANOT_BE_SERIALIZED);
        }
    }

    private String serializeInternal(Object o) {
        StringBuilder sb = new StringBuilder(SERIALIZATION_PROTOCOL);

        addClass(sb, o);

        return sb.toString();
    }

    private void addClass(StringBuilder sb, Object o) {
        sb.append(CLASS_BEGIN);

        Class clazz = o.getClass();
        addClassMetaData(sb, clazz, o);
        addParentClassesMetaData(sb, clazz.getSuperclass(), o);
        addClassBody(sb, clazz, o);

        sb.append(CLASS_END);
    }

    private void addClassMetaData(StringBuilder sb, Class clazz, Object o) {
        String className = clazz.getName();
        sb.append(CLASS_META_BEGIN)
            .append(className.length())
            .append(className)
            .append(clazz.getDeclaredFields().length);

        addClassFieldsMetaData(sb, o, clazz.getDeclaredFields());

        sb.append(CLASS_META_END);
    }

    private void addClassFieldsMetaData(StringBuilder sb, Object o, Field[] fields) {
        for (Field field : fields) {
            field.setAccessible(true);

            sb.append(DEFAULT_SEPARATOR)
                .append(getActualFieldType(o, field))
                .append(DEFAULT_SEPARATOR)
                .append(field.getName());
        }
    }

    private void addParentClassMetaData(StringBuilder sb, Class clazz, Object o) {
        sb.append(CLASS_BEGIN);

        addClassMetaData(sb, clazz, o);
        addClassBody(sb, clazz, o);

        sb.append(CLASS_END);
    }

    private void addClassBody(StringBuilder sb, Class clazz, Object o) {
        sb.append(CLASS_BODY_BEGIN);

        addClassFieldsData(sb, clazz, o);

        sb.append(CLASS_BODY_END);
    }

    private void addClassFieldsData(StringBuilder sb, Class clazz, Object o) {
        for (int i = 0; i < clazz.getDeclaredFields().length; i++) {
            Field field = clazz.getDeclaredFields()[i];
            field.setAccessible(true);

            try {
                Object value = field.get(o);
                if (i != 0) {
                    sb.append(DEFAULT_SEPARATOR);
                }
                if (value == null) {
                    sb.append(OBJECT_NULL);
                } else if (BASIC_SIMPLE_TYPES.contains(field.getType().getName())) {
                    sb.append(value);
                } else if (JAVA_UTIL_DATE.equals(field.getType().getName())) {
                    sb.append(((Date) value).getTime());
                } else if (value instanceof Collection) {
                    addCollectionMember(sb, (Collection) value);
                } else if (value instanceof Map) {
                    addMapMember(sb, (Map) value);
                } else {
                    // add full description of object
                    addClass(sb, value);
                }
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(OBJECT_CANOT_BE_SERIALIZED);
            }
        }
    }

    private void addCollectionMember(StringBuilder sb, Collection collection) {
        if (collection == null) {
            sb.append(OBJECT_NULL);
        } else {
            Iterator iterator = collection.iterator();
            Object obj = null;
            if (iterator.hasNext()) {
                obj = iterator.next();

                if (obj != null) {
                    sb.append(obj.getClass().getName());
                }
            }

            sb.append(ARRAY_BEGIN);

            if (obj != null) {
                addBasicTypeOrClass(sb, obj);
            }

            while (iterator.hasNext()) {
                sb.append(ARRAY_VALUE_SEPARATOR);
                addBasicTypeOrClass(sb, iterator.next());
            }

            sb.append(ARRAY_END);
        }
    }

    private void addMapMember(StringBuilder sb, Map map) {
        if (map == null) {
            sb.append(OBJECT_NULL);
        } else {
            Iterator<Map.Entry> iterator = map.entrySet().iterator();
            Map.Entry obj = null;
            if (iterator.hasNext()) {

                obj = iterator.next();

                if (obj != null) {
                    sb.append(obj.getKey().getClass().getName())
                        .append(KEY_VALUE_SEPARATOR)
                        .append(obj.getValue().getClass().getName());
                }
            }

            sb.append(ARRAY_BEGIN);

            if (obj != null) {
                addBasicTypeOrClass(sb, obj.getKey());
                sb.append(KEY_VALUE_SEPARATOR);
                addBasicTypeOrClass(sb, obj.getValue());
            }

            while (iterator.hasNext()) {
                sb.append(ARRAY_VALUE_SEPARATOR);

                obj = iterator.next();
                addBasicTypeOrClass(sb, obj.getKey());
                sb.append(KEY_VALUE_SEPARATOR);
                addBasicTypeOrClass(sb, obj.getValue());
            }

            sb.append(ARRAY_END);
        }
    }

    private void addBasicTypeOrClass(StringBuilder sb, Object obj) {
        if (BASIC_SIMPLE_TYPES.contains(obj.getClass().getName())) {
            sb.append(obj);
        } else {
            addClass(sb, obj);
        }
    }

    private void addParentClassesMetaData(StringBuilder sb, Class clazz, Object o) {
        if (!JAVA_LANG_OBJECT.equals(clazz.getName())) {
            addParentClassMetaData(sb, clazz, o);
            addParentClassesMetaData(sb, clazz.getSuperclass(), o);
        }
    }

    private static String getActualFieldType(Object targetObject, Field field) {
        String typeName = field.getType().getTypeName();

        if (!BASIC_SIMPLE_TYPES.contains(typeName)) {
            try {
                Object object = field.get(targetObject);
                if (object != null) {
                    Type genericType = field.getGenericType();
                    if (genericType instanceof ParameterizedType) {
                        return object.getClass().getTypeName() + COLLECTION_INITIALIZED;
                    }
                }
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(OBJECT_CANOT_BE_SERIALIZED);
            }
        }

        return typeName;
    }
}
