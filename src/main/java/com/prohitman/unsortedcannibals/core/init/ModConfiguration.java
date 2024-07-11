package com.prohitman.unsortedcannibals.core.init;

import com.google.common.collect.MinMaxPriorityQueue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class ModConfiguration {
    public static ForgeConfigSpec COMMON_CONFIG;

    public static final ForgeConfigSpec.BooleanValue SHOULD_SPAWN_CRAVES_FROM_FLESH;
    public static final ForgeConfigSpec.BooleanValue SHOULD_PLAY_CANNIBAL_AMBIENT_SOUNDS;
    public static final ForgeConfigSpec.DoubleValue CANNIBAL_SPAWN_ON_FLESH_CHANCE;
    public static final ForgeConfigSpec.BooleanValue SHOULD_RESPAWN_CANNIBALS;
    public static final ForgeConfigSpec.DoubleValue RESPAWN_CHANCE;
    public static final ForgeConfigSpec.IntValue PATROL_WEIGHT;

    public static final ForgeConfigSpec.IntValue MIN_SPAWN_DISTANCE;
    public static final ForgeConfigSpec.IntValue MAX_SPAWN_DISTANCE;

    public static final ForgeConfigSpec.IntValue MIN_CRAVE_SIZE;
    public static final ForgeConfigSpec.IntValue MAX_CRAVE_SIZE;

    public static final ForgeConfigSpec.IntValue MIN_FRENZY_SIZE;
    public static final ForgeConfigSpec.IntValue MAX_FRENZY_SIZE;

    public static final ForgeConfigSpec.IntValue MIN_YEARN_SIZE;
    public static final ForgeConfigSpec.IntValue MAX_YEARN_SIZE;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        COMMON_BUILDER.push("Misc. Configs");
        PATROL_WEIGHT = COMMON_BUILDER.comment("Crave Patrol spawn weights. Restart your game to see changes.")
                .worldRestart()
                .defineInRange("patrolWeight", 2, 0, 20);

        SHOULD_SPAWN_CRAVES_FROM_FLESH = COMMON_BUILDER.comment("Should spawn Craves randomly from a Reeking Flesh Block when there is enough space.")
                .define("shouldSpawnCraveInFlesh", true);

        CANNIBAL_SPAWN_ON_FLESH_CHANCE = COMMON_BUILDER.comment("Chance of Crave Cannibals spawning on reeking flesh blocks.")
                .defineInRange("spawnChanceCraveFlesh", 0.2D, 0, 1D);

        SHOULD_RESPAWN_CANNIBALS = COMMON_BUILDER.comment("Should respawn crave cannibals coninuously in campsites.")
                .define("shouldRespawn", true);

        RESPAWN_CHANCE = COMMON_BUILDER.comment("Chance of Crave Cannibals respawning in campsites.")
                .defineInRange("respawnChance", 0.25D, 0, 1D);

        SHOULD_PLAY_CANNIBAL_AMBIENT_SOUNDS = COMMON_BUILDER.comment("Should play random cannibal ambient sounds in forests and jungles.")
                .define("shouldPlayCannibalAmbientSounds", true);

        COMMON_BUILDER.pop();

        COMMON_BUILDER.push("Live Bait Configs");
        MIN_SPAWN_DISTANCE = COMMON_BUILDER.comment("Minimum distance in blocks from player of the live bait cannibal spawns. Must be smaller than the maximum distance. Maximum value for this config is 30 blocks.")
                .defineInRange("minSpawnDistance", 7, 0, 30);
        MAX_SPAWN_DISTANCE = COMMON_BUILDER.comment("Maximum distance in blocks from player of the live bait cannibal spawns. Must be bigger than the minimum distance. Maximum value for this config is 100 blocks.")
                .defineInRange("maxSpawnDistance", 13, 0, 100);

        MIN_CRAVE_SIZE = COMMON_BUILDER.comment("Minimum number of Craves in a live bait patrol. Maximum value for this config is 30 Craves.")
                .defineInRange("minCraveSize", 3, 0, 30);
        MAX_CRAVE_SIZE = COMMON_BUILDER.comment("Maximum number of Craves in a live bait patrol. This value will be added on top of the Minimum value, thus making the size of the Craves between [minCraveSize, minCraveSize + maxCraveSize]. Maximum value for this config is 30 Craves.")
                .defineInRange("maxCraveSize", 3, 0, 30);

        MIN_FRENZY_SIZE = COMMON_BUILDER.comment("Minimum number of Frenzies in a live bait patrol. Maximum value for this config is 20 Frenzies.")
                .defineInRange("minFrenzySize", 2, 0, 20);
        MAX_FRENZY_SIZE = COMMON_BUILDER.comment("Maximum number of Frenzies in a live bait patrol. This value will be added on top of the Minimum value, thus making the size of the Frenzies between [minFrenzySize, minFrenzySize + maxFrenzySize]. Maximum value for this config is 20 Frenzies.")
                .defineInRange("maxFrenzySize", 2, 0, 20);

        MIN_YEARN_SIZE = COMMON_BUILDER.comment("Minimum number of Yearns in a live bait II patrol. Maximum value for this config is 10 Yearns.")
                .defineInRange("minYearnSize", 1, 0, 10);
        MAX_YEARN_SIZE = COMMON_BUILDER.comment("Maximum number of Yearns in a live bait II patrol. This value will be added on top of the Minimum value, thus making the size of the Yearns between [minYearnSize, minYearnSize + maxYearnSize]. Maximum value for this config is 10 Yearns.")
                .defineInRange("maxYearnSize", 1, 0, 10);

        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading configEvent) { }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading configEvent) { }
}
