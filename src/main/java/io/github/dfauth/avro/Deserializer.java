package io.github.dfauth.avro;

import org.apache.avro.specific.SpecificRecord;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface Deserializer<T extends SpecificRecord> {

    T deserialize(Class<T> classOfT, byte[] data);

    default Map<String,T> deserializeMap(Class<T> targetClass, byte[] bytes) {
        return deserializeMap(bytes, t -> deserialize(targetClass, t));
    }

    <R> Map<String,R> deserializeMap(byte[] bytes, Function<byte[], R> converter);

    List<T> deserializeList(Class<T> currencyPairClass, byte[] bytes);
}
