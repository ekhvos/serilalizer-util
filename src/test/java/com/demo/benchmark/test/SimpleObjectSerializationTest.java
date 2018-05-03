package com.demo.benchmark.test;

import java.io.InputStream;
import java.util.List;

import org.junit.BeforeClass;

import com.demo.benchmark.data.UserSimple;

public class SimpleObjectSerializationTest extends BaseSerializationTest {

    @BeforeClass
    public static void setUpOnce() throws Exception {
        try (InputStream is = BaseSerializationTest.class.getResourceAsStream("/generated_simple.json")) {
            objects = MAPPER.readValue(is, MAPPER.getTypeFactory().constructCollectionType(List.class, UserSimple.class));
        }
    }
}
