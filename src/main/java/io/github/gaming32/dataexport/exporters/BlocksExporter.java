package io.github.gaming32.dataexport.exporters;

import com.google.gson.stream.JsonWriter;
import io.github.gaming32.dataexport.DataExporter;
import io.github.gaming32.dataexport.ExportUtil;
import io.github.gaming32.dataexport.mixin.AbstractBlockAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

public final class BlocksExporter implements DataExporter {
    @Override
    public void export(final JsonWriter output) throws Exception {
        final var soundGroups = ExportUtil.collectConstantsToMap(BlockSoundGroup.class);
        final var materials = ExportUtil.collectConstantsToMap(Material.class);
        output.beginObject();
        for (final Block block : Registry.BLOCK) {
            output.name(Registry.BLOCK.getId(block).toString());
            output.beginObject();
            output.name("soundGroup").value(soundGroups.get(block.getSoundGroup(block.getDefaultState())));
            output.name("material").value(materials.get(((AbstractBlockAccessor)block).getMaterial()));
            output.name("mapColor").value(block.getDefaultMapColor().id);
            output.name("blastResistance").value(block.getBlastResistance());
            output.name("hardness").value(block.getHardness());
            output.endObject();
        }
        output.endObject();
    }
}
