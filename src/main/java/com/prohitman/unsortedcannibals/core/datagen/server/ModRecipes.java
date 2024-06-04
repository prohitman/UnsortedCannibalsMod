package com.prohitman.unsortedcannibals.core.datagen.server;

import com.prohitman.unsortedcannibals.core.init.ModBlocks;
import com.prohitman.unsortedcannibals.core.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class ModRecipes extends RecipeProvider {
    public ModRecipes(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CRUSHER_AXE.get()).define('B', ModItems.STURDY_BONES.get()).define('S', Items.STICK)
                .pattern("BB")
                .pattern("BS")
                .pattern(" S")
                .unlockedBy("has_sturdy_bones", has(ModItems.STURDY_BONES.get()))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.RAZOR_SWORD.get()).define('B', ModItems.STURDY_BONES.get()).define('S', Items.STICK)
                .pattern("B")
                .pattern("B")
                .pattern("S")
                .unlockedBy("has_sturdy_bones", has(ModItems.STURDY_BONES.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BONE_HELMET.get()).define('B', ModItems.STURDY_BONES.get()).define('L', Items.LEATHER)
                .pattern("BBB")
                .pattern("BLB")
                .unlockedBy("has_sturdy_bones", has(ModItems.STURDY_BONES.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BONE_CHESTPLATE.get()).define('B', ModItems.STURDY_BONES.get()).define('L', Items.LEATHER)
                .pattern("BLB")
                .pattern("BBB")
                .pattern("BBB")
                .unlockedBy("has_sturdy_bones", has(ModItems.STURDY_BONES.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.BONE_BARRICADE.get()).define('B', ModItems.STURDY_BONES.get()).define('O', Items.BONE)
                .pattern("O O")
                .pattern(" B ")
                .pattern("O O")
                .unlockedBy("has_sturdy_bones", has(ModItems.STURDY_BONES.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SERRATED_SPEAR.get()).define('B', ModItems.SERRATED_SPEARHEAD.get()).define('S', Items.STICK)
                .pattern("  B")
                .pattern(" S ")
                .pattern("S  ")
                .unlockedBy("has_spearhead", has(ModItems.SERRATED_SPEARHEAD.get()))
                .save(consumer);
    }
}
