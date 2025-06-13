package io.github.dfauth.avro;

import org.apache.avro.specific.SpecificRecord;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

public interface Deserializer<T extends SpecificRecord> {

    T deserialize(Class<T> classOfT, byte[] data);

    Map<String,T> deserializeMap(Class<T> targetClass, byte[] bytes);

    List<T> deserializeList(Class<T> currencyPairClass, byte[] bytes);
}
