package com.demo.benchmark.serializer;

import com.demo.Deserializer;
import com.demo.Serializer;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class CustomUtilSerialization implements Serialization {
    private Serializer serializer = new Serializer();
    private Deserializer deserializer = new Deserializer();

    @Override
    public byte[] serialize(Object object) {
        return serializer.serialize(object);
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> type) {
        return deserializer.deserialize(bytes);
    }
}
