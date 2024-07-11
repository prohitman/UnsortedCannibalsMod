package com.prohitman.unsortedcannibals.common.blocks;

import com.prohitman.unsortedcannibals.common.entities.living.CraveCannibal;
import com.prohitman.unsortedcannibals.common.entities.living.YearnCannibal;
import com.prohitman.unsortedcannibals.core.init.ModConfiguration;
import com.prohitman.unsortedcannibals.core.init.ModEntities;
import com.prohitman.unsortedcannibals.core.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ReekingFleshBlock extends Block {
    private boolean shouldSpawnCraves;
    private double spawnChance;
    public ReekingFleshBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return true;
    }
    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);

        shouldSpawnCraves = ModConfiguration.SHOULD_SPAWN_CRAVES_FROM_FLESH.get();
        spawnChance = ModConfiguration.CANNIBAL_SPAWN_ON_FLESH_CHANCE.get();

        if(shouldSpawnCraves){
            if(pRandom.nextFloat() < spawnChance){
                for(int i=1; i <=3; i++){
                    BlockPos blockPos = pPos.above(i);
                    if(!pLevel.getBlockState(blockPos).isAir()){
                        return;
                    }
                }

                CraveCannibal crave = ModEntities.CRAVE.get().create(pLevel);

                BlockPos spawnPos = pPos.above();

                crave.moveTo(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());

                crave.finalizeSpawn(pLevel, pLevel.getCurrentDifficultyAt(crave.blockPosition()), MobSpawnType.EVENT, null, null);
                pLevel.addFreshEntity(crave);
            }
        }
    }

    public void animateTick(BlockState p_221055_, Level p_221056_, BlockPos p_221057_, RandomSource p_221058_) {
        if (p_221058_.nextInt(4) == 0) {
            Direction direction = Direction.getRandom(p_221058_);

            if(direction != Direction.UP){
                BlockPos blockpos = p_221057_.relative(direction);
                BlockState blockstate = p_221056_.getBlockState(blockpos);
                if (!p_221055_.canOcclude() || !blockstate.isFaceSturdy(p_221056_, blockpos, direction.getOpposite())) {
                    double d0 = direction.getStepX() == 0 ? p_221058_.nextDouble() : 0.5D + (double)direction.getStepX() * 0.6D;
                    double d1 = direction.getStepY() == 0 ? p_221058_.nextDouble() : 0.5D + (double)direction.getStepY() * 0.6D;
                    double d2 = direction.getStepZ() == 0 ? p_221058_.nextDouble() : 0.5D + (double)direction.getStepZ() * 0.6D;
                    p_221056_.addParticle(ModParticles.BLOOD_PARTICLE.get(), (double)p_221057_.getX() + d0, (double)p_221057_.getY() + d1, (double)p_221057_.getZ() + d2, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }
}
