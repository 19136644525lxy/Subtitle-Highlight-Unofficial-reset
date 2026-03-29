package com.qituo.shur.Data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.qituo.shur.Util.ColorCode;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class SubtitleTypeLoader implements SimpleSynchronousResourceReloadListener {
    public static final Identifier ID = Identifier.of("shur", "subtitle_types");
    private static final Gson GSON = new Gson();
    private static Map<String, Map<String, ColorCode>> subtitleTypes = new HashMap<>();

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public void reload(ResourceManager manager) {
        try {
            // 加载默认数据文件
            InputStream inputStream = manager.getResource(Identifier.of("shur", "subtitle_types.json")).get().getInputStream();
            JsonObject jsonObject = GSON.fromJson(new InputStreamReader(inputStream), JsonObject.class);
            JsonObject subtitleTypesJson = jsonObject.getAsJsonObject("subtitle_types");

            // 解析数据
            subtitleTypes.clear();
            loadSubtitleTypes(subtitleTypesJson, "", subtitleTypes);
        } catch (Exception e) {
            e.printStackTrace();
            // 如果加载失败，使用默认配置
            loadDefaultSubtitleTypes();
        }
    }

    private void loadSubtitleTypes(JsonObject jsonObject, String path, Map<String, Map<String, ColorCode>> result) {
        for (String key : jsonObject.keySet()) {
            String currentPath = path.isEmpty() ? key : path + "." + key;
            if (jsonObject.get(key).isJsonObject()) {
                JsonObject nestedObject = jsonObject.getAsJsonObject(key);
                if (nestedObject.has("color")) {
                    // 找到颜色配置
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
                    // 递归处理嵌套对象
                    loadSubtitleTypes(nestedObject, currentPath, result);
                }
            }
        }
    }

    private void loadDefaultSubtitleTypes() {
        subtitleTypes.clear();
        
        // 环境
        Map<String, ColorCode> ambientMap = new HashMap<>();
        ambientMap.put("ambient", ColorCode.DARK_BLUE);
        subtitleTypes.put("", ambientMap);
        
        // 方块
        Map<String, ColorCode> blockMap = new HashMap<>();
        blockMap.put("generic", ColorCode.GRAY);
        blockMap.put("interact", ColorCode.GREEN);
        blockMap.put("working", ColorCode.YELLOW);
        blockMap.put("dangerous", ColorCode.RED);
        blockMap.put("crop", ColorCode.GREEN);
        blockMap.put("other", ColorCode.GRAY);
        subtitleTypes.put("block", blockMap);
        
        // 魔咒
        Map<String, ColorCode> enchantMap = new HashMap<>();
        enchantMap.put("enchant", ColorCode.LIGHT_PURPLE);
        subtitleTypes.put("", enchantMap);
        
        // 实体
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
        
        // 物品
        Map<String, ColorCode> itemMap = new HashMap<>();
        itemMap.put("weapon", ColorCode.RED);
        itemMap.put("armor", ColorCode.GOLD);
        itemMap.put("tool", ColorCode.AQUA);
        itemMap.put("other", ColorCode.GRAY);
        subtitleTypes.put("item", itemMap);
        
        // 其他
        Map<String, ColorCode> otherMap = new HashMap<>();
        otherMap.put("other", ColorCode.GRAY);
        subtitleTypes.put("", otherMap);
    }

    public static ColorCode getColor(String path, String key) {
        Map<String, ColorCode> map = subtitleTypes.get(path);
        if (map != null) {
            return map.get(key);
        }
        return null;
    }

    public static void register() {
        // 注册数据加载器
    }
}