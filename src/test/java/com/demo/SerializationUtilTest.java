package com.demo;

import com.demo.data.SerialTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class SerializationUtilTest {
    private SerializationUtil serializationUtil = new SerializationUtil();

    @Test
    public void check_serialization_deserialization() {
        //GIVEN
        SerialTest serialTest = new SerialTest();
        serialTest.setVersion(11);
        serialTest.setParentVersion(22);
        serialTest.setIntegerValue(33);
        serialTest.setLongValue(null);
        serialTest.setEmptyIntegerListInitAfter(new ArrayList<>());
        serialTest.getContain().setContainVersion(44444);

        //WHEN
        byte[] bytes = serializationUtil.serialize(serialTest);
        Object object = serializationUtil.deserialize(bytes);

        //THEN
        Assert.assertTrue(serialTest.equals(object));
    }
}
