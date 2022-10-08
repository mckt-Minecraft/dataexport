package io.github.gaming32.dataexport.exporters;

import com.google.gson.stream.JsonWriter;
import io.github.gaming32.dataexport.DataExportMod;
import io.github.gaming32.dataexport.DataExporter;
import io.github.gaming32.dataexport.ExportUtil;
import io.github.gaming32.dataexport.mixin.AbstractBlockAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.StairsBlock;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Field;

public final class BlocksExporter implements DataExporter {
    private static final Field STAIRS_BASE_BLOCK_STATE;

    static {
        final MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();
        Field stairsBaseBlockState;
        try {
            stairsBaseBlockState = StairsBlock.class.getDeclaredField(resolver.mapFieldName(
                "intermediary",
                resolver.mapClassName("intermediary", "net.minecraft.class_2510"),
                "field_11574",
                "L" + BlockState.class.getName().replace('.', '/') + ";"
            ));
            stairsBaseBlockState.setAccessible(true);
        } catch (Exception e) {
            stairsBaseBlockState = null;
            DataExportMod.LOGGER.warn("Failed to get field StairsBlock.baseBlockState", e);
        }
        STAIRS_BASE_BLOCK_STATE = stairsBaseBlockState;
    }

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
            if (block instanceof StairsBlock stairsBlock) {
                output.value("stairs");
                output.name("baseBlockState");
                if (STAIRS_BASE_BLOCK_STATE != null) {
                    output.value(STAIRS_BASE_BLOCK_STATE.get(stairsBlock).toString());
                } else {
                    output.value("minecraft:stone");
                }
            } else {
                output.value("block");
            }
            output.endObject();
            output.endObject();
        }
        output.endObject();
    }
}
