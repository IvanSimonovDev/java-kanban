package lib.web.typeAdapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter jsonWriter, final Duration duration) throws IOException {
        String result = duration != null ? duration.toString() : "null";
        jsonWriter.value(result);
    }

    @Override
    public Duration read(final JsonReader jsonReader) throws IOException {
        String json = jsonReader.nextString();
        return json.equals("null") ? null : Duration.parse(json);
    }
}
