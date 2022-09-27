package io.github.gaming32.dataexport.exporters;

import com.google.gson.stream.JsonWriter;
import io.github.gaming32.dataexport.DataExporter;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

public final class SoundEventsExporter implements DataExporter {
    @Override
    public void export(JsonWriter output) throws Exception {
        output.beginObject();
        for (final SoundEvent event : Registry.SOUND_EVENT) {
            output.name(event.getId().toString()).value(Registry.SOUND_EVENT.getRawId(event));
        }
        output.endObject();
    }
}
