package lib.web.typeAdapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    @Override
    public void write(JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
        String result = localDateTime != null ? localDateTime.toString() : "null";
        jsonWriter.value(result);
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        String json = jsonReader.nextString();
        return json.equals("null") ? null : LocalDateTime.parse(json);
    }
}
