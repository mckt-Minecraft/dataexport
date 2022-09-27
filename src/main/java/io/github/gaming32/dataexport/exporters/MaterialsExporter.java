package io.github.gaming32.dataexport.exporters;

import com.google.gson.stream.JsonWriter;
import io.github.gaming32.dataexport.DataExporter;
import io.github.gaming32.dataexport.ExportUtil;
import net.minecraft.block.Material;

import java.util.Locale;

public final class MaterialsExporter implements DataExporter {
    @Override
    public void export(final JsonWriter output) throws Exception {
        output.beginObject();
        ExportUtil.collectConstants(Material.class, (name, material) -> {
            output.name(name);
            output.beginObject();
            output.name("mapColor").value(material.getColor().id);
            output.name("pistonBehavior").value(material.getPistonBehavior().name().toLowerCase(Locale.ROOT));
            output.name("blocksMovement").value(material.blocksMovement());
            output.name("burnable").value(material.isBurnable());
            output.name("liquid").value(material.isLiquid());
            output.name("blocksLight").value(material.blocksLight());
            output.name("replaceable").value(material.isReplaceable());
            output.name("solid").value(material.isSolid());
            output.endObject();
        });
        output.endObject();
    }
}
