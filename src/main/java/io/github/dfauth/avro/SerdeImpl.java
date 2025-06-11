package io.github.dfauth.avro;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

@Slf4j
public class SerdeImpl<T extends SpecificRecord> implements Serde<T> {

    @Override
    public T deserialize(Class<T> classOfT, byte[] data) {

        try {
            Schema schema = (Schema) classOfT.getField("SCHEMA$").get(classOfT);

            SpecificDatumReader<T> datumReader = new SpecificDatumReader<>(schema);

            // Create a BinaryDecoder to read the binary data
            BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(data, null);

            // Deserialize the data
            return datumReader.read(null, decoder);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] serialize(T t) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            SpecificDatumWriter<SpecificRecord> datumWriter = new SpecificDatumWriter<>(t.getSchema());
            Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
            datumWriter.write(t, encoder);
            encoder.flush();
            return out.toByteArray();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
