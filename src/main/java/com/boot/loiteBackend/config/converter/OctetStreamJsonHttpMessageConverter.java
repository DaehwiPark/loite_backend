package com.boot.loiteBackend.config.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

@Component
public class OctetStreamJsonHttpMessageConverter extends AbstractJackson2HttpMessageConverter {

    public OctetStreamJsonHttpMessageConverter(ObjectMapper objectMapper) {
        // APPLICATION_OCTET_STREAM 을 JSON으로 처리 가능하게 등록
        super(objectMapper, MediaType.APPLICATION_OCTET_STREAM);
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        // 쓰기는 필요 없으니 false
        return false;
    }
}
