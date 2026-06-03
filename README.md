# AutoScripting (Ghidra Extension)

AutoScripting is a Ghidra extension that runs a target script automatically when a program is opened. By default, it executes `Autoscript.py` from the `~/ghidra_scripts` directory. The extension content exists inside Ghidra_Autoscripting_Extension.

![Static Badge](https://img.shields.io/badge/Gradle-%2302303A?logo=Gradle) ![Ghidra](https://custom-icon-badges.demolab.com/badge/Ghidra-B00020?style=flat&logo=ghidra2&logoColor=white&labelColor=000000) ![Java](https://custom-icon-badges.demolab.com/badge/Java-0074BD?style=flat&logo=javacup2&labelColor=000000&color=0074BD)


## How It Works

- The `AutoStartPlugin` listens for program-open events.
- The first time a program is opened in a session, the plugin loads and runs `Autoscript.py` from `~/ghidra_scripts`.
- If the same program is reopened in the same session, the script will not rerun unless the program is closed first.

## Requirements

- Ghidra 11.4.2 (or a compatible version you specify)
- Java (required by Ghidra)
- Gradle (or use the Gradle wrapper if you add one later)

## Configure Your Environment

You must point the build to your local Ghidra installation.

Option 1: Set an environment variable:

```sh
export GHIDRA_INSTALL_DIR=/path/to/ghidra
```

Option 2: Edit [gradle.properties](gradle.properties) and set:

```properties
GHIDRA_INSTALL_DIR=/path/to/ghidra
```

If your Ghidra version differs, update the `version` field in [extension.properties](extension.properties) to match your Ghidra version.

## Build the Extension

From the project root:

```sh
gradle buildExtension
```

This produces a zip in `dist/` (for example, `dist/AutoScripting.zip`).

## Install the Extension in Ghidra

1. Open Ghidra.
2. Go to **File > Install Extensions**.
3. Click **+** and select the generated zip from `dist/`.
4. Restart Ghidra when prompted.
5. Open **File > Configure** and enable the **AutoScript** plugin under **Scripting** if it is not already enabled.

## Install the Auto Script

Place your script at:

```
~/ghidra_scripts/Autoscript.py
```

Notes:
- The filename must be exactly `Autoscript.py`.
- The script runs each time a program is opened (once per program per session).

## Troubleshooting

- If the build fails, confirm `GHIDRA_INSTALL_DIR` is set correctly.
- If the plugin does not appear, verify the `version` field in [extension.properties](extension.properties) matches your Ghidra version.
- If the script does not run, check that `Autoscript.py` exists in `~/ghidra_scripts`.

## Project Structure (minimal build files)

```
build.gradle
extension.properties
gradle.properties
settings.gradle
src/main/java/autoscript/AutoStartPlugin.java
```
