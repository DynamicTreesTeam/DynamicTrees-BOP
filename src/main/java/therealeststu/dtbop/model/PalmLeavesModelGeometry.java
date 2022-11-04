package therealeststu.dtbop.model;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class PalmLeavesModelGeometry implements IModelGeometry<PalmLeavesModelGeometry> {

    protected final ResourceLocation frondsResLoc;

    public PalmLeavesModelGeometry (@Nullable final ResourceLocation frondsResLoc){
        this.frondsResLoc = frondsResLoc;
    }

    @Override
    public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
        return new PalmLeavesBakedModel(modelLocation, frondsResLoc);
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        if (frondsResLoc == null) return new HashSet<>();
        return Collections.singleton(new Material(TextureAtlas.LOCATION_BLOCKS, frondsResLoc));
    }
}
