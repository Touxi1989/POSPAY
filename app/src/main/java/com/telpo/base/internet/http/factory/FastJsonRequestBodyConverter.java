package com.telpo.base.internet.http.factory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

final class FastJsonRequestBodyConverter<T> implements retrofit2.Converter<T, okhttp3.RequestBody> {
  private static final okhttp3.MediaType MEDIA_TYPE = okhttp3.MediaType.parse("application/json; charset=UTF-8");
  private SerializeConfig serializeConfig;
  private SerializerFeature[] serializerFeatures;

  FastJsonRequestBodyConverter(SerializeConfig config, SerializerFeature... features) {
    serializeConfig = config;
    serializerFeatures = features;
  }

  @Override
  public okhttp3.RequestBody convert(T value) throws java.io.IOException {
    byte[] content;
    if (serializeConfig != null) {
      if (serializerFeatures != null) {
        content = JSON.toJSONBytes(value, serializeConfig, serializerFeatures);
      } else {
        content = JSON.toJSONBytes(value, serializeConfig);
      }
    } else {
      if (serializerFeatures != null) {
        content = JSON.toJSONBytes(value, serializerFeatures);
      } else {
        content = JSON.toJSONBytes(value);
      }
    }
    return okhttp3.RequestBody.create(MEDIA_TYPE, content);
  }
}
