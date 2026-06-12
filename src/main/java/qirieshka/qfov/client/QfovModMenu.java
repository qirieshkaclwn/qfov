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
                .setTitle(Component.translatable("text.qfov.title"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            ConfigCategory general = builder.getOrCreateCategory(Component.translatable("text.qfov.category.general"));

            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("text.qfov.option.prevent_fov_flipping"), QfovConfig.preventFovFlipping)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("text.qfov.option.prevent_fov_flipping.tooltip"))
                .setSaveConsumer(newValue -> {
                    QfovConfig.preventFovFlipping = newValue;
                    QfovConfig.save();
                })
                .build());

            return builder.build();
        };
    }
}
