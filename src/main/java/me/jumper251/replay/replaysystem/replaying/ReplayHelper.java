package me.jumper251.replay.replaysystem.replaying;

import com.comphenix.packetwrapper.WrapperPlayServerTitle;
import com.comphenix.protocol.wrappers.EnumWrappers.TitleAction;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.google.gson.Gson;
import me.jumper251.replay.filesystem.ItemConfig;
import me.jumper251.replay.filesystem.ItemConfigOption;
import me.jumper251.replay.filesystem.ItemConfigType;
import me.jumper251.replay.filesystem.Messages;
import me.jumper251.replay.utils.version.MaterialBridge;
import me.jumper251.replay.utils.ReflectionHelper;
import me.jumper251.replay.utils.VersionUtil;
import me.jumper251.replay.utils.VersionUtil.VersionEnum;
import me.jumper251.replay.utils.fetcher.TextureInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.util.*;


public class ReplayHelper {

    private final static Gson GSON = new Gson();
    public static HashMap<String, Replayer> replaySessions = new HashMap<>();

    public static ItemStack createItem(ItemConfigOption option) {
        String displayName = ChatColor.translateAlternateColorCodes('&', option.getName());

        ItemStack stack = createItem(option.getMaterial(), displayName, option.getData());


        if (stack.getItemMeta() instanceof SkullMeta) {

            SkullMeta meta = (SkullMeta) stack.getItemMeta();
            if (option.getOwner() != null) {
                meta.setOwner(option.getOwner());
            }

            if (option.getTexture() != null) {
                setSkullTexture(meta, option.getTexture());
            }
            meta.setDisplayName(displayName);
            stack.setItemMeta(meta);
        }

        return stack;
    }

    public static ItemStack createItem(Material material, String name, int data) {
        ItemStack stack = new ItemStack(material, 1, (byte) data);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        stack.setItemMeta(meta);

        return stack;
    }

    public static ItemStack getPauseItem() {
        return createItem(ItemConfig.getItem(ItemConfigType.PAUSE));
    }

    public static ItemStack getResumeItem() {
        return createItem(ItemConfig.getItem(ItemConfigType.RESUME));
    }

    public static void createTeleporter(Player player, Replayer replayer, int page) {
        List<String> npcNames = new ArrayList<>(replayer.getNPCList().keySet());
        int size = (int) Math.ceil(npcNames.size() / 9.0) * 9;

        int pageSize = 9 * 5;
        int pages = (int) Math.ceil((double) size / pageSize);
        if (size <= (9 * 6)) {
            pages = 1;
            pageSize = size;
        }

        List<String> elements = npcNames.subList((page - 1) * pageSize, Math.min(page * pageSize, npcNames.size()));

        int inventorySize = pages > 1 ? pageSize + 9 : pageSize;
        Inventory inv = Bukkit.createInventory(null, inventorySize, "§7Teleporter");

        int index = 0;

        for (String name : elements) {
            ItemStack stack = new ItemStack(MaterialBridge.PLAYER_HEAD.toMaterial(), 1, (short) 3);
            SkullMeta meta = (SkullMeta) stack.getItemMeta();
            meta.setDisplayName("§6" + name);
            meta.setOwner(name);
            stack.setItemMeta(meta);

            inv.setItem(index, stack);

            index++;
        }

        if (page < pages) {
            ItemStack nextPage = new ItemStack(Material.ARROW, page + 1);
            ItemMeta nextMeta = nextPage.getItemMeta();
            nextMeta.setDisplayName(Messages.GUI_TELEPORTER_NEXT_PAGE.getFullMessage());
            nextPage.setItemMeta(nextMeta);
            inv.setItem(inventorySize - 1, nextPage);
        }
        if (page > 1) {
            ItemStack previousPage = new ItemStack(Material.ARROW, page - 1);
            ItemMeta previousMeta = previousPage.getItemMeta();
            previousMeta.setDisplayName(Messages.GUI_TELEPORTER_PREVIOUS_PAGE.getFullMessage());
            previousPage.setItemMeta(previousMeta);
            inv.setItem(inventorySize - 9, previousPage);
        }


        player.openInventory(inv);
    }


    public static void sendTitle(Player player, String title, String subTitle, int stay) {
        if (VersionUtil.isAbove(VersionEnum.V1_17)) {
            ReflectionHelper.getInstance().sendTitle(player, title, subTitle, 0, stay, 20);
            return;
        }

        WrapperPlayServerTitle packet = new WrapperPlayServerTitle();
        packet.setAction(TitleAction.TIMES);
        packet.setStay(stay);
        packet.setFadeIn(0);
        packet.setFadeOut(20);

        packet.sendPacket(player);

        if (subTitle != null) {
            WrapperPlayServerTitle sub = new WrapperPlayServerTitle();
            sub.setAction(TitleAction.SUBTITLE);
            sub.setTitle(WrappedChatComponent.fromText(subTitle));

            sub.sendPacket(player);
        }

        WrapperPlayServerTitle titlePacket = new WrapperPlayServerTitle();
        titlePacket.setAction(TitleAction.TITLE);
        titlePacket.setTitle(title != null ? WrappedChatComponent.fromText(title) : WrappedChatComponent.fromText(""));

        titlePacket.sendPacket(player);


    }

    public static boolean isInRange(Location loc1, Location loc2) {
        return loc1.getWorld().getName().equals(loc2.getWorld().getName()) && (loc1.distance(loc2) <= 48D);
    }

    public static void setSkullTexture(SkullMeta meta, String texture) {
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID(), "");
        PlayerTextures textures = profile.getTextures();
        String decoded = new String(Base64.getDecoder().decode(texture));

        TextureInfo info = GSON.fromJson(decoded, TextureInfo.class);
        if (info.getTextures() != null && info.getTextures().getSkin().getUrl() != null) {
            textures.setSkin(info.getTextures().getSkin().getUrl());
            profile.setTextures(textures);
            meta.setOwnerProfile(profile);
        }
    }

}
