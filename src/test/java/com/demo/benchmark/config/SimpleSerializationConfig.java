package com.demo.benchmark.config;

import com.demo.benchmark.data.UserSimple;
import com.demo.benchmark.serializer.Serialization;
import org.openjdk.jmh.annotations.*;

import static com.demo.benchmark.config.SerializationConfig.*;

@State(Scope.Benchmark)
public class SimpleSerializationConfig {
    private SerializationConfig config;

    @Param({JAVA_SERIALIZATION, KRYO_SERIALIZATION, JACKSON_JSON_SERIALIZATION, CUSTOM_UTIL_SERIALIZATION})
    public String serializerName;

    @Setup(Level.Invocation)
    public void setUp() throws Exception {
        this.config = new SerializationConfig("/generated_simple.json", UserSimple.class);
    }

    /**
     * Gets sample for serialization.
     *
     * @param index
     * @return
     */
    public Object getSampleToSerialize(int index) {
        return config.getSampleToSerialize(index);
    }

    /**
     * Gets sample for deserialization.
     *
     * @param index
     * @return
     */
    public byte[] getSampleToDeserialize(String serializerName, int index) {
        return config.getSampleToDeserialize(serializerName, index);
    }

    /**
     * Gets serializer.
     *
     * @param serializer
     * @return
     */
    public Serialization getSerializer(String serializer) {
        return config.getSerializer(serializer);
    }

}
