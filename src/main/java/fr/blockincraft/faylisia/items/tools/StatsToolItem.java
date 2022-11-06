package fr.blockincraft.faylisia.items.tools;

import fr.blockincraft.faylisia.items.CustomItemStack;
import fr.blockincraft.faylisia.items.StatsItemModel;
import fr.blockincraft.faylisia.items.enchantment.CustomEnchantments;
import fr.blockincraft.faylisia.player.Stats;
import fr.blockincraft.faylisia.utils.ColorsUtils;
import fr.blockincraft.faylisia.utils.TextUtils;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsToolItem extends ToolItem implements StatsItemModel {
    private final Map<Stats, Double> stats = new HashMap<>();

    public StatsToolItem(@NotNull Material material, @NotNull String id) {
        super(material, id);
    }

    /**
     * Add stats to the lore
     * @return text to add
     */
    @Override
    @NotNull
    protected List<String> firstLore(CustomItemStack customItemStack) {
        /*List<String> lore = super.firstLore(customItemStack);

        List<Map.Entry<Stats, Double>> sorted = stats.entrySet().stream().sorted((o1, o2) -> o1.getKey().index - o2.getKey().index).toList();

        sorted.forEach(entry -> {
            lore.add(ColorsUtils.translateAll("&7" + entry.getKey().name + " &" + entry.getKey().color + "+" + entry.getValue()));
        });*/

        return TextUtils.genStatsLore(customItemStack, this);
    }

    @Override
    public boolean validStats(boolean inMainHand, boolean inArmorSlot) {
        return inMainHand;
    }

    @Override
    public double getStat(@NotNull Stats stat, CustomItemStack customItemStack) {
        double value = stats.containsKey(stat) ? stats.get(stat) : 0;

        if (this.isEnchantable(customItemStack)) {
            for (Map.Entry<CustomEnchantments, Integer> entry : customItemStack.getEnchantments().entrySet()) {
                value += entry.getKey().statsBonus.itemStat(customItemStack, stat, entry.getValue());
            }
        }

        return value;
    }

    @Override
    @NotNull
    public Map<Stats, Double> getStats(CustomItemStack customItemStack) {
        return new HashMap<>(stats);
    }

    /**
     * Remove a stat to this item
     * @param stat stat to remove
     * @return this instance
     */
    @NotNull
    public StatsToolItem removeStat(@NotNull Stats stat) {
        if (isRegistered()) throw new ChangeRegisteredItem();

        stats.remove(stat);
        return this;
    }

    /**
     * Add or edit a stat on this item
     * @param stat stat to add/edit
     * @param value value of stat
     * @return this instance
     */
    @NotNull
    public StatsToolItem setStat(@NotNull Stats stat, double value) {
        if (isRegistered()) throw new ChangeRegisteredItem();

        stats.put(stat, value);
        return this;
    }
}
