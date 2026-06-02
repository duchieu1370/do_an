package com.shopHMsic.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

@Configuration
@RequiredArgsConstructor
public class MessageTemplate {
    private final MessageSource messageSource;

    public String message(String key, String... value) {
        return messageSource.getMessage(key, value, LocaleContextHolder.getLocale());
    }
}
