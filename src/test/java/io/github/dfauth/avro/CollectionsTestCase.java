package io.github.dfauth.avro;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@Slf4j
public class CollectionsTestCase {

    @Test
    public void testMap() {

        Serde<CurrencyPair> serde = new SerdeImpl<>();

        Map<String,CurrencyPair> src = Map.of("EUR/USD", CurrencyPair.newBuilder().setBase("EUR").setTerm("USD").build());
        byte[] bytes = serde.serializeMap(src);
        log.info("bytes: {}", new String(Base64.getEncoder().encode(bytes)));
        Map<String,CurrencyPair> dest = serde.deserializeMap(CurrencyPair.class, bytes);
        assertEquals(src, dest);
    }

    @Test
    public void testList() {

        Serde<CurrencyPair> serde = new SerdeImpl<>();

        List<CurrencyPair> src = List.of(CurrencyPair.newBuilder().setBase("EUR").setTerm("USD").build());
        byte[] bytes = serde.serializeList(src);
        log.info("bytes: {}", new String(Base64.getEncoder().encode(bytes)));
        List<CurrencyPair> dest = serde.deserializeList(CurrencyPair.class, bytes);
        assertEquals(src, dest);
    }

    @Test
    public void testMapOfList() {

        Serde<CurrencyPair> serde = new SerdeImpl<>();

        Map<String,List<CurrencyPair>> src = Map.of("EUR/USD", List.of(CurrencyPair.newBuilder().setBase("EUR").setTerm("USD").build()));
        byte[] bytes = serde.serializeMap(src, serde::serializeList);
        log.info("bytes: {}", new String(Base64.getEncoder().encode(bytes)));
        Map<String, List<CurrencyPair>> dest = serde.deserializeMap(bytes, b -> serde.deserializeList(CurrencyPair.class, b));
        assertEquals(src, dest);
    }

    @Test
    public void testListOfMap() {

        Serde<CurrencyPair> serde = new SerdeImpl<>();

        List<Map<String, CurrencyPair>> src = List.of(Map.of("EUR/USD", CurrencyPair.newBuilder().setBase("EUR").setTerm("USD").build()));
        byte[] bytes = serde.serializeList(src, serde::serializeMap);
        log.info("bytes: {}", new String(Base64.getEncoder().encode(bytes)));
        List<Map<String, CurrencyPair>> dest = serde.deserializeList(bytes, b -> serde.deserializeMap(CurrencyPair.class, b));
        assertEquals(src, dest);
    }

}
