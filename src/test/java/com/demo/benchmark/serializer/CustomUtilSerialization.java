package com.demo.benchmark.serializer;

import com.demo.Deserializer;
import com.demo.Serializer;

public class CustomUtilSerialization extends Serialization {

    public CustomUtilSerialization() {
        super("Custom serialization-util");
    }

    @Override
    public byte[] serialize(Object object) {
        Serializer serializer = new Serializer();
        return serializer.serialize(object);
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> type) {
        Deserializer deserializer = new Deserializer();
        return deserializer.deserialize(bytes);
    }
}
