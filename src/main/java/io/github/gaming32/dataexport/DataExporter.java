package io.github.gaming32.dataexport;

import com.google.gson.stream.JsonWriter;

public interface DataExporter {
    void export(final JsonWriter output) throws Exception;
}
