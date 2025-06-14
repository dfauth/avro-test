some experiments with avro

avro lacks native support for lists and maps

this serde adds support for both and allows composition of more complect types (Lists of Maps; maps of Maps etc)

the simple deserializer will also choke on short read - ie. the serialized object contains less fields than the deserializer expects. 
This deserializer will handle such cases

the long read case works in the default deserialzer already so no need to handle

Of course to do this correctly you need to use a schema registry
