package com.demo.benchmark.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import com.demo.benchmark.data.Friend;
import com.demo.benchmark.data.Post;
import com.demo.benchmark.data.UserComplex;
import com.demo.benchmark.data.UserSimple;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;

public class KryoSerialization extends Serialization {

    public KryoSerialization() {
        super("Kryo");
    }

    private static final KryoFactory KRYO_FACTORY = new KryoFactory() {
        @Override
        public Kryo create() {
            Kryo kryo = new Kryo();
            kryo.register(UserSimple.class);
            kryo.register(UserComplex.class);
            kryo.register(Friend.class);
            kryo.register(Post.class);
            kryo.register(Date.class);
            kryo.register(ArrayList.class);
            return kryo;
        }
    };

    private static final KryoPool KRYO_POOL = new KryoPool.Builder(KRYO_FACTORY)
        .softReferences()
        .build();

    public KryoSerialization(String name) {
        super(name);
    }

    public Input getInput(InputStream is) {
        return new Input(is);
    }

    public Output getOutput(OutputStream os) {
        return new Output(os);
    }

    @Override
    public byte[] serialize(Object object) throws Exception {
        Kryo kryo = KRYO_POOL.borrow();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (Output output = getOutput(baos)) {
                kryo.writeObject(output, object);
            }
            return baos.toByteArray();
        } finally {
            KRYO_POOL.release(kryo);
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> type) throws Exception {
        Kryo kryo = KRYO_POOL.borrow();
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            try (Input input = getInput(bais)) {
                return kryo.readObject(input, type);
            }
        } finally {
            KRYO_POOL.release(kryo);
        }
    }
}
