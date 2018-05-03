package com.demo.info;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class ClassMetaData {
    private String className;
    private int numOfFields;
    private List<Pair<String, String>> fieldTypeAndName = new LinkedList<>();

    public void addFieldData(String type, String name) {
        fieldTypeAndName.add(new MutablePair(type, name));
    }
}
