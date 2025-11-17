package com.techniphoenix.techniraiders.helper;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;
import net.minecraft.world.server.ServerWorld;

public class RaidHelper {
    public static boolean isEntityInRaid(LivingEntity entity) {
        World world = entity.level; // In modern Forge, this is 'level', or 'world' in older versions.

        // 1. Raids are server-side only
        if (world.isClientSide()) {
            return false;
        }

        // 2. Cast the World to ServerWorld (or ServerLevel in newer versions)
        if (world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;

            // 3. Get the entity's position
            BlockPos pos = entity.blockPosition(); // Or entity.blockPosition() in modern versions

            // 4. Use the ServerWorld method to check for a Raid at that position
            // This method returns the Raid object if one is found, or null otherwise.
            Raid raid = serverWorld.getRaidAt(pos);

            // 5. Check if a Raid object was returned
            return raid != null && raid.isActive();
        }

        return false;
    }
}
