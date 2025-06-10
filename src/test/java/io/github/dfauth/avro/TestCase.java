package io.github.dfauth.avro;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Base64;

import static io.github.dfauth.avro.Status.ACTIVE;
import static org.junit.Assert.assertEquals;

@Slf4j
public class TestCase {

    @Test
    public void testIt() throws IOException {
        User user = User.newBuilder()
                .setAge(21)
                .setName("fred")
                .setStatus(ACTIVE)
                .setSalary(new BigDecimal("100000.00"))
                .build();
        ByteBuffer bytes = user.toByteBuffer();
        log.info("bytes: {}", Base64.getEncoder().encode(bytes.array()));
        User rehydratedUser = User.fromByteBuffer(bytes);
        assertEquals(user, rehydratedUser);
    }

}
