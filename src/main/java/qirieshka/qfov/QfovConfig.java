package qirieshka.qfov;

import net.fabricmc.loader.api.FabricLoader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class QfovConfig {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("qfov.properties");
    public static boolean preventFovFlipping = true;

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            save();
            return;
        }
        Properties properties = new Properties();
        try (InputStream in = Files.newInputStream(CONFIG_PATH)) {
            properties.load(in);
            preventFovFlipping = Boolean.parseBoolean(properties.getProperty("preventFovFlipping", "true"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        Properties properties = new Properties();
        properties.setProperty("preventFovFlipping", String.valueOf(preventFovFlipping));
        try (OutputStream out = Files.newOutputStream(CONFIG_PATH)) {
            properties.store(out, "Qfov Configuration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
