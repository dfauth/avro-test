package io.github.dfauth.avro;

import org.apache.avro.specific.SpecificRecord;

import java.nio.ByteBuffer;

public interface Deserializer<T extends SpecificRecord> {

    T deserialize(Class<T> classOfT, byte[] data);
}
