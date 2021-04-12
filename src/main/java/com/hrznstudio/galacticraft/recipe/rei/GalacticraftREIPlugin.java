/*
 * Copyright (c) 2019-2021 HRZN LTD
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.hrznstudio.galacticraft.recipe.rei;

import com.google.common.collect.Lists;
import com.hrznstudio.galacticraft.Constants;
import com.hrznstudio.galacticraft.api.screen.MachineHandledScreen;
import com.hrznstudio.galacticraft.block.GalacticraftBlocks;
import com.hrznstudio.galacticraft.items.GalacticraftItems;
import com.hrznstudio.galacticraft.mixin.client.HandledScreenHooks;
import com.hrznstudio.galacticraft.recipe.FabricationRecipe;
import com.hrznstudio.galacticraft.recipe.ShapedCompressingRecipe;
import com.hrznstudio.galacticraft.recipe.ShapelessCompressingRecipe;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.*;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import java.util.List;

/**
 * @author <a href="https://github.com/StellarHorizons">StellarHorizons</a>
 */
@Environment(EnvType.CLIENT)
public class GalacticraftREIPlugin implements REIPluginV0 {
    public static final ResourceLocation CIRCUIT_FABRICATION = new ResourceLocation(Constants.MOD_ID, "plugins/circuit_fabricator");
    public static final ResourceLocation COMPRESSING = new ResourceLocation(Constants.MOD_ID, "plugins/compressing");

    public void registerPluginCategories(RecipeHelper recipeHelper) {
        recipeHelper.registerCategory(new DefaultFabricationCategory());
        recipeHelper.registerCategory(new DefaultCompressingCategory());
    }

    @Override
    public void registerOthers(RecipeHelper recipeHelper) {
        recipeHelper.registerWorkingStations(CIRCUIT_FABRICATION, EntryStack.create(GalacticraftBlocks.CIRCUIT_FABRICATOR));
        recipeHelper.registerWorkingStations(COMPRESSING, EntryStack.create(GalacticraftBlocks.COMPRESSOR), EntryStack.create(GalacticraftBlocks.ELECTRIC_COMPRESSOR));
    }

    @Override
    public ResourceLocation getPluginIdentifier() {
        return new ResourceLocation(Constants.MOD_ID, Constants.MOD_ID);
    }

    @Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
        for (Recipe<?> value : recipeHelper.getRecipeManager().getRecipes()) {
            if (value instanceof FabricationRecipe) {
                recipeHelper.registerDisplay(new DefaultFabricationDisplay((FabricationRecipe) value));
            } else if (value instanceof ShapelessCompressingRecipe) {
                recipeHelper.registerDisplay(new DefaultShapelessCompressingDisplay((ShapelessCompressingRecipe) value));
            } else if (value instanceof ShapedCompressingRecipe) {
                recipeHelper.registerDisplay(new DefaultShapedCompressingDisplay((ShapedCompressingRecipe) value));
            }
        }
    }

    @Override
    public void registerBounds(DisplayHelper displayHelper) {
        BaseBoundsHandler.getInstance().registerExclusionZones(MachineHandledScreen.class, () -> {
            MachineHandledScreen<?> machineScreen = (MachineHandledScreen<?>) REIHelper.getInstance().getPreviousContainerScreen();
            HandledScreenHooks screenHooks = (HandledScreenHooks) machineScreen;
            List<Rectangle> l = Lists.newArrayList();

            if (machineScreen.securityOpen) {
                l.add(new Rectangle(screenHooks.gcr_getX() + screenHooks.gcr_getImageWidth(), screenHooks.gcr_getY(), MachineHandledScreen.PANEL_WIDTH, MachineHandledScreen.PANEL_HEIGHT));
            } else {
                l.add(new Rectangle(screenHooks.gcr_getX() + screenHooks.gcr_getImageWidth(), screenHooks.gcr_getY(), MachineHandledScreen.TAB_WIDTH, MachineHandledScreen.TAB_HEIGHT));
            }

            return l;
        });
    }

    @Override
    public void registerEntries(EntryRegistry entryRegistry) {
        for (Item item : GalacticraftItems.HIDDEN_ITEMS) {
            entryRegistry.removeEntry(EntryStack.create(item));
        }
    }
}