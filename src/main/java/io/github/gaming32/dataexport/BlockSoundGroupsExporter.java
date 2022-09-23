package io.github.gaming32.dataexport;

import com.google.gson.stream.JsonWriter;
import net.minecraft.sound.BlockSoundGroup;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;

public final class BlockSoundGroupsExporter implements DataExporter {
    @Override
    public void export(final JsonWriter output) throws Exception {
        output.beginObject();
        for (final Field field : BlockSoundGroup.class.getDeclaredFields()) {
            final int mods = field.getModifiers();
            if (!Modifier.isPublic(mods) || !Modifier.isStatic(mods) || !Modifier.isFinal(mods)) continue;
            if (field.getType() != BlockSoundGroup.class) continue;
            final BlockSoundGroup soundGroup = (BlockSoundGroup) field.get(null);
            output.name(field.getName().toLowerCase(Locale.ROOT));
            output.beginObject();
            output.name("volume").value(soundGroup.volume);
            output.name("pitch").value(soundGroup.pitch);
            output.name("breakSound").value(soundGroup.getBreakSound().getId().toString());
            output.name("stepSound").value(soundGroup.getStepSound().getId().toString());
            output.name("placeSound").value(soundGroup.getPlaceSound().getId().toString());
            output.name("hitSound").value(soundGroup.getHitSound().getId().toString());
            output.name("fallSound").value(soundGroup.getFallSound().getId().toString());
            output.endObject();
        }
        output.endObject();
    }
}
