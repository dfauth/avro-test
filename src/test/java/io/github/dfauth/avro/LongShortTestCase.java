package io.github.dfauth.avro;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Base64;

import static org.junit.Assert.assertEquals;

@Slf4j
public class LongShortTestCase {

    @Test
    public void testUnchanged() {

        CurrencyPair in = CurrencyPair.newBuilder().setBase("EUR").setTerm("USD").build();

        {
            byte[] bytes = new SerdeImpl<>().serialize(in);
            log.info("bytes: {}", new String(Base64.getEncoder().encode(bytes)));
            CurrencyPairV2 out = new SerdeImpl<CurrencyPairV2>().deserialize(CurrencyPairV2.class, bytes);
            assertEquals(in.getBase(), out.getBase());
            assertEquals(in.getTerm(), out.getTerm());
        }

    }

    @Test
    public void testLong() {

        CurrencyPairV3 in = CurrencyPairV3.newBuilder().setBase("EUR").setTerm("USD").setAnotherField("blah").build();

        byte[] bytes = new SerdeImpl<>().serialize(in);
        log.info("bytes: {}", new String(Base64.getEncoder().encode(bytes)));
        CurrencyPair out = new SerdeImpl<CurrencyPair>().deserialize(CurrencyPair.class, bytes);
        assertEquals(in.getBase(), out.getBase());
        assertEquals(in.getTerm(), out.getTerm());
    }

    @Test
    public void testShort() {

        CurrencyPair in = CurrencyPair.newBuilder().setBase("EUR").setTerm("USD").build();

        byte[] bytes = new SerdeImpl<>().serialize(in);
        log.info("bytes: {}", new String(Base64.getEncoder().encode(bytes)));
        CurrencyPairV3 out = new SerdeImpl<CurrencyPairV3>().deserialize(CurrencyPairV3.class, bytes);
        assertEquals(in.getBase(), out.getBase());
        assertEquals(in.getTerm(), out.getTerm());
    }

}
