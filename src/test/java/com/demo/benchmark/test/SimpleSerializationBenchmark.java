package com.demo.benchmark.test;

import com.demo.benchmark.config.SimpleSerializationConfig;
import com.demo.benchmark.data.UserSimple;
import com.demo.benchmark.serializer.Serialization;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

public class SimpleSerializationBenchmark {

    @Benchmark
    @Measurement(iterations = 100)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 2)
    public void test_serialization(SimpleSerializationConfig config) throws Exception {
        int index = (int) (Math.random() * 100);

        Object sample = config.getSampleToSerialize(index);
        Serialization serializer = config.getSerializer(config.serializerName);

        serializer.serialize(sample);
    }

    @Benchmark
    @Measurement(iterations = 100)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Warmup(iterations = 2)
    public void test_deserialization(SimpleSerializationConfig config) throws Exception {
        int index = (int) (Math.random() * 100);

        byte[] sample = config.getSampleToDeserialize(config.serializerName, index);
        Serialization serializer = config.getSerializer(config.serializerName);

        serializer.deserialize(sample, UserSimple.class);
    }
}
