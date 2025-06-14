package io.github.dfauth.avro;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.util.List;
import java.util.Map;

@Slf4j
public class RedisSerde<T extends SpecificRecord> extends SerdeImpl<T> {

    public RedisSerializer<Map<String, T>> createMapSerializer(Class<T> classOfT) {

        return new RedisSerializer<>() {
            @Override
            public byte[] serialize(Map<String, T> value) throws SerializationException {
                return serializeMap(value);
            }

            @Override
            public Map<String, T> deserialize(byte[] bytes) throws SerializationException {
                return deserializeMap(classOfT, bytes);
            }

        };
    }

    public RedisSerializer<List<T>> createListSerializer(Class<T> classOfT) {

        return new RedisSerializer<>() {
            @Override
            public byte[] serialize(List<T> value) throws SerializationException {
                return serializeList(value);
            }

            @Override
            public List<T> deserialize(byte[] bytes) throws SerializationException {
                return deserializeList(classOfT, bytes);
            }

        };
    }

    public RedisSerializer<T> createAvroSerializer(Class<T> classOfT) {

        return new RedisSerializer<>() {
            @Override
            public byte[] serialize(T value) throws SerializationException {
                return serializeInternal(value);
            }

            @Override
            public T deserialize(byte[] bytes) throws SerializationException {
                return deserializeInternal(classOfT, bytes);
            }

        };
    }
}
