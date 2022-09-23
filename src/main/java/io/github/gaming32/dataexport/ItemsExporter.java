package io.github.gaming32.dataexport;

import com.google.gson.stream.JsonWriter;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public final class ItemsExporter implements DataExporter {
    @Override
    public void export(JsonWriter output) throws Exception {
        output.beginObject();
        for (final Item item : Registry.ITEM) {
            output.name(Registry.ITEM.getId(item).toString()).beginObject();
            output.name("rawId").value(Item.getRawId(item));
            output.name("maxCount").value(item.getMaxCount());
            output.name("maxDamage").value(item.getMaxDamage());
            output.name("extraNetworkSynced").value(item.isNetworkSynced());
            output.name("enchantability").value(item.getEnchantability());
            output.name("food").value(item.isFood());
            output.name("fireproof").value(item.isFireproof());
            output.name("nestable").value(item.canBeNested());
            output.name("drinkSound").value(item.getDrinkSound().getId().toString());
            output.name("eatSound").value(item.getEatSound().getId().toString());
            output.name("equipSound").value(
                item.getEquipSound() != null ? item.getEquipSound().getId().toString() : null
            );
            output.endObject();
        }
        output.endObject();
    }
}
