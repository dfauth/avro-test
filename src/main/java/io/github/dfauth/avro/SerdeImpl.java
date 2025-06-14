package io.github.dfauth.avro;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class SerdeImpl<T extends SpecificRecord> implements Serde<T> {

    @Override
    public T deserialize(Class<T> classOfT, byte[] data) {
        return deserializeInternal(classOfT, data);
    }

    @Override
    public Map<String, T> deserializeMap(Class<T> targetClass, byte[] data) {
        return deserializeInternal(AvroMap.class, data).getEntries()
                .entrySet()
                .stream()
                .<Map<String,T>>reduce(new HashMap<>(),
                        (m,e) -> {
                           m.put(e.getKey(), deserializeInternal(targetClass, e.getValue().array()));
                           return m;
                        },
                        (l,r) -> Stream.concat(l.entrySet().stream(), r.entrySet().stream())
                                .collect(Collectors.<Map.Entry<String,T>,String,T>toMap(Map.Entry::getKey, Map.Entry::getValue))
                        );
    }

    @Override
    public List<T> deserializeList(Class<T> targetClass, byte[] data) {
        return deserializeInternal(AvroList.class, data).getEntries()
                .stream()
                .<List<T>>reduce(new ArrayList<>(),
                        (l,e) -> {
                            l.add(deserializeInternal(targetClass, e.array()));
                            return l;
                        },
                        (l,r) -> Stream.concat(l.stream(), r.stream()).toList()
                );
    }

    @Override
    public byte[] serialize(T t) {
        return serializeInternal(t);
    }

    @Override
    public byte[] serializeMap(Map<String, T> map) {
        return serializeInternal(AvroMap.newBuilder().setEntries(map.entrySet().stream().<Map<String,ByteBuffer>>reduce(new HashMap<String,ByteBuffer>(),
                (b,e) -> {
                    b.put(e.getKey(), ByteBuffer.wrap(serialize(e.getValue())));
                    return b;
                },
                (l,r) -> Stream.concat(l.entrySet().stream(), r.entrySet().stream())
                        .collect(Collectors.<Map.Entry<String,ByteBuffer>,String,ByteBuffer>toMap(Map.Entry::getKey, Map.Entry::getValue)))
        ).build());
    }

    @Override
    public byte[] serializeList(List<T> list) {
        return serializeInternal(AvroList.newBuilder().setEntries(list.stream().<List<ByteBuffer>>reduce(new ArrayList<>(),
                (b,e) -> {
                    b.add(ByteBuffer.wrap(serialize(e)));
                    return b;
                },
                (l,r) -> Stream.concat(l.stream(), r.stream()).toList())
        ).build());
    }

    protected byte[] serializeInternal(SpecificRecord t) {
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

    protected <R extends SpecificRecord> R deserializeInternal(Class<R> classOfR, byte[] data) {

        try {
            Schema schema = (Schema) classOfR.getField("SCHEMA$").get(classOfR);

            DatumReader<R> datumReader = new MyDatumReader<>(schema);

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
}
