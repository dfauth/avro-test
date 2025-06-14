package io.github.dfauth.avro;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@Slf4j
public class RedisSerializationTestCase {

    @Test
    public void testCurrencyPair() {

        CurrencyPair in = CurrencyPair.newBuilder().setBase("USD").setTerm("EUR").build();

        RedisSerde<CurrencyPair> redisSerde = new RedisSerde<>();

        SerializationPair<CurrencyPair> pair = SerializationPair.fromSerializer(redisSerde.createAvroSerializer(CurrencyPair.class));

        byte[] bytes = pair.getWriter().write(in).array();

        CurrencyPair out = pair.getReader().read(ByteBuffer.wrap(bytes));

        assertEquals(in, out);

    }

    @Test
    public void testCurrencyMapOfPair() {

        Map<String, CurrencyPair> in = Map.of("EUR/USD", CurrencyPair.newBuilder().setBase("USD").setTerm("EUR").build());

        RedisSerde<CurrencyPair> redisSerde = new RedisSerde<>();

        SerializationPair<Map<String, CurrencyPair>> pair = SerializationPair.fromSerializer(redisSerde.createMapSerializer(CurrencyPair.class));

        byte[] bytes = pair.getWriter().write(in).array();

        Map<String, CurrencyPair> out = pair.getReader().read(ByteBuffer.wrap(bytes));

        assertEquals(in, out);

    }

    @Test
    public void testCurrencyListOfPair() {

        List<CurrencyPair> in = List.of(CurrencyPair.newBuilder().setBase("USD").setTerm("EUR").build());

        RedisSerde<CurrencyPair> redisSerde = new RedisSerde<>();

        SerializationPair<List<CurrencyPair>> pair = SerializationPair.fromSerializer(redisSerde.createListSerializer(CurrencyPair.class));

        byte[] bytes = pair.getWriter().write(in).array();

        List<CurrencyPair> out = pair.getReader().read(ByteBuffer.wrap(bytes));

        assertEquals(in, out);

    }

}
