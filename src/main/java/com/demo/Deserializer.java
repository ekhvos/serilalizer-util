package com.demo;

import com.demo.info.ClassMetaData;
import com.demo.initializer.FieldInitializationFactory;
import com.demo.initializer.FieldInitializer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.*;

import static com.demo.util.Tags.*;
import static com.demo.util.Types.BASIC_SIMPLE_TYPES;

/**
 * Deserializer used for deserializing objects from stream.
 */
public class Deserializer {

    private static final int CS_TAG_LENGTH = 9;
    private static final String OBJECT_CANNOT_BE_DESERIALIZED_EXCEPTION = "Object cannot be deserialized";
    private static final String KEY_VALUE_SEPARATOR_REGEX = "\\|\\|";

    /**
     * Deserializes object from byte array.
     *
     * @param bytes the byte array
     * @return the object
     */
    public Object deserialize(byte[] bytes) {
        try {
            StringReader reader = new StringReader(new String(bytes, "UTF-8"));

            if (!isSerializedStream(reader)) {
                throw new IllegalArgumentException(OBJECT_CANNOT_BE_DESERIALIZED_EXCEPTION);
            }

            Object targetObject = null;
            String tag = readControlSymbol(reader);
            return readClassData(reader, tag, targetObject, null);
        } catch (Exception e) {
            throw new IllegalArgumentException(OBJECT_CANNOT_BE_DESERIALIZED_EXCEPTION);
        }
    }

    private Object readClassData(StringReader reader, String currentTag, Object targetObject, Class<?> currentClass)
        throws IOException, ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        String tag = currentTag;
        Class<?> clazz = currentClass;

        // start reading root class info
        if (CLASS_BEGIN.equals(tag)) {
            tag = readControlSymbol(reader);

            // start reading root class metadata
            ClassMetaData targetClassMetaData = null;
            while (CLASS_META_BEGIN.equals(tag)) {
                targetClassMetaData = readClassMetaData(reader, tag);

                tag = readControlSymbol(reader);
            }

            // Class meta data should not be null
            if (targetClassMetaData == null) {
                throw new IllegalArgumentException(OBJECT_CANNOT_BE_DESERIALIZED_EXCEPTION);
            }

            // init root object with default initialization
            if (targetObject == null) {
                targetObject = initTargetObject(targetClassMetaData);
                clazz = targetObject.getClass();
            }

            // read info about parent classes
            while (CLASS_BEGIN.equals(tag)) {
                readClassData(reader, tag, targetObject, clazz.getSuperclass());

                tag = readControlSymbol(reader);
            }

            // parent classes info ended
            if (CLASS_END.equals(tag)) {
                tag = readControlSymbol(reader);
            }

            // start reading root class fields information
            if (CLASS_BODY_BEGIN.equals(tag)) {
                if (targetClassMetaData.getNumOfFields() > 0) {
                    Object[] objects = readBody(reader, BEGIN_END_PAIRS.get(tag), targetClassMetaData.getNumOfFields());
                    initObjectBaseFields(targetClassMetaData, targetObject, clazz, objects);
                }

                // read CLASS_BODY_END
                readControlSymbol(reader);
            }
        } else {
            throw new IllegalArgumentException(OBJECT_CANNOT_BE_DESERIALIZED_EXCEPTION);
        }

