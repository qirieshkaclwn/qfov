package qirieshka.qfov;

import net.fabricmc.api.ModInitializer;

public class Qfov implements ModInitializer {

    @Override
    public void onInitialize() {
        QfovConfig.load();
    }
}
