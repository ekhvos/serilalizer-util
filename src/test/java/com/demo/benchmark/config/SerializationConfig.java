package com.demo.benchmark.config;

import com.demo.benchmark.serializer.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializationConfig {

    public static final String JAVA_SERIALIZATION = "JavaSerialization";
    public static final String KRYO_SERIALIZATION = "KryoSerialization";
    public static final String JACKSON_JSON_SERIALIZATION = "JacksonJsonSerialization";
    public static final String CUSTOM_UTIL_SERIALIZATION = "CustomUtilSerialization";

    private static final ObjectMapper MAPPER = new ObjectMapper()
        .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss X"));

    private static Map<String, Serialization> serializers = new HashMap<String, Serialization>() {{
        put(JAVA_SERIALIZATION, new JavaSerialization());
        put(KRYO_SERIALIZATION, new KryoSerialization());
        put(JACKSON_JSON_SERIALIZATION, new JacksonJsonSerialization());
        put(CUSTOM_UTIL_SERIALIZATION, new CustomUtilSerialization());
    }};

    private List<Object> objectsToSerialize;

    private Map<String, List<byte[]>> objectsToDeserialize = new HashMap<String, List<byte[]>>() {{
        put(JAVA_SERIALIZATION, new ArrayList<>());
        put(KRYO_SERIALIZATION, new ArrayList<>());
        put(JACKSON_JSON_SERIALIZATION, new ArrayList<>());
        put(CUSTOM_UTIL_SERIALIZATION, new ArrayList<>());
    }};

    private String filePath;
    private Class objectRootClass;

    public SerializationConfig(String filePath, Class objectRootClass) throws Exception {
        this.filePath = filePath;
        this.objectRootClass = objectRootClass;

        init();
    }

    /**
     * Setup data for tests.
     */
    protected void init() throws Exception {
        try (InputStream is = SerializationConfig.class.getResourceAsStream(filePath)) {
            objectsToSerialize = MAPPER.readValue(is, MAPPER.getTypeFactory().constructCollectionType(List.class, objectRootClass));

            // fill serialized data for deserialization step
            for (Object object : objectsToSerialize) {
                objectsToDeserialize.get(JAVA_SERIALIZATION).add(serializers.get(JAVA_SERIALIZATION).serialize(object));
                objectsToDeserialize.get(KRYO_SERIALIZATION).add(serializers.get(KRYO_SERIALIZATION).serialize(object));
                objectsToDeserialize.get(JACKSON_JSON_SERIALIZATION).add(serializers.get(JACKSON_JSON_SERIALIZATION).serialize(object));
                objectsToDeserialize.get(CUSTOM_UTIL_SERIALIZATION).add(serializers.get(CUSTOM_UTIL_SERIALIZATION).serialize(object));
            }
        }
    }

    /**
     * Gets sample for serialization.
     *
     * @param index
     * @return
     */
    public Object getSampleToSerialize(int index) {
        return objectsToSerialize.get(index);
    }

    /**
     * Gets sample for deserialization.
     *
     * @param index
     * @return
     */
    public byte[] getSampleToDeserialize(String serializerName, int index) {
        return objectsToDeserialize.get(serializerName).get(index);
    }

    /**
     * Gets serializer.
     *
     * @param serializer
     * @return
     */
    public Serialization getSerializer(String serializer) {
        return serializers.get(serializer);
    }
}
