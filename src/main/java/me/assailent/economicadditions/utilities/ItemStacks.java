package me.assailent.economicadditions.utilities;

import me.assailent.economicadditions.EconomicAdditions;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Skull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemStacks {
    private static EconomicAdditions plugin = EconomicAdditions.getPlugin();

    public static ItemStack createItemStack(String stringMaterial, @Nullable Integer size, @Nullable Integer customModelData, @Nullable String title, @Nullable ArrayList<String> lore, @Nullable OfflinePlayer player, @Nullable String tag, @Nullable String tagValue) {
        if (stringMaterial.equals("PLAYERHEAD")) {
            if (player != null)
                return createSkullStack(size, customModelData, title, lore, player, tag, tagValue);
            else
                stringMaterial = "PLAYER_HEAD";
        }
        ItemStack item = new ItemStack(Material.valueOf(stringMaterial), (size == null) ? 1 : size);
        ItemMeta itemMeta = item.getItemMeta();
        if (customModelData != null)
            itemMeta.setCustomModelData(customModelData);
        if (title != null) {
            String titleText;
            if (player != null) {
                titleText = PlaceholderAPI.setPlaceholders(player, title);
            } else {
                titleText = title;
            }

            itemMeta.displayName(MiniMessage.miniMessage().deserialize(titleText));
        }
        if (lore != null) {
            ArrayList<Component> loreList = new ArrayList<Component>();
            for (int i = 0; i < lore.size(); i++) {
                String loreText;
                if (player != null) {
                    loreText = PlaceholderAPI.setPlaceholders(player, lore.get(i));
                } else {
                    loreText = lore.get(i);
                }
                loreList.add(MiniMessage.miniMessage().deserialize(loreText));
            }
            itemMeta.lore(loreList);
        }
        item.setItemMeta(itemMeta);
        if (tag != null && tagValue != null)
            NBTApi.addNBT(item, tag, tagValue);

        return item;
    }

    public static ItemStack createSkullStack(@Nullable Integer size, @Nullable Integer customModelData, @Nullable String title, @Nullable ArrayList<String> lore, OfflinePlayer player, @Nullable String tag, @Nullable String tagValue) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, (size == null) ? 1 : size);
        SkullMeta itemMeta = (SkullMeta) skull.getItemMeta();
        if (customModelData != null)
            itemMeta.setCustomModelData(customModelData);
        if (title != null) {
            String titleText;
            if (player != null) {
                titleText = PlaceholderAPI.setPlaceholders(player, title);
            } else {
                titleText = title;
            }

            itemMeta.displayName(MiniMessage.miniMessage().deserialize(titleText));
        }
        if (lore != null) {
            ArrayList<Component> loreList = new ArrayList<Component>();
            for (int i = 0; i < lore.size(); i++) {
                String loreText;
                if (player != null) {
                    loreText = PlaceholderAPI.setPlaceholders(player, lore.get(i));
                } else {
                    loreText = lore.get(i);
                }
                loreList.add(MiniMessage.miniMessage().deserialize(loreText));
            }
            itemMeta.lore(loreList);
        }

        itemMeta.setOwningPlayer(player);
        skull.setItemMeta(itemMeta);
        if (tag != null && tagValue != null)
            NBTApi.addNBT(skull, tag, tagValue);

        return skull;
    }

    public static ItemStack createItemStack(Material material, @Nullable Integer size, @Nullable Integer customModelData, @Nullable String title, @Nullable ArrayList<String> lore, @Nullable OfflinePlayer player, @Nullable String tag, @Nullable String tagValue) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        if (customModelData != null)
            itemMeta.setCustomModelData(customModelData);
        if (title != null) {
            String titleText;
            if (player != null) {
                titleText = PlaceholderAPI.setPlaceholders(player, title);
            } else {
                titleText = title;
            }

            itemMeta.displayName(MiniMessage.miniMessage().deserialize(titleText));
        }
        if (lore != null) {
            ArrayList<Component> loreList = new ArrayList<Component>();
            for (int i = 0; i < lore.size(); i++) {
                String loreText;
                if (player != null) {
                    loreText = PlaceholderAPI.setPlaceholders(player, lore.get(i));
                } else {
                    loreText = lore.get(i);
                }
                loreList.add(MiniMessage.miniMessage().deserialize(loreText));
            }
            itemMeta.lore(loreList);
        }
        item.setItemMeta(itemMeta);
        if (tag != null && tagValue != null)
            NBTApi.addNBT(item, tag, tagValue);
        return item;
    }

    public static ItemStack createItemStack(ConfigurationSection item, @Nullable String tag, @Nullable String tagValue, @Nullable OfflinePlayer player) {
        String material = item.getString("Material");
        Integer size = item.getInt("Amount");
        Integer customModelData = item.getInt("CustomModelData");
        String title = item.getString("Title");
        List<String> lore = item.getStringList("Lore");
        if (tag != null && tagValue == null)
            tag = null;
        ItemStack itemStack = createItemStack(material, size, customModelData, title, (ArrayList<String>) lore, player, tag, tagValue);

        return itemStack;
    }
}