package com.shopHMsic.ultilities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.stream.Stream;

@Service
@Slf4j
public class MapperUtil {

    @Value("${spring.jackson.time-zone}")
    private String timeZone;

    @Value("${spring.jackson.date-format}")
    private String dateFormat;

    public static Class<?> getClassForObject(Object obj) {
        Class<?> clazz = null;
        if (obj == null) {
            throw new NullPointerException();
        }
        try {
            clazz = ClassUtils.getClass(ClassUtils.getName(obj));
        } catch (ClassNotFoundException e) {
            log.error("Class not found");
        }
        return clazz;
    }


    public static String convertToJsonName(Class<?> clazz, String fieldName) {
        return Stream.of(clazz.getDeclaredFields())
                .filter(field -> field.getName().equals(fieldName))
                .map(Field::getDeclaredAnnotations)
                .flatMap(Stream::of)
                .filter(annotation -> annotation instanceof JsonProperty)
                .map(annotation -> ((JsonProperty) annotation).value())
                .filter(StringUtils::isNotBlank)
                .findFirst().orElse(fieldName);
    }

    public static boolean isAssignableFromForCC( Class<?> cls,  Class<?> tocls) {
        return ClassUtils.isAssignable(cls, tocls);
    }
}
