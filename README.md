# serilalizer-util
Serialization utility

Entry point SerializationUtil.class. Contains methods for serialization/deserialization.
Works with all primitive type and their wrappers, including java.lang.String, java.util.Date and collections/maps with previously mentioned types inside.
Also can work with complex object containing all previously mentioned objects/primitives.

################################################################################################

to build project use:

### mvn clean install

to run benchmarks run class BenchmarkRunner

################################################################################################

# Result of benchmark

Result "com.demo.benchmark.test.SimpleSerializationBenchmark.test_serialization":
  0.003 ±(99.9%) 0.001 ms/op [Average]
  (min, avg, max) = (0.002, 0.003, 0.004), stdev = 0.001
  CI (99.9%): [0.003, 0.003] (assumes normal distribution)


### Run complete. Total time: 00:27:18

Benchmark                                                   (serializerName)  Mode  Cnt  Score    Error  Units
ComplexSerializationBenchmark.test_deserialization         JavaSerialization  avgt  100  0.048 ±  0.008  ms/op
ComplexSerializationBenchmark.test_deserialization         KryoSerialization  avgt  100  0.007 ±  0.001  ms/op
ComplexSerializationBenchmark.test_deserialization  JacksonJsonSerialization  avgt  100  0.013 ±  0.001  ms/op
ComplexSerializationBenchmark.test_deserialization   CustomUtilSerialization  avgt  100  0.182 ±  0.005  ms/op
ComplexSerializationBenchmark.test_serialization           JavaSerialization  avgt  100  0.009 ±  0.001  ms/op
ComplexSerializationBenchmark.test_serialization           KryoSerialization  avgt  100  0.004 ±  0.001  ms/op
ComplexSerializationBenchmark.test_serialization    JacksonJsonSerialization  avgt  100  0.005 ±  0.001  ms/op
ComplexSerializationBenchmark.test_serialization     CustomUtilSerialization  avgt  100  0.034 ±  0.002  ms/op
SimpleSerializationBenchmark.test_deserialization          JavaSerialization  avgt  100  0.009 ±  0.001  ms/op
SimpleSerializationBenchmark.test_deserialization          KryoSerialization  avgt  100  0.002 ±  0.001  ms/op
SimpleSerializationBenchmark.test_deserialization   JacksonJsonSerialization  avgt  100  0.001 ±  0.001  ms/op
SimpleSerializationBenchmark.test_deserialization    CustomUtilSerialization  avgt  100  0.019 ±  0.001  ms/op
SimpleSerializationBenchmark.test_serialization            JavaSerialization  avgt  100  0.001 ±  0.001  ms/op
SimpleSerializationBenchmark.test_serialization            KryoSerialization  avgt  100  0.001 ±  0.001  ms/op
SimpleSerializationBenchmark.test_serialization     JacksonJsonSerialization  avgt  100  0.001 ±  0.001  ms/op
SimpleSerializationBenchmark.test_serialization      CustomUtilSerialization  avgt  100  0.003 ±  0.001  ms/op