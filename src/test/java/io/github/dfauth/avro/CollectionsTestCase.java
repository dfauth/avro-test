package io.github.dfauth.avro;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.Map;

import static io.github.dfauth.avro.Status.ACTIVE;
import static org.junit.Assert.assertEquals;

@Slf4j
public class CollectionsTestCase {

    @Test
    public void testIt() {

        SerdeImpl<CurrencyPair> serde = new SerdeImpl<>();

        Map<String,CurrencyPair> src = Map.of("EUR/USD", CurrencyPair.newBuilder().setBase("EUR").setAlt("USD").build());
        byte[] bytes = serde.serializeMap(src);
        log.info("bytes: {}", new String(Base64.getEncoder().encode(bytes)));
        Map<String,CurrencyPair> dest = serde.deserializeMap(CurrencyPair.class, bytes);
        assertEquals(src, dest);
    }

}
