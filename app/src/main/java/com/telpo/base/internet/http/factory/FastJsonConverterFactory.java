package com.telpo.base.internet.http.factory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import retrofit2.Converter;
import retrofit2.Retrofit;


/**
 * A {@linkplain Converter.Factory converter} which uses FastJson for JSON.
 * <p>
 * Because FastJson is so flexible in the types it supports, this converter assumes that it can
 * handle all types. If you are mixing JSON serialization with something else (such as protocol
 * buffers), you must {@linkplain Retrofit.Builder#addConverterFactory(Converter.Factory) add
 * this instance} last to allow the other converters a chance to see their types.
 */
public class FastJsonConverterFactory extends retrofit2.Converter.Factory {

  private ParserConfig mParserConfig = ParserConfig.getGlobalInstance();
  private int featureValues = JSON.DEFAULT_PARSER_FEATURE;
  private Feature[] features;

  private SerializeConfig serializeConfig;
  private SerializerFeature[] serializerFeatures;

  /**
   * Create an default instance for conversion. Encoding to JSON and
   * decoding from JSON (when no charset is specified by a header) will use UTF-8.
   * @return The instance of FastJsonConverterFactory
   */
  public static com.telpo.base.internet.http.factory.FastJsonConverterFactory create() {
    return new com.telpo.base.internet.http.factory.FastJsonConverterFactory();
  }

  private FastJsonConverterFactory() {
  }

  @Override
  public retrofit2.Converter<okhttp3.ResponseBody, ?> responseBodyConverter(java.lang.reflect.Type type, java.lang.annotation.Annotation[] annotations,
                                                                            retrofit2.Retrofit retrofit) {
    return new FastJsonResponseBodyConverter<>(type, mParserConfig, featureValues, features);
  }

  @Override
  public retrofit2.Converter<?, okhttp3.RequestBody> requestBodyConverter(java.lang.reflect.Type type,
                                                                          java.lang.annotation.Annotation[] parameterAnnotations, java.lang.annotation.Annotation[] methodAnnotations, retrofit2.Retrofit retrofit) {
    return new FastJsonRequestBodyConverter<>(serializeConfig, serializerFeatures);
  }

  public ParserConfig getParserConfig() {
    return mParserConfig;
  }

  public com.telpo.base.internet.http.factory.FastJsonConverterFactory setParserConfig(ParserConfig config) {
    this.mParserConfig = config;
    return this;
  }

  public int getParserFeatureValues() {
    return featureValues;
  }

  public com.telpo.base.internet.http.factory.FastJsonConverterFactory setParserFeatureValues(int featureValues) {
    this.featureValues = featureValues;
    return this;
  }

  public Feature[] getParserFeatures() {
    return features;
  }

  public com.telpo.base.internet.http.factory.FastJsonConverterFactory setParserFeatures(Feature[] features) {
    this.features = features;
    return this;
  }

  public SerializeConfig getSerializeConfig() {
    return serializeConfig;
  }

  public com.telpo.base.internet.http.factory.FastJsonConverterFactory setSerializeConfig(SerializeConfig serializeConfig) {
    this.serializeConfig = serializeConfig;
    return this;
  }

  public SerializerFeature[] getSerializerFeatures() {
    return serializerFeatures;
  }

  public com.telpo.base.internet.http.factory.FastJsonConverterFactory setSerializerFeatures(SerializerFeature[] features) {
    this.serializerFeatures = features;
    return this;
  }
}
