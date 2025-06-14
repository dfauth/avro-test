package io.github.dfauth.avro;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.io.ResolvingDecoder;
import org.apache.avro.specific.SpecificDatumReader;

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@AllArgsConstructor
public class MyDatumReader<T> extends SpecificDatumReader<T> {
    public MyDatumReader(Schema schema) {
        super(schema);
    }

    protected Object readRecord(Object old, Schema expected, ResolvingDecoder in) throws IOException {
        Object record = getData().newRecord(old, expected);

        return Arrays.stream(in.readFieldOrder())
                .reduce(record, (r, f) -> {
                        try {
                            MyDatumReader.this.readField(r, f, null, in, null);
                            return r;
                        } catch (EOFException e) {
                            // EOFException indicates a short avro object:
                            // ie. an earlier version of this object
                            // we ignore the exception and allow processing to continue
                            // to populate the properties we have
                            return r;
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                            throw new RuntimeException(e);
                        }
                    },
                    (l,r) -> l
                );
    }
}
