package com.demo;

/**
 * Serialization/Deserialization utility class.
 */
public class SerializationUtil {
    private Serializer serializer;
    private Deserializer deserializer;

    public SerializationUtil() {
        this.serializer = new Serializer();
        this.deserializer = new Deserializer();
    }

    /**
     * Serialize object to byte array.
     * @param o
     * @return the byte array
     */
    public byte[] serialize(Object o) {
        return serializer.serialize(o);
    }

    /**
     * Deserialize object from array of bytes.
     * @param bytes
     * @return the object
     */
    public Object deserialize(byte[] bytes) {
        return deserializer.deserialize(bytes);
    }
}
