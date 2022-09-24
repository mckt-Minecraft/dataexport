package io.github.gaming32.dataexport;

import com.google.gson.stream.JsonWriter;
import net.minecraft.block.Block;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class BlocksExporter implements DataExporter {
    @Override
    public void export(final JsonWriter output) throws Exception {
        final var soundGroups = findBlockSoundGroups();
        output.beginObject();
        for (final Block block : Registry.BLOCK) {
            output.name(Registry.BLOCK.getId(block).toString());
            output.beginObject();
            output.name("soundGroup").value(soundGroups.get(block.getSoundGroup(block.getDefaultState())));
            output.name("mapColor").value(block.getDefaultMapColor().id);
            output.name("blastResistance").value(block.getBlastResistance());
            output.name("hardness").value(block.getHardness());
            output.endObject();
        }
        output.endObject();
    }

    private static Map<BlockSoundGroup, String> findBlockSoundGroups() throws IllegalAccessException {
        final Map<BlockSoundGroup, String> result = new HashMap<>();
        for (final Field field : BlockSoundGroup.class.getDeclaredFields()) {
            final int mods = field.getModifiers();
            if (!Modifier.isPublic(mods) || !Modifier.isStatic(mods) || !Modifier.isFinal(mods)) continue;
            if (field.getType() != BlockSoundGroup.class) continue;
            final BlockSoundGroup soundGroup = (BlockSoundGroup) field.get(null);
            result.put(soundGroup, field.getName().toLowerCase(Locale.ROOT));
        }
        return result;
    }
}
