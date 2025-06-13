package io.github.dfauth.avro;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;

import java.nio.ByteBuffer;
import java.util.Map;

public interface Serializer<T extends SpecificRecord> {

    byte[] serialize(T t);

    byte[] serializeMap(Map<String, T> map);
}
