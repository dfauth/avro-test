package io.github.dfauth.avro;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.io.ResolvingDecoder;
import org.apache.avro.specific.SpecificDatumReader;

import java.io.EOFException;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class MyDatumReader<T> extends SpecificDatumReader<T> {
    public MyDatumReader(Schema schema) {
        super(schema);
    }

    protected Object readRecord(Object old, Schema expected, ResolvingDecoder in) throws IOException {
        Object record = getData().newRecord(old, expected);
        Object state = null; // getData().getRecordState(record, expected);
        Schema.Field[] var6 = in.readFieldOrder();
        int var7 = var6.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            Schema.Field field = var6[var8];
            int pos = field.pos();
            String name = field.name();
            Object oldDatum = null;
            if (old != null) {
                oldDatum = null; // getData().getField(record, name, pos, state);
            }

            try {
                this.readField(record, field, oldDatum, in, state);
            } catch (EOFException e) {
                break;
            }
        }

        return record;
    }
}
