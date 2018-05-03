package com.demo.benchmark.test;

import java.io.InputStream;
import java.util.List;

import org.junit.BeforeClass;

import com.demo.benchmark.data.UserComplex;

public class ComplexObjectSerializationTest extends BaseSerializationTest {

    @BeforeClass
    public static void setUpOnce() throws Exception {
        try (InputStream is = BaseSerializationTest.class.getResourceAsStream("/generated_complex.json")) {
            objects = MAPPER.readValue(is, MAPPER.getTypeFactory().constructCollectionType(List.class, UserComplex.class));
        }
    }
}
