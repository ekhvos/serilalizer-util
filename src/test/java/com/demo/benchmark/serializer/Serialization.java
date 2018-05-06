package com.demo.benchmark.serializer;

public interface Serialization {
    /**
     * Serialize object.
     */
    byte[] serialize(Object object) throws Exception;

    /**
     * Deserialize object.
     */
    Object deserialize(byte[] bytes, Class<?> type) throws Exception;
}
