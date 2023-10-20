package net.kozibrodka.wolves.utils;


import net.kozibrodka.wolves.blocks.Axle;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Item;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.MathHelper;

import java.util.List;

/**
 * This is a TERRIBLE and really inefficient way of sorting utils.
 * TODO: Relocate methods into appropriate utils or discard them entirely.
 */
public class UnsortedUtils
{

    public UnsortedUtils()
    {
    }

    public static int GetOppositeFacing(int iFacing)
    {
        return iFacing ^ 1;
    }

    public static int RotateFacingAroundJ(int iFacing, boolean bReverse)
    {
        if(iFacing >= 2)
        {
            switch (iFacing) {
                case 2 -> iFacing = 5;
                case 3 -> iFacing = 4;
                case 4 -> iFacing = 2;
                case 5 -> iFacing = 3;
            }
            if(!bReverse)
            {
                iFacing = GetOppositeFacing(iFacing);
            }
        }
        return iFacing;
    }

    public static void EjectStackWithRandomOffset(Level world, int i, int j, int k, ItemInstance stack)
    {
        float xOffset = world.rand.nextFloat() * 0.7F + 0.15F;
        float yOffset = world.rand.nextFloat() * 0.2F + 0.1F;
        float zOffset = world.rand.nextFloat() * 0.7F + 0.15F;
        Item entityitem = new Item(world, (float)i + xOffset, (float)j + yOffset, (float)k + zOffset, stack);
        float velocityFactor = 0.05F;
        entityitem.velocityX = (float)world.rand.nextGaussian() * velocityFactor;
        entityitem.velocityY = (float)world.rand.nextGaussian() * velocityFactor + 0.2F;
        entityitem.velocityZ = (float)world.rand.nextGaussian() * velocityFactor;
        entityitem.pickupDelay = 10;
        world.spawnEntity(entityitem);
    }

    public static void EjectSingleItemWithRandomOffset(Level world, int i, int j, int k, int iShiftedItemIndex, int iDamage)
    {
        ItemInstance ItemInstance = new ItemInstance(iShiftedItemIndex, 1, iDamage);
        EjectStackWithRandomOffset(world, i, j, k, ItemInstance);
    }

    public static void DropStackAsIfBlockHarvested(Level world, int i, int j, int k, ItemInstance stack)
    {
        float f1 = 0.7F;
        double d = (double)(world.rand.nextFloat() * f1) + (double)(1.0F - f1) * 0.5D;
        double d1 = (double)(world.rand.nextFloat() * f1) + (double)(1.0F - f1) * 0.5D;
        double d2 = (double)(world.rand.nextFloat() * f1) + (double)(1.0F - f1) * 0.5D;
        Item entityitem = new Item(world, (double)i + d, (double)j + d1, (double)k + d2, stack);
        entityitem.pickupDelay = 10;
        world.spawnEntity(entityitem);
    }

    public static void DropSingleItemAsIfBlockHarvested(Level world, int i, int j, int k, int iShiftedItemIndex, int iDamage)
    {
        ItemInstance ItemInstance = new ItemInstance(iShiftedItemIndex, 1, iDamage);
        DropStackAsIfBlockHarvested(world, i, j, k, ItemInstance);
    }

    public static int ConvertPlacingEntityOrientationToBlockFacing(Living entityLiving)
    {
        float pitch = entityLiving.pitch;
        if(pitch > 60F)
        {
            return 1;
        }
        if(pitch < -60F)
        {
            return 0;
        } else
        {
            return ConvertPlacingEntityOrientationToFlatBlockFacing(entityLiving);
        }
    }

    public static int ConvertPlacingEntityOrientationToFlatBlockFacing(Living entityLiving)
    {
        float pitch = entityLiving.pitch;
        int l = MathHelper.floor((double)((entityLiving.yaw * 4F) / 360F) + 0.5D) & 3;
        int iFacing;
        if(l == 0)
        {
            iFacing = 2;
        } else
        if(l == 1)
        {
            iFacing = 5;
        } else
        if(l == 2)
        {
            iFacing = 3;
        } else
        {
            iFacing = 4;
        }
        return iFacing;
    }

    public static int ConvertPlacingEntityOrientationToBlockFlatFacing(Living entityLiving)
    {
        int l = MathHelper.floor((double)((entityLiving.yaw * 4F) / 360F) + 0.5D) & 3;
        int iFacing;
        if(l == 0)
        {
            iFacing = 2;
        } else
        if(l == 1)
        {
            iFacing = 5;
        } else
        if(l == 2)
        {
            iFacing = 3;
        } else
        {
            iFacing = 4;
        }
        return iFacing;
    }

