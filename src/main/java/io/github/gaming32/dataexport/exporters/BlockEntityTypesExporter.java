package io.github.gaming32.dataexport.exporters;

import com.google.gson.stream.JsonWriter;
import io.github.gaming32.dataexport.DataExporter;
import io.github.gaming32.dataexport.mixin.BlockEntityTypeAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class BlockEntityTypesExporter implements DataExporter {
    @Override
    public void export(JsonWriter output) throws Exception {
        output.beginObject();
        for (final BlockEntityType<?> type : Registry.BLOCK_ENTITY_TYPE) {
            //noinspection ConstantConditions
            output.name(BlockEntityType.getId(type).toString()).beginObject();
            output.name("blocks").beginArray();
            for (final Block supportedBlock : ((BlockEntityTypeAccessor)type).getBlocks()) {
                output.value(Registry.BLOCK.getId(supportedBlock).toString());
            }
            output.endArray();
            output.name("networkId").value(Registry.BLOCK_ENTITY_TYPE.getRawId(type));
            output.endObject();
        }
        output.endObject();
    }
}
