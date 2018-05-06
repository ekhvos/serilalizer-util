package com.demo.benchmark.serializer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class JacksonJsonSerialization implements Serialization {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper(new JsonFactory());

    @Override
    public byte[] serialize(Object object) throws Exception {
        return JSON_MAPPER.writeValueAsString(object).getBytes();
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> type) throws Exception {
        return JSON_MAPPER.readValue(bytes, type);
    }
}
