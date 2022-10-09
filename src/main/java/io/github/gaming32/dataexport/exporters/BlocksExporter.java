package io.github.gaming32.dataexport.exporters;

import com.google.gson.stream.JsonWriter;
import io.github.gaming32.dataexport.DataExporter;
import io.github.gaming32.dataexport.ExportUtil;
import io.github.gaming32.dataexport.mixin.AbstractBlockAccessor;
import io.github.gaming32.dataexport.mixin.StairsBlockAccessor;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Property;
import net.minecraft.util.registry.Registry;

import java.util.stream.Collectors;

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
            output.name("typeProperties").beginObject();
            output.name("type");
            if (block instanceof PillarBlock) {
                output.value("pillar");
            } else if (block instanceof SaplingBlock) {
                output.value("sapling");
            } else if (block instanceof PaneBlock) {
                output.value("pane");
            } else if (block instanceof DoorBlock) {
                output.value("door");
            } else if (block instanceof SlabBlock) {
                output.value("slab");
            } else if (block instanceof StairsBlock stairsBlock) {
                output.value("stairs");
                output.name("baseBlockState").value(
                    blockStateToString(((StairsBlockAccessor)stairsBlock).getBaseBlockState())
                );
            } else if (block instanceof TrapdoorBlock) {
                output.value("trapdoor");
            } else if (block instanceof AbstractSignBlock signBlock) {
                output.value(block instanceof WallSignBlock ? "wall_sign" : "sign");
                output.name("signType").value(signBlock.getSignType().getName());
            } else {
                output.value("block");
            }
            output.endObject();
            output.endObject();
        }
        output.endObject();
    }

    private static String blockStateToString(BlockState state) {
        final StringBuilder result = new StringBuilder(Registry.BLOCK.getId(state.getBlock()).toString());
        if (!state.getEntries().isEmpty()) {
            result.append('[');
            result.append(state.getEntries().entrySet().stream().map(entry ->
                entry.getKey().getName() + '=' + nameValue(entry.getKey(), entry.getValue())
            ).collect(Collectors.joining(",")));
            result.append(']');
        }
        return result.toString();
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> String nameValue(Property<T> property, Object value) {
        return property.name((T)value); // aaaaaaaaaa
    }
}