    public static boolean IsBlockBeingPrecipitatedOn(Level world, int i, int j, int k)
    {
        if(!world.isRaining())
        {
            return false;
        }
        if(!world.isAboveGroundCached(i, j, k))
        {
            return false;
        }
        if(world.method_228(i, k) > j)
        {
            return false;
        }
        Biome biomegenbase = world.getBiomeSource().getBiome(i, k);
        if(biomegenbase.canSnow())
        {
            return true;
        }
        return biomegenbase.canRain();
    }

    public static void PositionAllMoveableEntitiesOutsideOfLocation(Level world, int i, int j, int k)
    {
        List list = world.getEntities((EntityBase)null, Box.createButWasteMemory(i, j, k, (double)i + 1.0D, (double)j + 1.0D, (double)k + 1.0D));
        if(list != null && list.size() > 0)
        {
            for(int listIndex = 0; listIndex < list.size(); listIndex++)
            {
                EntityBase entity = (EntityBase)list.get(listIndex);
                if(entity.method_1380() || (entity instanceof Item))
                {
                    PositionEntityOutsideOfLocation(world, entity, i, j, k);
                }
            }

        }
    }

    private static void PositionEntityOutsideOfLocation(Level world, EntityBase entity, int i, int j, int k)
    {
        double minPosX = (float)i;
        double minPosY = (float)j;
        double minPosZ = (float)k;
        double maxPosX = (float)(i + 1);
        double maxPosY = (float)(j + 1);
        double maxPosZ = (float)(k + 1);
        boolean xOverlap = false;
        boolean yOverlap = false;
        boolean zOverlap = false;
        double xOffset = 0.0D;
        double yOffset = 0.0D;
        double zOffset = 0.0D;
        if(entity.boundingBox.minX <= maxPosX && entity.boundingBox.maxX >= minPosX)
        {
            xOverlap = true;
            if(Math.abs(maxPosX - entity.boundingBox.minX) < Math.abs(minPosX - entity.boundingBox.maxX))
            {
                xOffset = (maxPosX - entity.boundingBox.minX) + 0.01D;
            } else
            {
                xOffset = minPosX - entity.boundingBox.maxX - 0.01D;
            }
        }
        if(entity.boundingBox.minY <= maxPosY && entity.boundingBox.maxY >= minPosY)
        {
            yOverlap = true;
            if(Math.abs(maxPosY - entity.boundingBox.minY) < Math.abs(minPosY - entity.boundingBox.maxY))
            {
                yOffset = (maxPosY - entity.boundingBox.minY) + 0.01D;
            } else
            {
                yOffset = minPosY - entity.boundingBox.maxY - 0.01D;
            }
        }
        if(entity.boundingBox.minZ <= maxPosZ && entity.boundingBox.maxZ >= minPosZ)
        {
            zOverlap = true;
            if(Math.abs(maxPosZ - entity.boundingBox.minZ) < Math.abs(minPosZ - entity.boundingBox.maxZ))
            {
                zOffset = (maxPosZ - entity.boundingBox.minZ) + 0.01D;
            } else
            {
                zOffset = minPosZ - entity.boundingBox.maxZ - 0.01D;
            }
        }
        double entityX = entity.x;
        double entityY = entity.y;
        double entityZ = entity.z;
        if(xOverlap && Math.abs(xOffset) < 0.20000000000000001D && (!yOverlap || Math.abs(xOffset) < Math.abs(yOffset)) && (!zOverlap || Math.abs(xOffset) < Math.abs(zOffset)))
        {
            entityX += xOffset;
        } else
        if(yOverlap && Math.abs(yOffset) < 0.20000000000000001D && (!zOverlap || Math.abs(yOffset) < Math.abs(zOffset)))
        {
            entityY += yOffset;
        } else
        if(zOverlap && Math.abs(zOffset) < 0.20000000000000001D)
        {
            entityZ += zOffset;
        }
        entity.setPosition(entityX, entityY, entityZ);
    }

//    public static void ServerPositionAllPlayerEntitiesOutsideOfLocation(Level var0, int var1, int var2, int var3)
//    {
//        // TODO: EntityPlayerMP.class
//        // TODO: getEntitiesWithinAABB
//        List var4 = var0.getEntities(ServerPlayer.class, Box.createButWasteMemory((double)var1, (double)var2, (double)var3, (double)var1 + 1.0D, (double)var2 + 1.0D, (double)var3 + 1.0D));
//
//        if (var4 != null && var4.size() > 0)
//        {
//            for (int var5 = 0; var5 < var4.size(); ++var5)
//            {
//                ServerPlayer var6 = (ServerPlayer) var4.get(var5);
//                ServerPositionPlayerEntityOutsideOfLocation(var0, var6, var1, var2, var3);
//            }
//        }
//    }
//
//    private static void ServerPositionPlayerEntityOutsideOfLocation(Level var0, ServerPlayer var1, int var2, int var3, int var4)
//    {
//        double var5 = (double)((float)var2);
//        double var7 = (double)((float)var3);
//        double var9 = (double)((float)var4);
//        double var11 = (double)((float)(var2 + 1));
//        double var13 = (double)((float)(var3 + 1));
//        double var15 = (double)((float)(var4 + 1));
//        boolean var17 = false;
//        boolean var18 = false;
//        boolean var19 = false;
//        double var20 = 0.0D;
//        double var22 = 0.0D;
//        double var24 = 0.0D;
//
//        if (var1.boundingBox.minX <= var11 && var1.boundingBox.maxX >= var5) {
//            var17 = true;
//            if (Math.abs(var11 - var1.boundingBox.minX) < Math.abs(var5 - var1.boundingBox.maxX)) {
//                var20 = var11 - var1.boundingBox.minX + 0.01D;
//            } else {
//                var20 = var5 - var1.boundingBox.maxX - 0.01D;
//            }
//        }
//
//        if (var1.boundingBox.minY <= var13 && var1.boundingBox.maxY >= var7) {
//            var18 = true;
//            if (Math.abs(var13 - var1.boundingBox.minY) < Math.abs(var7 - var1.boundingBox.maxY)) {
//                var22 = var13 - var1.boundingBox.minY + 0.01D;
//            } else {
//                var22 = var7 - var1.boundingBox.maxY - 0.01D;
//            }
//        }
//        if (var1.boundingBox.minZ <= var15 && var1.boundingBox.maxZ >= var9)
//        {
//            var19 = true;
//            if (Math.abs(var15 - var1.boundingBox.minZ) < Math.abs(var9 - var1.boundingBox.maxZ)) {
//                var24 = var15 - var1.boundingBox.minZ + 0.01D;
//            } else {
//                var24 = var9 - var1.boundingBox.maxZ - 0.01D;
//            }
//        }
//
//        double var26 = var1.x;
//        double var28 = var1.y;
//        double var30 = var1.z;
//
//        if (var17 && Math.abs(var20) < 0.2D && (!var18 || Math.abs(var20) < Math.abs(var22)) && (!var19 || Math.abs(var20) < Math.abs(var24))) {
//            var26 += var20;
//        } else
//        if (var18 && Math.abs(var22) < 0.2D && (!var19 || Math.abs(var22) < Math.abs(var24))) {
//            var28 += var22;
//        } else
//        if (var19 && Math.abs(var24) < 0.2D) {
//            var30 += var24;
//        }
//
//        var1.setPosition(var26, var28, var30);
//        var1.packetHandler.send(new PlayerPositionAndLook0xDC2SPacket(var26, var28 + 1.6200000047683716D, var28, var30, var1.yaw, var1.pitch, false));
//    }

    public static void DestroyHorizontallyAttachedAxles(Level world, int i, int j, int k)
    {
        for(int iFacing = 2; iFacing <= 5; iFacing++)
        {
            BlockPosition tempPos = new BlockPosition(i, j, k);
            tempPos.AddFacingAsOffset(iFacing);
            if(world.getTileId(tempPos.i, tempPos.j, tempPos.k) != BlockListener.fcAxleBlock.id)
            {
                continue;
            }
            Axle axleBlock = (Axle)BlockListener.fcAxleBlock;
            if(axleBlock.IsAxleOrientedTowardsFacing(world, tempPos.i, tempPos.j, tempPos.k, iFacing))
            {
                axleBlock.BreakAxle(world, tempPos.i, tempPos.j, tempPos.k);
            }
        }

    }

    public static boolean CanPlantGrowOnBlock(Level world, int i, int j, int k, BlockBase plantBlock)
    {
        int iTargetid = world.getTileId(i, j, k);
        BlockBase block = BlockBase.BY_ID[iTargetid];
        if(block != null && (block instanceof SoilTemplate))
        {
            return ((SoilTemplate)block).CanPlantGrowOnBlock(world, i, j, k, plantBlock);
        } else
        {
            return false;
        }
    }
}
