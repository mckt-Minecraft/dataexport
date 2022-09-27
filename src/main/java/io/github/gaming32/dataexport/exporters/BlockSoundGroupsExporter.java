package io.github.gaming32.dataexport.exporters;

import com.google.gson.stream.JsonWriter;
import io.github.gaming32.dataexport.DataExporter;
import io.github.gaming32.dataexport.ExportUtil;
import net.minecraft.sound.BlockSoundGroup;

public final class BlockSoundGroupsExporter implements DataExporter {
    @Override
    public void export(final JsonWriter output) throws Exception {
        output.beginObject();
        ExportUtil.collectConstants(BlockSoundGroup.class, (name, soundGroup) -> {
            output.name(name);
            output.beginObject();
            output.name("volume").value(soundGroup.volume);
            output.name("pitch").value(soundGroup.pitch);
            output.name("breakSound").value(soundGroup.getBreakSound().getId().toString());
            output.name("stepSound").value(soundGroup.getStepSound().getId().toString());
            output.name("placeSound").value(soundGroup.getPlaceSound().getId().toString());
            output.name("hitSound").value(soundGroup.getHitSound().getId().toString());
            output.name("fallSound").value(soundGroup.getFallSound().getId().toString());
            output.endObject();
        });
        output.endObject();
    }
}
