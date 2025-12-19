package com.syric.teupnepa.datagen;

import com.syric.teupnepa.registry.TUNPTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TUNPEntityTypeTagProvider extends EntityTypeTagsProvider {

    public TUNPEntityTypeTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

        this.tag(TUNPTags.EntityTypes.GOLD_DAMAGED).add(
                EntityType.PIGLIN,
                EntityType.PIGLIN_BRUTE,
                EntityType.HOGLIN,
                EntityType.ZOGLIN,
                EntityType.ZOMBIFIED_PIGLIN);
        this.tag(TUNPTags.EntityTypes.END_NATIVE).add(
                EntityType.ENDERMAN,
                EntityType.ENDERMITE,
                EntityType.ENDER_DRAGON);
        this.tag(TUNPTags.EntityTypes.SCULK).add(
                EntityType.WARDEN);
        this.tag(TUNPTags.EntityTypes.WITHER).add(
                EntityType.WITHER_SKELETON,
                EntityType.WITHER);
    }
}
