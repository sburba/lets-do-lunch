package com.letsdolunch.letsdolunch;

import com.letsdolunch.letsdolunch.model.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Configuration
public class AppConfig extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {
    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer());
        return builder;
    }
}
