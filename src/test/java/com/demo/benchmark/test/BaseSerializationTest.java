package com.demo.benchmark.test;

import java.text.SimpleDateFormat;
import java.util.Collection;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.demo.benchmark.serializer.CustomUtilSerialization;
import com.demo.benchmark.serializer.JacksonJsonSerialization;
import com.demo.benchmark.serializer.JavaSerialization;
import com.demo.benchmark.serializer.KryoSerialization;
import com.demo.benchmark.serializer.Serialization;
import com.demo.benchmark.serializer.SerializationExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class BaseSerializationTest {

    private static final int ITERATIONS = Integer.parseInt(System.getProperty("serialization.iterations"));
    
    protected static final ObjectMapper MAPPER = new ObjectMapper()
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss X"));
    
    protected static Collection<Object> objects;

    private void executeSerialization(Serialization serialization) throws Exception {
        SerializationExecutor executor = new SerializationExecutor(serialization);
        executor.execute(objects, ITERATIONS);
        executor.printStatistics();
    }

    @Test
    public void test01_javaSerialization() throws Exception {
        executeSerialization(new JavaSerialization());
    }

    @Test
    public void test02_kryo() throws Exception {
        executeSerialization(new KryoSerialization());
    }

    @Test
    public void test03_jacksonJson() throws Exception {
        executeSerialization(new JacksonJsonSerialization());
    }

    @Test
    public void test04_serialization_util() throws Exception {
        executeSerialization(new CustomUtilSerialization());
    }
}
