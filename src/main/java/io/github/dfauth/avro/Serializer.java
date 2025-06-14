package io.github.dfauth.avro;

import org.apache.avro.specific.SpecificRecord;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface Serializer<T extends SpecificRecord> {

    byte[] serialize(T t);

    default byte[] serializeMap(Map<String, T> map) {
        return serializeMap(map, this::serialize);
    }

    <R> byte[] serializeMap(Map<String, R> map, Function<R,byte[]> converter);

    byte[] serializeList(List<T> src);

    default <R> byte[] serializeList(List<R> map, Function<R,T> converter) {
        return serializeList(map.stream()
                .map(converter)
                .toList());
    }

}
