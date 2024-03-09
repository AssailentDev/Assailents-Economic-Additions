package me.assailent.economicadditions.utilities;

import me.assailent.economicadditions.EconomicAdditions;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.HashMap;

public class NBTApi {
    public static @Nullable String getNBT(@NotNull ItemStack item, String key) {
        Objects.requireNonNull(item,"item must not be null");
        if(!item.hasItemMeta()) return null;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(EconomicAdditions.getPlugin(),key);
        if(pdc.has(namespacedKey, PersistentDataType.STRING)) {
            return pdc.get(namespacedKey, PersistentDataType.STRING);
        }
        return null;
    }

    public static @Nullable String getNBT(@NotNull Entity entity, String key) {
        Objects.requireNonNull(entity,"entity must not be null");
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(EconomicAdditions.getPlugin(),key);
        if(pdc.has(namespacedKey, PersistentDataType.STRING)) {
            return pdc.get(namespacedKey, PersistentDataType.STRING);
        }
        return null;
    }

    public static void addNBT(@NotNull ItemStack item, String key, String value) {
        Objects.requireNonNull(item,"item must not be null");
        ItemMeta meta = item.hasItemMeta() ? item.getItemMeta() : Bukkit.getItemFactory().getItemMeta(item.getType());
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(EconomicAdditions.getPlugin(), key);
        pdc.set(namespacedKey, PersistentDataType.STRING, value);
        item.setItemMeta(meta);
    }

    public static void addNBT(@NotNull Entity entity, String key, String value) {
        Objects.requireNonNull(entity,"entity must not be null");
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(EconomicAdditions.getPlugin(), key);
        pdc.set(namespacedKey, PersistentDataType.STRING, value);
    }

    public static boolean hasNBT(@NotNull ItemStack item, String key) {
        Objects.requireNonNull(item,"item must not be null");
        if(!item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.has(new NamespacedKey(EconomicAdditions.getPlugin(),key),PersistentDataType.STRING);
    }

    public static boolean hasNBT(@NotNull Entity entity, String key) {
        Objects.requireNonNull(entity,"entity must not be null");
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        return pdc.has(new NamespacedKey(EconomicAdditions.getPlugin(),key),PersistentDataType.STRING);
    }

    public static void removeNBT(@NotNull ItemStack item, String key) {
        Objects.requireNonNull(item,"item must not be null");
        if(!item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.remove(new NamespacedKey(EconomicAdditions.getPlugin(),key));
        item.setItemMeta(meta);
    }

    public static void removeNBT(@NotNull Entity entity, String key) {
        Objects.requireNonNull(entity,"entity must not be null");
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        pdc.remove(new NamespacedKey(EconomicAdditions.getPlugin(),key));
    }

    public static HashMap<String,String> getAllValues(@NotNull ItemStack item) {
        Objects.requireNonNull(item,"item must not be null");
        HashMap<String,String> map = new HashMap<>();
        if(!item.hasItemMeta()) return map;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        for(NamespacedKey key : pdc.getKeys()) {
            map.put(key.toString(),pdc.get(key,PersistentDataType.STRING));
        }
        return map;
    }
}
