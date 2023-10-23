package ru.yandex.practicum.filmorate.model.customJsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateSerialize extends StdSerializer<LocalDate> {
    public LocalDateSerialize() {
        super(LocalDate.class);
    }

    @Override
    public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider sp) throws IOException {
        jsonGenerator.writeString(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}
