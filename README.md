[![](https://jitpack.io/v/Nico44YT/ModernModelFormat.svg)](https://jitpack.io/#Nico44YT/ModernModelFormat)

Allows the use of the `1.21.11` model format in older versions with the help of black magic, specifically allowing all three axis (XYZ) rotation and incrementes larger/smaller than 22.5°, additionally removing the 3x3x3 size constraint.

### Example
![Showcase of the mod featuring a diamond block with three axis rotation](https://cdn.modrinth.com/data/1SGQO17b/images/5e56cece5d81bfe5dae2a29a3aaa13d22f29ddbc.png)

<details>
<summary>Model</summary>

File: `assets/minecraft/models/block/diamond_block.json`
```json
{
  "format_version": "1.21.11",
  "textures": {
    "all": "block/diamond_block"
  },
  "elements": [
    {
      "from": [0, 0, 0],
      "to": [16, 16, 16],
      "rotation": {"x": 45, "y": 45, "z": 45, "origin": [8, 8, 8]},
      "faces": {
        "north": {"uv": [0, 0, 16, 16], "texture": "#all"},
        "east": {"uv": [0, 0, 16, 16], "texture": "#all"},
        "south": {"uv": [0, 0, 16, 16], "texture": "#all"},
        "west": {"uv": [0, 0, 16, 16], "texture": "#all"},
        "up": {"uv": [0, 0, 16, 16], "texture": "#all"},
        "down": {"uv": [0, 0, 16, 16], "texture": "#all"}
      }
    }
  ]
}
```

</details>

Supports both rotation types:

*All axis rotation*
```json
  "rotation": {
    "x": 45,
    "y": 45,
    "z": 45,
    "origin": [
      8,
      8,
      8
    ]
  }
```

*Single axis rotation*
```json
  "rotation": {
    "angle": 45,
    "axis": "y",
    "origin": [
      8,
      8,
      8
    ]
  }
```

### Maven
```gradle
repositories {
  maven { url 'https://jitpack.io' }
}

dependencies {
  modRuntimeOnly 'com.github.Nico44YT:ModernModelFormat:<version>'
}
```
Version generally is comprised of "<mod_version>_<game_version_range>"
e.g: "1.3.0-1.19.3-1.21.3"




### Issues
If you have found an issue please report it on the [github](https://github.com/Nico44YT/ModernModelFormat/issues), along with (if applicable):
- any logs
- the model
- a screenshot
- the game version
- modlist

#### My models are not loading anymore
Try checking the model file for any extra brackets ```{}``` because Modern Model Format ensure a strict json validation.

Example:
```log
[18:30:32] [Worker-Main-2/ERROR]: Failed to load model mod_id:models/item/example_item.json
com.google.gson.JsonSyntaxException: com.google.gson.stream.MalformedJsonException: Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 506 path $
```
