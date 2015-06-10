package com.letsdolunch.letsdolunch.model;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");

    public LocalDateTimeSerializer() {
        super(LocalDateTime.class);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeString(value.format(formatter));
    }
}
