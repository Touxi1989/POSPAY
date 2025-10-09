package com.telpo.base.internet.http.factory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;

final class FastJsonResponseBodyConverter<T> implements retrofit2.Converter<okhttp3.ResponseBody, T> {

    private static final Feature[] EMPTY_SERIALIZER_FEATURES = new Feature[0];

    private java.lang.reflect.Type mType;

    private ParserConfig config;
    private int featureValues;
    private Feature[] features;

    FastJsonResponseBodyConverter(java.lang.reflect.Type type, ParserConfig config, int featureValues,
                                  Feature... features) {
        mType = type;
        this.config = config;
        this.featureValues = featureValues;
        this.features = features;
    }

    @Override
    public T convert(okhttp3.ResponseBody value) throws java.io.IOException {
        try {
            if (value.contentLength() > 32 * 1024 * 1024) {
                throw new java.io.IOException("sizeLimit");
            }
            return JSON.parseObject(value.string(), mType, config, featureValues,
                features != null ? features : EMPTY_SERIALIZER_FEATURES);
        } finally {
            value.close();
        }
    }

    private String inputStream2String(java.io.InputStream in) throws java.io.IOException {
        StringBuilder out = new StringBuilder();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1;) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }
}