        return targetObject;
    }

    private Object initTargetObject(ClassMetaData classMetaData) throws ClassNotFoundException,
        IllegalAccessException, InstantiationException {
        Class<?> clazz = Class.forName(classMetaData.getClassName());
        return clazz.newInstance();
    }

    private Object initObjectBaseFields(ClassMetaData classMetaData, Object targetObject, Class<?> currentClass, Object[] values)
        throws IllegalAccessException, NoSuchFieldException {
        if (classMetaData.getNumOfFields() != values.length) {
            throw new IllegalArgumentException(OBJECT_CANNOT_BE_DESERIALIZED_EXCEPTION);
        }

        Iterator<Pair<String, String>> fieldTypeNameIter = classMetaData.getFieldTypeAndName().iterator();
        for (int i = 0; i < values.length; i++) {
            Pair<String, String> fieldTypeName = fieldTypeNameIter.next();
            Field field = currentClass.getDeclaredField(fieldTypeName.getValue());
            field.setAccessible(true);

            // initialize field with proper value using particular field initializer
            FieldInitializer fieldInitializer = FieldInitializationFactory.getFieldInitializer(fieldTypeName.getKey());
            fieldInitializer.init(field, fieldTypeName.getKey(), targetObject, getValueOrNull(values[i]));
        }

        return targetObject;
    }

    private ClassMetaData readClassMetaData(StringReader reader, String currentTag) throws IOException {
        if (CLASS_META_BEGIN.equals(currentTag)) {
            StringBuilder classNameLength = new StringBuilder();
            char symbol = (char) reader.read();

            // read class name length
            while (isNumeric(symbol)) {
                classNameLength.append(symbol);

                symbol = (char) reader.read();
            }

            // As symbol variable already contains first letter of classname - retrieve only amount of letters left
            char[] className = new char[Integer.valueOf(classNameLength.toString()) - 1];
            cleanBuffer(classNameLength);
            reader.read(className);

            ClassMetaData classMetaData = new ClassMetaData();
            classMetaData.setClassName(symbol + new String(className));

            fillClassFieldsMetaData(reader, classMetaData);

            return classMetaData;
        } else {
            throw new IllegalArgumentException(OBJECT_CANNOT_BE_DESERIALIZED_EXCEPTION);
        }
    }

    private void fillClassFieldsMetaData(StringReader reader, ClassMetaData classMetaData) throws IOException {
        String classFieldsDescription = readUntilAnyTag(reader, BEGIN_END_PAIRS.get(CLASS_META_BEGIN)).getKey();
        String[] classFields = classFieldsDescription.split(DEFAULT_SEPARATOR);

        Integer numOfFields = Integer.valueOf(classFields[0]);
        classMetaData.setNumOfFields(numOfFields);

        for (int i = 1; i <= numOfFields * 2; i += 2) {
            classMetaData.addFieldData(classFields[i], classFields[i + 1]);
        }
    }

    private Object[] readBody(StringReader reader, String endTag, int numberOfFields)
        throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Object[] objects = new Object[numberOfFields];
        StringBuilder data = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        char[] tmpChar = new char[1];
        int i = 0;
        while (!endTag.equals(temp.toString())) {
            // read character
            reader.read(tmpChar);

            // workaround to fix clashes when string finished with same characters as CLASS_BEGIN tag
            if (':' == tmpChar[0] && CLASS_BEGIN.startsWith(temp.toString())) {
                data.append(temp.toString());
                cleanBuffer(temp);
            }

            temp.append(tmpChar);

            if (DEFAULT_SEPARATOR.startsWith(temp.toString()) && DEFAULT_SEPARATOR.equals(temp.toString())) {
                // if first object has basic type
                if (data.toString().length() > 0) {
                    objects[i] = data.toString();
                    i++;

                    cleanBuffer(temp);
                    cleanBuffer(data);
                }
            } else if (CLASS_BEGIN.equals(temp.toString())) {
                Object obj = readClassData(reader, CLASS_BEGIN, null, null);
                objects[i] = obj;

                cleanBuffer(temp);
                cleanBuffer(data);

                i++;

                // switch to next class member and bypass default separator for fields
                reader.read(tmpChar);
                temp.append(tmpChar);
                if (DEFAULT_SEPARATOR.startsWith(temp.toString())) {
                    reader.read(tmpChar);
                    cleanBuffer(temp);
                }
            } else if (data.toString().endsWith("[")) {
                // process complex types aka collections and maps

                String collectionMapType = data.toString().substring(0, data.toString().length() - 1);
                boolean isMapObj = collectionMapType.contains(KEY_VALUE_SEPARATOR);

                if (temp.toString().endsWith("]")) {
                    data.append(temp.toString());
                    cleanBuffer(temp);
                } else if (!onlyBasicType(collectionMapType.split(KEY_VALUE_SEPARATOR_REGEX))) {

                    String[] collectionMapTypes = collectionMapType.split(KEY_VALUE_SEPARATOR_REGEX);

                    if (isMapObj) {
                        objects[i] = deserializeComplexMap(reader, temp, collectionMapTypes);
                    } else {
                        objects[i] = deserializeComplexCollection(reader, temp, collectionMapTypes);
                    }

                    cleanBuffer(temp);
                    cleanBuffer(data);

                    i++;

                    // switch to next class member
                    reader.read(tmpChar);
                    temp.append(tmpChar);
                    if (DEFAULT_SEPARATOR.startsWith(temp.toString())) {
                        reader.read(tmpChar);
                        cleanBuffer(temp);
                    }
                }
            } else if (!DEFAULT_SEPARATOR.startsWith(temp.toString())
                && !CLASS_BEGIN.startsWith(temp.toString())
                && !endTag.startsWith(temp.toString())) {
                data.append(temp.toString());
                cleanBuffer(temp);
            }

        }

        if (i < numberOfFields) {
            objects[i] = data.toString();
        }

        return objects;
    }

    private Object deserializeComplexCollection(StringReader reader, StringBuilder temp, String[] collectionMapTypes)
        throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Collection<Object> collection = new ArrayList<>();
        // read control symbols left for class identification for collection
        char[] tmpChar = new char[CS_TAG_LENGTH - 1];
        reader.read(tmpChar);
        temp.append(tmpChar);
        // set char buffer to 1 symbol
        tmpChar = new char[1];

        //read first object of array
        Object key = readCollectionMapData(reader, collectionMapTypes[0], temp, true);//readClassData(reader, CLASS_BEGIN, null, null);

        //clean up control symbol buffer and read next symbol
        cleanBuffer(temp);
        reader.read(tmpChar);
        temp.append(tmpChar);

        //if collection contains only one element just add it to collection
        if ("]".equals(temp.toString()) || ARRAY_VALUE_SEPARATOR.equals(temp.toString())) {
            collection.add(key);
        }

        while (!"]".equals(temp.toString())) {
            if (ARRAY_VALUE_SEPARATOR.equals(temp.toString())) {
                //clean up control symbol buffer
                cleanBuffer(temp);

                key = readClassData(reader, readControlSymbol(reader), null, null);
                collection.add(key);

                if (!BASIC_SIMPLE_TYPES.contains(collectionMapTypes[0])) {
                    cleanBuffer(temp);
                }
            }

            if (temp.toString().isEmpty()
                || (!ARRAY_VALUE_SEPARATOR.equals(temp.toString())
                && !"]".equals(temp.toString()))) {
                reader.read(tmpChar);
                temp.append(tmpChar);
            }
        }

        return collection;
    }

    private Object deserializeComplexMap(StringReader reader, StringBuilder temp, String[] collectionMapTypes)
        throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Map<Object, Object> map = new HashMap<>();
        char[] tmpChar = new char[1];
        //read first object of mapSimpleComplexTypes
        Object key = readCollectionMapData(reader, collectionMapTypes[0], temp, true);//readClassData(reader, CLASS_BEGIN, null, null);
        Object value = null;

        // read next 2 symbols which should be key/value separator
        if (temp.toString().isEmpty()) {
            reader.read(tmpChar);
            temp.append(tmpChar);

            reader.read(tmpChar);
            temp.append(tmpChar);
        }


        if (KEY_VALUE_SEPARATOR.equals(temp.toString())) {
            cleanBuffer(temp);
            value = readCollectionMapData(reader, collectionMapTypes[1], temp, false);
            map.put(key, value);

            if (temp.toString().isEmpty()) {
                reader.read(tmpChar);
                temp.append(tmpChar);
            }
        }

        while (!"]".equals(temp.toString())) {
            if (KEY_VALUE_SEPARATOR.startsWith(temp.toString()) && KEY_VALUE_SEPARATOR.equals(temp.toString())) {
                //clean up control symbol buffer
                cleanBuffer(temp);
                value = readCollectionMapData(reader, collectionMapTypes[1], temp, false);

                map.put(key, value);

                if (!ARRAY_VALUE_SEPARATOR.equals(temp.toString()) && !"]".equals(temp.toString())) {
                    cleanBuffer(temp);
                }
            } else if (ARRAY_VALUE_SEPARATOR.equals(temp.toString())) {
                //clean up control symbol buffer
                cleanBuffer(temp);

                key = readCollectionMapData(reader, collectionMapTypes[0], temp, true);

                if (!BASIC_SIMPLE_TYPES.contains(collectionMapTypes[0])) {
                    cleanBuffer(temp);
                }
            }

            if (temp.toString().isEmpty()
                || (!KEY_VALUE_SEPARATOR.equals(temp.toString())
                && !ARRAY_VALUE_SEPARATOR.equals(temp.toString())
                && !"]".equals(temp.toString()))) {
                reader.read(tmpChar);
                temp.append(tmpChar);
            }
        }

        return map;
    }

    /**
     * Read data from stream until any tag appears.
     *
     * @param reader
     * @param endTags
     * @return
     * @throws IOException
     */
    private Pair<String, String> readUntilAnyTag(StringReader reader, String... endTags) throws IOException {
        StringBuilder data = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        char[] tmpChar = new char[1];
        while (!matchAnyTag(temp, endTags)) {
            // read character
            reader.read(tmpChar);
            temp.append(tmpChar);

            if (!startsWithAnyTag(temp, endTags)) {
                data.append(temp.toString());
                cleanBuffer(temp);
            }
        }

        return new MutablePair<>(data.toString(), temp.toString());
    }

    private static boolean matchAnyTag(StringBuilder sb, String... endTags) {
        String buffer = sb.toString();
        for (String tag : endTags) {
            if (buffer.equals(tag)) {
                return true;
            }
        }

        return false;
    }

    private static boolean startsWithAnyTag(StringBuilder sb, String... endTags) {
        String buffer = sb.toString();
        for (String tag : endTags) {
            if (tag.startsWith(buffer)) {
                return true;
            }
        }

        return false;
    }

    private static String readControlSymbol(StringReader reader) throws IOException {
        char[] csBuffer = new char[CS_TAG_LENGTH];
        reader.read(csBuffer);
        return new String(csBuffer);
    }

    private static boolean isSerializedStream(StringReader reader) throws IOException {
        char[] csBuffer = new char[CS_TAG_LENGTH];
        reader.read(csBuffer);
        return SERIALIZATION_PROTOCOL.equals(new String(csBuffer));
    }

    private static boolean isNumeric(char symbol) {
        return StringUtils.isNumeric(String.valueOf(symbol));
    }

    private static Object getValueOrNull(Object value) {
        return OBJECT_NULL.equals(value) ? null : value;
    }

    private static boolean onlyBasicType(String[] types) {
        for (String type : types) {
            if (!BASIC_SIMPLE_TYPES.contains(type)) {
                return false;
            }
        }

        return true;
    }

    private static void cleanBuffer(StringBuilder sb) {
        sb.delete(0, sb.toString().length());
    }

    private Object readCollectionMapData(StringReader reader, String type, StringBuilder temp, boolean isKey)
        throws ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException, IOException {
        if (BASIC_SIMPLE_TYPES.contains(type)) {
            String[] separators = isKey ? new String[]{KEY_VALUE_SEPARATOR} : new String[]{ARRAY_VALUE_SEPARATOR, "]"};

            FieldInitializer fieldInitializer = FieldInitializationFactory.getFieldInitializer(type);
            Pair<String, String> valueAndTag = readUntilAnyTag(reader, separators);
            String value = temp.toString() + valueAndTag.getKey();

            //clean up temp buffer, set key-value separator
            cleanBuffer(temp);
            temp.append(valueAndTag.getValue());

            return fieldInitializer.convert(value);
        } else {

            String tag = temp.toString();
            if (tag.length() > 0) {
                char[] chars = new char[CS_TAG_LENGTH - tag.length()];
                reader.read(chars);
                tag = temp.append(chars).toString();
            } else {
                tag = readControlSymbol(reader);
            }

            //clean up temp buffer
            cleanBuffer(temp);

            return readClassData(reader, tag, null, null);
        }
    }
}
