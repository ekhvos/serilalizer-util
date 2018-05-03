package com.demo.benchmark.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class JavaSerialization extends Serialization {

    public JavaSerialization() {
        super("Java serialization");
    }

    @Override
    public byte[] serialize(Object object) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
        }
        return baos.toByteArray();
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> type) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        }
    }
}
