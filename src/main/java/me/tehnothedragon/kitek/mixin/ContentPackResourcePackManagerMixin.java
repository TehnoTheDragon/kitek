package me.tehnothedragon.kitek.mixin;

import me.tehnothedragon.kitek.registry.KitekRegistries;
import me.tehnothedragon.kitek.resource.ContentPackResourcePackProvider;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Mixin(ResourcePackManager.class)
public class ContentPackResourcePackManagerMixin {
    @Shadow
    @Final
    @Mutable
    public Set<ResourcePackProvider> providers;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void construct(ResourcePackProvider[] providers1, CallbackInfo ci) {
        providers = new LinkedHashSet<>(List.of(providers1));

        providers.add(new ContentPackResourcePackProvider(KitekRegistries.INSTANCE::getCONTENT_PACK));
    }
}
