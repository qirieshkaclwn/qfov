package qirieshka.qfov.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;
import qirieshka.qfov.QfovConfig;

public class QfovModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("Qfov Configuration"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            ConfigCategory general = builder.getOrCreateCategory(Component.literal("General"));

            general.addEntry(entryBuilder.startBooleanToggle(Component.literal("Prevent FOV Flipping"), QfovConfig.preventFovFlipping)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> {
                    QfovConfig.preventFovFlipping = newValue;
                    QfovConfig.save();
                })
                .build());

            return builder.build();
        };
    }
}
