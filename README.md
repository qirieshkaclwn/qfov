# Qfov (Field of View Unlocker)

A Minecraft Fabric mod for version **26.1.2** (running Java 25) that unlocks the game's Field of View (FOV) slider up to 359° and implements smart, bidirectional perspective flipping prevention.

## Features

- **Extended FOV Slider**: Extends the game options FOV slider limits from `30° - 110°` to `30° - 359°`.
- **Bidirectional Flipping Prevention**:
  - If your base setting is `< 180°` (standard view): Clamps the calculated in-game FOV to a maximum of `179.0°` so that sprinting or speed effects do not cross the 180° boundary and flip your screen upside down.
  - If your base setting is `> 180°` (upside-down view): Clamps the calculated in-game FOV to a minimum of `181.0°` to prevent the screen from flipping back right-side up during slow effects or when sprinting ends.
- **In-Game Configuration Screen**: Mod settings can be adjusted in-game via **Cloth Config** and **Mod Menu**.
- **Config Persistence**: Automatically saves and loads your settings to and from `config/qfov.properties`.

## Requirements

- Minecraft `26.1.2`
- Fabric Loader
- Fabric API
- Cloth Config (Fabric)
- Mod Menu (optional, for configuration GUI)

## Compilation & Installation

To build the mod from source:
1. Clone the repository.
2. Make sure you have JDK 25 installed.
3. Build the JAR using Gradle:
   ```bash
   ./gradlew build
   ```
4. Find the built jar under `build/libs/qfov-1.0.jar` and copy it to your Minecraft `mods` folder.

## Author

- **qirieshka** - Creator and developer

