package com.demo.initializer;

import static com.demo.util.Tags.COLLECTION_INITIALIZED;

import java.util.HashMap;
import java.util.Map;

import com.demo.initializer.impl.BooleanFieldInitializer;
import com.demo.initializer.impl.ByteFieldInitializer;
import com.demo.initializer.impl.CharFieldInitializer;
import com.demo.initializer.impl.CollectionFieldInitializer;
import com.demo.initializer.impl.DateFieldInitializer;
import com.demo.initializer.impl.DoubleFieldInitializer;
import com.demo.initializer.impl.FloatFieldInitializer;
import com.demo.initializer.impl.IntegerFieldInitializer;
import com.demo.initializer.impl.LongFieldInitializer;
import com.demo.initializer.impl.MapFieldInitializer;
import com.demo.initializer.impl.ObjectFieldInitializer;
import com.demo.initializer.impl.ShortFieldInitializer;
import com.demo.initializer.impl.StringFieldInitializer;
import com.demo.util.Types;

/**
 * Field Initialization Factory.
 */
public final class FieldInitializationFactory {

    private static final Map<String, FieldInitializer> typeToInitializer = new HashMap<>();

    static {
        typeToInitializer.put("byte", new ByteFieldInitializer());
        typeToInitializer.put("java.lang.Byte", new ByteFieldInitializer());
        typeToInitializer.put("short", new ShortFieldInitializer());
        typeToInitializer.put("java.lang.Short", new ShortFieldInitializer());
        typeToInitializer.put("int", new IntegerFieldInitializer());
        typeToInitializer.put("java.lang.Integer", new IntegerFieldInitializer());
        typeToInitializer.put("long", new LongFieldInitializer());
        typeToInitializer.put("java.lang.Long", new LongFieldInitializer());
        typeToInitializer.put("float", new FloatFieldInitializer());
        typeToInitializer.put("java.lang.Float", new FloatFieldInitializer());
        typeToInitializer.put("double", new DoubleFieldInitializer());
        typeToInitializer.put("java.lang.Double", new DoubleFieldInitializer());
        typeToInitializer.put("char", new CharFieldInitializer());
        typeToInitializer.put("java.lang.Character", new CharFieldInitializer());
        typeToInitializer.put("boolean", new BooleanFieldInitializer());
        typeToInitializer.put("java.lang.Boolean", new BooleanFieldInitializer());
        typeToInitializer.put("java.lang.String", new StringFieldInitializer());
        typeToInitializer.put("java.util.Date", new DateFieldInitializer());

        typeToInitializer.put("java.lang.Object", new ObjectFieldInitializer());
    }

    /**
     * Gets field initializer by field type.
     *
     * @param type type of field
     * @return field initializer
     */
    public static FieldInitializer getFieldInitializer(String type) {
        FieldInitializer fieldInitializer = typeToInitializer.get(type);
        if (fieldInitializer == null) {
            if (type.endsWith(COLLECTION_INITIALIZED)) {
                String actualCollectionType = type.substring(0, type.indexOf(COLLECTION_INITIALIZED));

                //TODO fix distinguishing mechanism between mapSimpleComplexTypes and collection
                if (actualCollectionType.endsWith("Map")) {
                    fieldInitializer = new MapFieldInitializer();
                } else {
                    fieldInitializer = new CollectionFieldInitializer();
                }
            } else {
                return typeToInitializer.get(Types.JAVA_LANG_OBJECT);
            }
        }

        return fieldInitializer;
    }
}
