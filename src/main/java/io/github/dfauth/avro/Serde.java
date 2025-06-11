package io.github.dfauth.avro;

import org.apache.avro.specific.SpecificRecord;

public interface Serde<T extends SpecificRecord> extends Serializer<T>, Deserializer<T> {
}
