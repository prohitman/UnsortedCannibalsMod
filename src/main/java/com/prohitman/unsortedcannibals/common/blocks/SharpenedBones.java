package com.prohitman.unsortedcannibals.common.blocks;

import com.prohitman.unsortedcannibals.core.init.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.service.mojang.Blackboard;

public class SharpenedBones extends Block {
    public static final BooleanProperty IS_BLOODY = BooleanProperty.create("is_bloody");
    public SharpenedBones(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(IS_BLOODY, false));
    }

    public VoxelShape makeShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1, 0.125, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.125, 0.5, 1, 0.875, 0.5), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.125, 0.5, 1, 0.875, 0.5), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.125, 0.1875, 0.82, 0.875, 0.82), BooleanOp.OR);

        return shape;
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        super.entityInside(pState, pLevel, pPos, pEntity);
    }

    public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float pFallDistance) {

        boolean isCreative = false;
        if(pEntity instanceof Player){
            Player player = (Player) pEntity;

            isCreative = player.isCreative();
        }

        if(pFallDistance > 2.0 && !isCreative && pEntity instanceof LivingEntity livingEntity){
            pEntity.causeFallDamage(pFallDistance + 2.0F, 2.5F, pLevel.damageSources().fall());
            livingEntity.addEffect(new MobEffectInstance(ModEffects.LIVE_BAIT.get(), 400), livingEntity);

            BlockState newState = pState.setValue(IS_BLOODY, true);
            pLevel.setBlock(pPos, newState, 2);
        }
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if(pEntity instanceof LivingEntity){
            pEntity.hurt(pLevel.damageSources().generic(), 1.5F);
        }
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return makeShape();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(IS_BLOODY);
    }
}
