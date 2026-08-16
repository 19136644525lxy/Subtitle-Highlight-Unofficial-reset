package yeah_zero.subtitle_highlight.Data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import yeah_zero.subtitle_highlight.Configure.Manager;
import yeah_zero.subtitle_highlight.Configure.Settings;
import yeah_zero.subtitle_highlight.Util.ColorCode;
import net.minecraft.resources.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SubtitleTypeLoader {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("subtitle_highlight", "subtitle_types");
    private static final Gson GSON = new Gson();
    private static Map<String, Map<String, ColorCode>> subtitleTypes = new HashMap<>();
    
    private static final ConcurrentHashMap<String, ColorCode> colorCache = new ConcurrentHashMap<>();
    
    private static final ColorCode NULL_MARKER = ColorCode.GRAY;

    public static void loadFromJson(InputStream inputStream) {
        try {
            JsonObject jsonObject = GSON.fromJson(new InputStreamReader(inputStream), JsonObject.class);
            if (jsonObject != null && jsonObject.has("subtitle_types")) {
                JsonObject subtitleTypesJson = jsonObject.getAsJsonObject("subtitle_types");
                subtitleTypes.clear();
                loadSubtitleTypes(subtitleTypesJson, "", subtitleTypes);
            } else {
                loadDefaultSubtitleTypes();
            }
            invalidateCache();
        } catch (Exception e) {
            e.printStackTrace();
            loadDefaultSubtitleTypes();
        }
    }

    private static void loadSubtitleTypes(JsonObject jsonObject, String path, Map<String, Map<String, ColorCode>> result) {
        for (String key : jsonObject.keySet()) {
            String currentPath = path.isEmpty() ? key : path + "." + key;
            if (jsonObject.get(key).isJsonObject()) {
                JsonObject nestedObject = jsonObject.getAsJsonObject(key);
                if (nestedObject.has("color")) {
                    String colorName = nestedObject.get("color").getAsString();
                    ColorCode colorCode = ColorCode.fromName(colorName);
                    if (colorCode != null) {
                        String parentPath = path.isEmpty() ? "" : path;
                        if (!result.containsKey(parentPath)) {
                            result.put(parentPath, new HashMap<>());
                        }
                        result.get(parentPath).put(key, colorCode);
                    }
                } else {
                    loadSubtitleTypes(nestedObject, currentPath, result);
                }
            }
        }
    }

    private static void loadDefaultSubtitleTypes() {
        subtitleTypes.clear();

        Map<String, ColorCode> ambientMap = new HashMap<>();
        ambientMap.put("ambient", ColorCode.DARK_BLUE);
        subtitleTypes.put("", ambientMap);

        Map<String, ColorCode> blockMap = new HashMap<>();
        blockMap.put("generic", ColorCode.GRAY);
        blockMap.put("interact", ColorCode.GREEN);
        blockMap.put("working", ColorCode.YELLOW);
        blockMap.put("dangerous", ColorCode.RED);
        blockMap.put("crop", ColorCode.GREEN);
        blockMap.put("other", ColorCode.GRAY);
        subtitleTypes.put("block", blockMap);

        Map<String, ColorCode> enchantMap = new HashMap<>();
        enchantMap.put("enchant", ColorCode.LIGHT_PURPLE);
        subtitleTypes.put("", enchantMap);

        Map<String, ColorCode> entityMobPlayerMap = new HashMap<>();
        entityMobPlayerMap.put("attack", ColorCode.RED);
        entityMobPlayerMap.put("hurt", ColorCode.RED);
        entityMobPlayerMap.put("other", ColorCode.WHITE);
        subtitleTypes.put("entity.mob.player", entityMobPlayerMap);

        Map<String, ColorCode> entityMobMap = new HashMap<>();
        entityMobMap.put("passive", ColorCode.GREEN);
        entityMobMap.put("neutral", ColorCode.YELLOW);
        entityMobMap.put("hostile", ColorCode.RED);
        entityMobMap.put("boss", ColorCode.DARK_PURPLE);
        subtitleTypes.put("entity.mob", entityMobMap);

        Map<String, ColorCode> entityMap = new HashMap<>();
        entityMap.put("vehicle", ColorCode.GRAY);
        entityMap.put("projectile", ColorCode.AQUA);
        entityMap.put("explosive", ColorCode.RED);
        entityMap.put("decoration", ColorCode.GRAY);
        entityMap.put("other", ColorCode.GRAY);
        subtitleTypes.put("entity", entityMap);

        Map<String, ColorCode> itemMap = new HashMap<>();
        itemMap.put("weapon", ColorCode.RED);
        itemMap.put("armor", ColorCode.GOLD);
        itemMap.put("tool", ColorCode.AQUA);
        itemMap.put("other", ColorCode.GRAY);
        subtitleTypes.put("item", itemMap);

        Map<String, ColorCode> otherMap = new HashMap<>();
        otherMap.put("other", ColorCode.GRAY);
        subtitleTypes.put("", otherMap);

        invalidateCache();
    }

    public static ColorCode getColor(String subtitleId) {
        if (subtitleId == null || subtitleId.isEmpty()) {
            return null;
        }

        String cacheKey = subtitleId;
        ColorCode cached = colorCache.get(cacheKey);
        if (cached != null) {
            return cached == NULL_MARKER ? null : cached;
        }

        String[] parts = subtitleId.split("\\.");
        String path = String.join(".", java.util.Arrays.copyOf(parts, parts.length - 1));
        String key = parts[parts.length - 1];

        ColorCode result = null;

        if (subtitleTypes.containsKey(path)) {
            Map<String, ColorCode> map = subtitleTypes.get(path);
            if (map.containsKey(key)) {
                result = map.get(key);
            }
        }

        if (result == null && !path.isEmpty()) {
            int lastDotIndex = path.lastIndexOf('.');
            if (lastDotIndex > 0) {
                String parentPath = path.substring(0, lastDotIndex);
                if (subtitleTypes.containsKey(parentPath)) {
                    Map<String, ColorCode> map = subtitleTypes.get(parentPath);
                    if (map.containsKey(key)) {
                        result = map.get(key);
                    }
                }
            }
        }

        if (result == null) {
            Settings.ColorSetting colorSettings = Manager.settings.colorSettings;
            if (subtitleId.startsWith("subtitles.ambient")) {
                result = colorSettings.ambient;
            } else if (subtitleId.startsWith("subtitles.block")) {
                if (subtitleId.contains("interact")) {
                    result = colorSettings.block.interact;
                } else if (subtitleId.contains("working")) {
                    result = colorSettings.block.working;
                } else if (subtitleId.contains("dangerous")) {
                    result = colorSettings.block.dangerous;
                } else if (subtitleId.contains("crop")) {
                    result = colorSettings.block.crop;
                } else {
                    result = colorSettings.block.generic;
                }
            } else if (subtitleId.startsWith("subtitles.enchant")) {
                result = colorSettings.enchant;
            } else if (subtitleId.startsWith("subtitles.entity")) {
                if (subtitleId.contains("player")) {
                    if (subtitleId.contains("attack")) {
                        result = colorSettings.entity.mob.player.attack;
                    } else if (subtitleId.contains("hurt")) {
                        result = colorSettings.entity.mob.player.hurt;
                    } else {
                        result = colorSettings.entity.mob.player.other;
                    }
                } else if (subtitleId.contains("boss")) {
                    result = colorSettings.entity.mob.boss;
                } else if (subtitleId.contains("hostile")) {
                    result = colorSettings.entity.mob.hostile;
                } else if (subtitleId.contains("neutral")) {
                    result = colorSettings.entity.mob.neutral;
                } else if (subtitleId.contains("passive")) {
                    result = colorSettings.entity.mob.passive;
                } else if (subtitleId.contains("vehicle")) {
                    result = colorSettings.entity.vehicle;
                } else if (subtitleId.contains("projectile")) {
                    result = colorSettings.entity.projectile;
                } else if (subtitleId.contains("explosive")) {
                    result = colorSettings.entity.explosive;
                } else if (subtitleId.contains("decoration")) {
                    result = colorSettings.entity.decoration;
                } else {
                    result = colorSettings.entity.other;
                }
            } else if (subtitleId.startsWith("subtitles.item")) {
                if (subtitleId.contains("weapon")) {
                    result = colorSettings.item.weapon;
                } else if (subtitleId.contains("armor")) {
                    result = colorSettings.item.armor;
                } else if (subtitleId.contains("tool")) {
                    result = colorSettings.item.tool;
                } else {
                    result = colorSettings.item.other;
                }
            } else {
                result = colorSettings.other;
            }
        }

        colorCache.put(cacheKey, result != null ? result : NULL_MARKER);
        return result;
    }

    public static void invalidateCache() {
        colorCache.clear();
    }

    public static void init() {
        loadDefaultSubtitleTypes();
    }
}
