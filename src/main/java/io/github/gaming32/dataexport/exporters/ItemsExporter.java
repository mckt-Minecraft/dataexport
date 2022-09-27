package io.github.gaming32.dataexport.exporters;

import com.google.gson.stream.JsonWriter;
import io.github.gaming32.dataexport.DataExporter;
import net.minecraft.item.FoodComponent;
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
            output.name("foodComponent");
            if (item.getFoodComponent() != null) {
                final FoodComponent food = item.getFoodComponent();
                output.beginObject();
                output.name("hunger").value(food.getHunger());
                output.name("saturationModifier").value(food.getSaturationModifier());
                output.name("meat").value(food.isMeat());
                output.name("alwaysEdible").value(food.isAlwaysEdible());
                output.name("snack").value(food.isSnack());
                output.endObject();
            } else {
                output.nullValue();
            }
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
