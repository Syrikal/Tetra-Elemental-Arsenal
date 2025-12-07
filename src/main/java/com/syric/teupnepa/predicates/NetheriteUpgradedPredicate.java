package com.syric.teupnepa.predicates;

import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.ItemPredicateModular;
import se.mickelus.tetra.module.ItemModuleMajor;
import se.mickelus.tetra.module.data.ImprovementData;

import java.util.Arrays;
import java.util.Objects;

public class NetheriteUpgradedPredicate extends ItemPredicateModular {

    private static final String[] improvements = new String[]{
            "upgradednetherite/gold",
            "upgradednetherite/fire",
            "upgradednetherite/ender",
            "upgradednetherite/water",
            "upgradednetherite/wither",
            "upgradednetherite/poison",
            "upgradednetherite/phantom",
            "upgradednetherite/feather",
            "upgradednetherite/corrupt",
            "upgradednetherite/ultimate",
            "upgradednetherite/aesthetic_gold",
            "upgradednetherite/aesthetic_fire",
            "upgradednetherite/aesthetic_ender",
            "upgradednetherite/aesthetic_water",
            "upgradednetherite/aesthetic_wither",
            "upgradednetherite/aesthetic_poison",
            "upgradednetherite/aesthetic_phantom",
            "upgradednetherite/aesthetic_feather",
            "upgradednetherite/aesthetic_corrupt",
            "upgradednetherite/aesthetic_ultimate"
    };

    private String side = "";

    public NetheriteUpgradedPredicate(JsonObject jsonObject) {
        super(jsonObject);
        if (jsonObject.has("side")) {
            this.side = jsonObject.get("side").getAsString();
        }
    }

    @Override
    public boolean test(ItemStack itemStack, String slot) {
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof IModularItem) {
            return checkNetheriteImprovements(itemStack, slot);
        }

        return true;
    }

    private boolean checkNetheriteImprovements(ItemStack itemStack, String slot) {
        IModularItem item = (IModularItem) itemStack.getItem();
//        TeUpNePa.LOGGER.debug("Testing slot " + slot);
        if (slot != null) {
            ItemModuleMajor module = (ItemModuleMajor) item.getModuleFromSlot(itemStack, slot);
//            TeUpNePa.LOGGER.debug("Checking slot " + slot + " for netherite improvements");
            return hasImprovements(module, itemStack);
        }
//        TeUpNePa.LOGGER.debug("Slot was null");
        {
            return Arrays.stream(item.getMajorModules(itemStack))
                .filter(Objects::nonNull)
                .filter((module) -> module.getKey().contains(side) || side.equals(""))
                .anyMatch((module) -> this.hasImprovements(module, itemStack)
            );
        }
    }

    private boolean hasImprovements(ItemModuleMajor module, ItemStack itemStack) {
        ImprovementData[] improvementData = module.getImprovements(itemStack);
        return Arrays.stream(improvements).anyMatch((entry) -> {
            for (ImprovementData data : improvementData) {
                if (entry.equals(data.key)) {
                    return true;
                }
            }
            return false;
        });
    }

}
