package therealeststu.dtbop.model;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class PalmLeavesModelGeometry implements IUnbakedGeometry<PalmLeavesModelGeometry> {

    protected final ResourceLocation frondsTextureLocation;

    public PalmLeavesModelGeometry(@Nullable final ResourceLocation frondsTextureLocation) {
        this.frondsTextureLocation = frondsTextureLocation;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
        return new PalmLeavesBakedModel(frondsTextureLocation, spriteGetter);
    }

    @Override
    public Collection<Material> getMaterials(IGeometryBakingContext owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        if (frondsTextureLocation == null) return new HashSet<>();
        return Collections.singleton(new Material(TextureAtlas.LOCATION_BLOCKS, frondsTextureLocation));
    }
}
