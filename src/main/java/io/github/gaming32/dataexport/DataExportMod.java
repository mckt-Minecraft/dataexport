package io.github.gaming32.dataexport;

import com.google.gson.stream.JsonWriter;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;
import io.github.gaming32.dataexport.exporters.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

public class DataExportMod implements ModInitializer {
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final Map<String, DataExporter> EXPORTS = Map.of(
        "blocks", new BlocksExporter(),
        "blockSoundGroups", new BlockSoundGroupsExporter(),
        "items", new ItemsExporter(),
        "materials", new MaterialsExporter(),
        "soundEvents", new SoundEventsExporter()
    );

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
            dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("dataexport")
                .executes(ctx -> {
                    ctx.getSource().sendFeedback(
                        Text.literal("Available exports: " + String.join(", ", EXPORTS.keySet())),
                        true
                    );
                    return 0;
                })
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("all")
                    .executes(ctx -> {
                        final long startTime = System.nanoTime();
                        int result = 0;
                        for (String export : EXPORTS.keySet()) {
                            result += export(export, ctx);
                        }
                        final long endTime = System.nanoTime();
                        ctx.getSource().sendFeedback(
                            Text.literal("Ran all exports in " + (endTime - startTime) / 1e6 + "ms"),
                            true
                        );
                        return result;
                    })
                )
                .then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("export", StringArgumentType.word())
                    .suggests((ctx, builder) -> {
                        for (String export : EXPORTS.keySet()) {
                            if (export.startsWith(builder.getRemainingLowerCase())) {
                                builder.suggest(export);
                            }
                        }
                        return builder.buildFuture();
                    })
                    .executes(ctx -> export(StringArgumentType.getString(ctx, "export"), ctx))
                )
            )
        );
    }

    private int export(String export, CommandContext<ServerCommandSource> ctx) {
        final DataExporter exporter = EXPORTS.get(export);
        if (exporter == null) {
            ctx.getSource().sendError(Text.literal("Unknown export " + export));
            return 1;
        }
        ctx.getSource().sendFeedback(Text.literal("Exporting " + export + "..."), true);
        Exception exception = null;
        final long startTime = System.nanoTime();
        final File output = new File("dataexport/" + export + ".json");
        output.getParentFile().mkdirs();
        try (JsonWriter writer = new JsonWriter(new FileWriter(output))) {
            writer.setIndent("  ");
            exporter.export(writer);
        } catch (final Exception e) {
            exception = e;
        }
        final long endTime = System.nanoTime();
        if (exception == null) {
            ctx.getSource().sendFeedback(
                Text.literal(
                    "Exported " + export +
                    " to \"" + output.getAbsolutePath() +
                    "\" in " + (endTime - startTime) / 1e6 + "ms"
                ),
                true
            );
            return 0;
        } else {
            ctx.getSource().sendError(
                Text.literal("Failed to export " + export + " in " + (endTime - startTime) / 1e6 + "ms: ")
                    .append(Text.literal(exception.toString()).styled(style -> style.withColor(Formatting.RED)))
            );
            LOGGER.error("Export failed", exception);
            return 1;
        }
    }
}
