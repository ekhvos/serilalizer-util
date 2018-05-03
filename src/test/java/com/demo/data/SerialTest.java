package com.demo.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class SerialTest extends Parent {
    private int version = 66;
    private char charValue = 'c';
    private byte byteValue = 1;
    private short shortValue = 2;
    private float floatValue = 3.3F;
    private double doubleValue = 4.4;
    private boolean booleaValue = true;
    private Integer integerValue = 777;
    private Long longValue = 9L;
    private List<Integer> nullIntegerList;
    private List<Integer> emptyIntegerList = new ArrayList<>();
    private List<Integer> emptyIntegerListInitAfter;
    private List<Integer> integerListInitConstructor;
    private Contain contain = new Contain();
    private List<Contain> nullContainList;
    private List<Contain> emptyContainList = new ArrayList<>();
    private List<Contain> emptyContainListInitConstructor = new ArrayList<>();
    private Map<String, Contain> nullMap;
    private Map<String, Contain> emptyMap = new HashMap<>();
    private Map<String, Contain> mapSimpleComplexTypes = new HashMap<>();
    private Map<Contain, Contain> mapComplexComplexTypes = new HashMap<>();
    private Map<Contain, String> mapComplexSimpleTypes = new HashMap<>();
    private Date dateValue = new Date();

    public SerialTest() {
        integerListInitConstructor = new ArrayList<>();
        integerListInitConstructor.add(42);
        integerListInitConstructor.add(77);

        emptyContainListInitConstructor.add(new Contain());
        emptyContainListInitConstructor.add(contain);
        Contain containForList = new Contain();
        containForList.setContainVersion(888);
        emptyContainListInitConstructor.add(containForList);

        mapSimpleComplexTypes.put("1", contain);
        mapSimpleComplexTypes.put("2", contain);
        mapSimpleComplexTypes.put("3", contain);

        mapComplexComplexTypes.put(contain, contain);
        mapComplexComplexTypes.put(new Contain(), new Contain());
        Contain containForMap = new Contain();
        containForMap.setContainVersion(333);
        mapComplexComplexTypes.put(containForMap, containForMap);

        mapComplexSimpleTypes.put(contain, "1");
        mapComplexSimpleTypes.put(new Contain(), "2");
        mapComplexSimpleTypes.put(containForMap, "3");

    }
}
