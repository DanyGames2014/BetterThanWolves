// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCTileEntityCrucible.java

package net.kozibrodka.wolves.tileentity;


// Referenced classes of package net.minecraft.src:
//            TileEntity, InventoryBase, ItemInstance, FCUtilsInventory, 
//            World, EntityPlayer, NBTTagCompound, ListTag, 
//            mod_FCBetterThanWolves, Block, FoodBase, FurnaceRecipes, 
//            Item, FCUtilsMisc, ItemMap, ItemShears

import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.utils.FCUtilsInventory;
import net.kozibrodka.wolves.utils.FCUtilsMisc;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.food.FoodBase;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;

public class FCTileEntityCrucible extends TileEntityBase
    implements InventoryBase
{

    public FCTileEntityCrucible()
    {
        crucibleContents = new ItemInstance[27];
        m_iCrucibleCookCounter = 0;
        m_bOverStokedFire = false;
    }

    public int getInventorySize()
    {
        return 27;
    }

    public int getMaxItemCount()
    {
        return 64;
    }

    public ItemInstance getInventoryItem(int iSlot)
    {
        return crucibleContents[iSlot];
    }

    public ItemInstance takeInventoryItem(int iSlot, int iAmount)
    {
        return FCUtilsInventory.DecrStackSize(this, iSlot, iAmount);
    }

    public void setInventoryItem(int iSlot, ItemInstance ItemInstance)
    {
        crucibleContents[iSlot] = ItemInstance;
        if(ItemInstance != null && ItemInstance.count > getMaxItemCount())
        {
            ItemInstance.count = getMaxItemCount();
        }
        markDirty();
    }

    public String getContainerName()
    {
        return "Crucible";
    }

    public boolean canPlayerUse(PlayerBase entityplayer)
    {
        if(level.getTileEntity(x, y, z) != this)
        {
            return false;
        } else
        {
            return entityplayer.squaredDistanceTo((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 64D;
        }
    }

    public void readIdentifyingData(CompoundTag nbttagcompound)
    {
        super.readIdentifyingData(nbttagcompound);
        ListTag nbttaglist = nbttagcompound.getListTag("Items");
        crucibleContents = new ItemInstance[getInventorySize()];
        for(int i = 0; i < nbttaglist.size(); i++)
        {
            CompoundTag nbttagcompound1 = (CompoundTag)nbttaglist.get(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if(j >= 0 && j < crucibleContents.length)
            {
                crucibleContents[j] = new ItemInstance(nbttagcompound1);
            }
        }

        m_iCrucibleCookCounter = nbttagcompound.getInt("m_iCrucibleCookCounter");
    }

    public void writeIdentifyingData(CompoundTag nbttagcompound)
    {
        super.writeIdentifyingData(nbttagcompound);
        ListTag nbttaglist = new ListTag();
        for(int i = 0; i < crucibleContents.length; i++)
        {
            if(crucibleContents[i] != null)
            {
                CompoundTag nbttagcompound1 = new CompoundTag();
                nbttagcompound1.put("Slot", (byte)i);
                crucibleContents[i].toTag(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.put("Items", nbttaglist);
        nbttagcompound.put("m_iCrucibleCookCounter", m_iCrucibleCookCounter);
    }

    public void updateEntity()
    {
        int iStokedFireFactor = GetStokedFireFactor();
        if(iStokedFireFactor > 0)
        {
            if(!m_bOverStokedFire)
            {
                m_bOverStokedFire = true;
                level.method_202(x, y, z, x, y, z);
            }
            if(DoesCrucibleContainUncookedItems())
            {
                m_iCrucibleCookCounter += iStokedFireFactor;
                if(m_iCrucibleCookCounter >= 1950)
                {
                    CookContents();
                    m_iCrucibleCookCounter = 0;
                }
            } else
            {
                m_iCrucibleCookCounter = 0;
            }
        } else
        {
            if(m_bOverStokedFire)
            {
                m_bOverStokedFire = false;
                level.method_202(x, y, z, x, y, z);
            }
            m_iCrucibleCookCounter = 0;
        }
    }

    public void markDirty()
    {
        level.method_202(x, y, z, x, y, z);
    }

    public int GetStokedFireFactor()
    {
        int fireFactor = 0;
        if(level.getTileId(x, y - 1, z) == mod_FCBetterThanWolves.fcStokedFire.id)
        {
            fireFactor += 5;
            int tempY = y - 1;
            for(int tempX = x - 1; tempX <= x + 1; tempX++)
            {
                for(int tempZ = z - 1; tempZ <= z + 1; tempZ++)
                {
                    if((tempX != x || tempZ != z) && level.getTileId(tempX, tempY, tempZ) == mod_FCBetterThanWolves.fcStokedFire.id)
                    {
                        fireFactor++;
                    }
                }

            }

        }
        return fireFactor;
    }

    public int getCookProgressScaled(int iScale)
    {
        return (m_iCrucibleCookCounter * iScale) / 1950;
    }

    public boolean IsCooking()
    {
        return m_iCrucibleCookCounter > 0;
    }

    public boolean DoesCrucibleContainUncookedItems()
    {
        return DoesCrucibleContainMeltableItems() || DoesCrucibleContainIngrediantsToForgeSteel();
    }

    private void CookContents()
    {
        if(DoesCrucibleContainMeltableItems())
        {
            MeltContents();
        } else
        if(DoesCrucibleContainIngrediantsToForgeSteel())
        {
            ForgeSteel();
        }
    }

    public boolean DoesCrucibleContainSmeltableItems()
    {
        for(int tempIndex = 0; tempIndex < 27; tempIndex++)
        {
            if(crucibleContents[tempIndex] == null)
            {
                continue;
            }
            ItemBase tempItem = crucibleContents[tempIndex].getType();
            if(CanItemBeSmeltedInCrucible(tempItem))
            {
                return true;
            }
        }

        return false;
    }

    public boolean CanItemBeSmeltedInCrucible(ItemBase item)
    {
        if(item != null && !(item instanceof FoodBase))
        {
            ItemInstance smeltingResult = SmeltingRecipeRegistry.getInstance().getResult(item.id);
            if(smeltingResult != null && !(smeltingResult.getType() instanceof FoodBase) && smeltingResult.getType().id != mod_FCBetterThanWolves.fcUnfiredPottery.id)
            {
                return true;
            }
        }
        return false;
    }

    private void SmeltContents()
    {
        for(int tempIndex = 0; tempIndex < 27; tempIndex++)
        {
            if(crucibleContents[tempIndex] == null)
            {
                continue;
            }
            ItemBase tempItem = crucibleContents[tempIndex].getType();
            if(!CanItemBeSmeltedInCrucible(tempItem))
            {
                continue;
            }
            ItemInstance cookedStack = SmeltingRecipeRegistry.getInstance().getResult(crucibleContents[tempIndex].getType().id).copy();
            takeInventoryItem(tempIndex, 1);
            if(!FCUtilsInventory.AddItemInstanceToInventory(this, cookedStack))
            {
                FCUtilsMisc.EjectStackWithRandomOffset(level, x, y + 1, z, cookedStack);
            }
            return;
        }

    }

    public boolean DoesCrucibleContainMeltableItems()
    {
        int iRailCount = 0;
        int iBroadheadArrowheadCount = 0;
        int iBroadheadArrowCount = 0;
        for(int tempIndex = 0; tempIndex < 27; tempIndex++)
        {
            if(crucibleContents[tempIndex] == null)
            {
                continue;
            }
            ItemBase tempItem = crucibleContents[tempIndex].getType();
            if(GetMeltingResult(tempItem) != null)
            {
                return true;
            }
            if(tempItem.id == BlockBase.RAIL.id)
            {
                iRailCount += crucibleContents[tempIndex].count;
                if(iRailCount >= 16)
                {
                    return true;
                }
                continue;
            }
            if(tempItem.id == mod_FCBetterThanWolves.fcBroadheadArrowhead.id)
            {
                iBroadheadArrowheadCount += crucibleContents[tempIndex].count;
                if(iBroadheadArrowheadCount >= 4)
                {
                    return true;
                }
                continue;
            }
            if(tempItem.id != mod_FCBetterThanWolves.fcBroadheadArrow.id)
            {
                continue;
            }
            iBroadheadArrowCount += crucibleContents[tempIndex].count;
            if(iBroadheadArrowCount >= 16)
            {
                return true;
            }
        }

        return false;
    }

    private void MeltContents()
    {
        int iRailCount = 0;
        int iBroadheadArrowheadCount = 0;
        int iBroadheadArrowCount = 0;
        for(int tempIndex = 0; tempIndex < 27; tempIndex++)
        {
            if(crucibleContents[tempIndex] == null)
            {
                continue;
            }
            ItemBase tempItem = crucibleContents[tempIndex].getType();
            ItemInstance cookedStack = GetMeltingResult(tempItem);
            if(cookedStack != null)
            {
                takeInventoryItem(tempIndex, 1);
                if(!FCUtilsInventory.AddItemInstanceToInventory(this, cookedStack))
                {
                    FCUtilsMisc.EjectStackWithRandomOffset(level, x, y + 1, z, cookedStack);
                }
                return;
            }
            if(tempItem.id == BlockBase.RAIL.id)
            {
                iRailCount += crucibleContents[tempIndex].count;
                if(iRailCount < 16)
                {
                    continue;
                }
                FCUtilsInventory.ConsumeItemsInInventory(this, BlockBase.RAIL.id, -1, 16);
                cookedStack = new ItemInstance(ItemBase.ironIngot, 6);
                if(!FCUtilsInventory.AddItemInstanceToInventory(this, cookedStack))
                {
                    FCUtilsMisc.EjectStackWithRandomOffset(level, x, y + 1, z, cookedStack);
                }
                return;
            }
            if(tempItem.id == mod_FCBetterThanWolves.fcBroadheadArrowhead.id)
            {
                iBroadheadArrowheadCount += crucibleContents[tempIndex].count;
                if(iBroadheadArrowheadCount < 4)
                {
                    continue;
                }
                FCUtilsInventory.ConsumeItemsInInventory(this, mod_FCBetterThanWolves.fcBroadheadArrowhead.id, -1, 4);
                cookedStack = new ItemInstance(mod_FCBetterThanWolves.fcSteel, 1);
                if(!FCUtilsInventory.AddItemInstanceToInventory(this, cookedStack))
                {
                    FCUtilsMisc.EjectStackWithRandomOffset(level, x, y + 1, z, cookedStack);
                }
                return;
            }
            if(tempItem.id != mod_FCBetterThanWolves.fcBroadheadArrow.id)
            {
                continue;
            }
            iBroadheadArrowCount += crucibleContents[tempIndex].count;
            if(iBroadheadArrowCount < 16)
            {
                continue;
            }
            FCUtilsInventory.ConsumeItemsInInventory(this, mod_FCBetterThanWolves.fcBroadheadArrow.id, -1, 16);
            cookedStack = new ItemInstance(mod_FCBetterThanWolves.fcSteel, 1);
            if(!FCUtilsInventory.AddItemInstanceToInventory(this, cookedStack))
            {
                FCUtilsMisc.EjectStackWithRandomOffset(level, x, y + 1, z, cookedStack);
            }
            return;
        }

    }

    ItemInstance GetMeltingResult(ItemBase item)
    {
        ItemInstance tempStack = null;
        int iShiftedIndex = item.id;
        if(iShiftedIndex == ItemBase.bucket.id || iShiftedIndex == ItemBase.lavaBucket.id || iShiftedIndex == ItemBase.waterBucket.id || iShiftedIndex == ItemBase.milk.id || iShiftedIndex == mod_FCBetterThanWolves.fcBucketCement.id)
        {
            tempStack = new ItemInstance(ItemBase.ironIngot, 3);
        } else
        if(iShiftedIndex == ItemBase.ironPickaxe.id || iShiftedIndex == ItemBase.ironAxe.id)
        {
            tempStack = new ItemInstance(ItemBase.ironIngot, 3);
        } else
        if(iShiftedIndex == ItemBase.ironSword.id || iShiftedIndex == ItemBase.ironHoe.id)
        {
            tempStack = new ItemInstance(ItemBase.ironIngot, 2);
        } else
        if(iShiftedIndex == ItemBase.ironShovel.id)
        {
            tempStack = new ItemInstance(ItemBase.ironIngot, 1);
        } else
        if(iShiftedIndex == ItemBase.ironHelmet.id)
        {
            tempStack = new ItemInstance(ItemBase.ironIngot, 5);
        } else
        if(iShiftedIndex == ItemBase.ironChestplate.id)
        {
            tempStack = new ItemInstance(ItemBase.ironIngot, 8);
        } else
        if(iShiftedIndex == ItemBase.ironLeggings.id)
        {
            tempStack = new ItemInstance(ItemBase.ironIngot, 7);
        } else
        if(iShiftedIndex == ItemBase.ironBoots.id)
        {
            tempStack = new ItemInstance(ItemBase.ironIngot, 4);
        } else
        if(iShiftedIndex == ItemBase.compass.id)
        {
            tempStack = new ItemInstance(ItemBase.ironIngot, 4);
        } else
        if(iShiftedIndex == ItemBase.flintAndSteel.id)
        {
            tempStack = new ItemInstance(ItemBase.ironIngot, 1);
        } else
        if(iShiftedIndex == ItemBase.ironDoor.id)
        {
            tempStack = new ItemInstance(ItemBase.ironIngot, 6);
        } else
        if(iShiftedIndex == ItemBase.map.id)
        {
            tempStack = new ItemInstance(ItemBase.ironIngot, 4);
        } else
        if(iShiftedIndex == ItemBase.shears.id)
        {
            tempStack = new ItemInstance(ItemBase.ironIngot, 2);
        } else
        if(iShiftedIndex == BlockBase.DETECTOR_RAIL.id || iShiftedIndex == mod_FCBetterThanWolves.fcDetectorRailWood.id || iShiftedIndex == mod_FCBetterThanWolves.fcDetectorRailObsidian.id)
        {
            tempStack = new ItemInstance(ItemBase.ironIngot, 1);
        } else
        if(iShiftedIndex == BlockBase.IRON_BLOCK.id)
        {
            tempStack = new ItemInstance(ItemBase.ironIngot, 9);
        } else
        if(iShiftedIndex == ItemBase.minecart.id || iShiftedIndex == ItemBase.minecartChest.id || iShiftedIndex == ItemBase.minecartFurnace.id)
        {
            tempStack = new ItemInstance(ItemBase.ironIngot, 5);
        } else
        if(iShiftedIndex == ItemBase.goldPickaxe.id || iShiftedIndex == ItemBase.goldAxe.id)
        {
            tempStack = new ItemInstance(ItemBase.goldIngot, 3);
        } else
        if(iShiftedIndex == ItemBase.goldSword.id || iShiftedIndex == ItemBase.goldHoe.id)
        {
            tempStack = new ItemInstance(ItemBase.goldIngot, 2);
        } else
        if(iShiftedIndex == ItemBase.goldShovel.id)
        {
            tempStack = new ItemInstance(ItemBase.goldIngot, 1);
        } else
        if(iShiftedIndex == ItemBase.goldHelmet.id)
        {
            tempStack = new ItemInstance(ItemBase.goldIngot, 5);
        } else
        if(iShiftedIndex == ItemBase.goldChestplate.id)
        {
            tempStack = new ItemInstance(ItemBase.goldIngot, 8);
        } else
        if(iShiftedIndex == ItemBase.goldLeggings.id)
        {
            tempStack = new ItemInstance(ItemBase.goldIngot, 7);
        } else
        if(iShiftedIndex == ItemBase.goldBoots.id)
        {
            tempStack = new ItemInstance(ItemBase.goldIngot, 4);
        } else
        if(iShiftedIndex == ItemBase.clock.id)
        {
            tempStack = new ItemInstance(ItemBase.goldIngot, 4);
        } else
        if(iShiftedIndex == BlockBase.GOLDEN_RAIL.id)
        {
            tempStack = new ItemInstance(ItemBase.goldIngot, 1);
        } else
        if(iShiftedIndex == BlockBase.GOLD_BLOCK.id)
        {
            tempStack = new ItemInstance(ItemBase.goldIngot, 9);
        } else
        if(iShiftedIndex == mod_FCBetterThanWolves.fcRefinedPickAxe.id || iShiftedIndex == mod_FCBetterThanWolves.fcRefinedAxe.id)
        {
            tempStack = new ItemInstance(mod_FCBetterThanWolves.fcSteel, 3);
        } else
        if(iShiftedIndex == mod_FCBetterThanWolves.fcRefinedSword.id || iShiftedIndex == mod_FCBetterThanWolves.fcRefinedHoe.id)
        {
            tempStack = new ItemInstance(mod_FCBetterThanWolves.fcSteel, 2);
        } else
        if(iShiftedIndex == mod_FCBetterThanWolves.fcRefinedShovel.id)
        {
            tempStack = new ItemInstance(mod_FCBetterThanWolves.fcSteel, 1);
        } else
        if(iShiftedIndex == mod_FCBetterThanWolves.fcAnvil.id)
        {
            tempStack = new ItemInstance(mod_FCBetterThanWolves.fcSteel, 7);
        } else
        if(iShiftedIndex == mod_FCBetterThanWolves.fcArmorPlate.id)
        {
            tempStack = new ItemInstance(mod_FCBetterThanWolves.fcSteel, 1);
        } else
        if(iShiftedIndex == mod_FCBetterThanWolves.fcPlateHelm.id)
        {
            tempStack = new ItemInstance(mod_FCBetterThanWolves.fcSteel, 5);
        } else
        if(iShiftedIndex == mod_FCBetterThanWolves.fcPlateBreastPlate.id)
        {
            tempStack = new ItemInstance(mod_FCBetterThanWolves.fcSteel, 8);
        } else
        if(iShiftedIndex == mod_FCBetterThanWolves.fcPlateLeggings.id)
        {
            tempStack = new ItemInstance(mod_FCBetterThanWolves.fcSteel, 7);
        } else
        if(iShiftedIndex == mod_FCBetterThanWolves.fcPlateBoots.id)
        {
            tempStack = new ItemInstance(mod_FCBetterThanWolves.fcSteel, 4);
        }
        return tempStack;
    }

    public boolean DoesCrucibleContainIngrediantsToForgeSteel()
    {
        boolean bContainsCoalDust = false;
        boolean bContainsConcentratedHellfire = false;
        int iIronCount = 0;
        for(int tempIndex = 0; tempIndex < 27; tempIndex++)
        {
            if(crucibleContents[tempIndex] == null)
            {
                continue;
            }
            ItemBase tempItem = crucibleContents[tempIndex].getType();
            if(tempItem.id == mod_FCBetterThanWolves.fcCoalDust.id)
            {
                bContainsCoalDust = true;
                if(iIronCount >= 3 && bContainsConcentratedHellfire)
                {
                    return true;
                }
                continue;
            }
            if(tempItem.id == mod_FCBetterThanWolves.fcConcentratedHellfire.id)
            {
                bContainsConcentratedHellfire = true;
                if(iIronCount >= 3 && bContainsCoalDust)
                {
                    return true;
                }
                continue;
            }
            if(tempItem.id != ItemBase.ironIngot.id)
            {
                continue;
            }
            iIronCount += crucibleContents[tempIndex].count;
            if(iIronCount >= 3 && bContainsCoalDust && bContainsConcentratedHellfire)
            {
                return true;
            }
        }

        return false;
    }

    private void ForgeSteel()
    {
        FCUtilsInventory.ConsumeItemsInInventory(this, mod_FCBetterThanWolves.fcCoalDust.id, -1, 1);
        FCUtilsInventory.ConsumeItemsInInventory(this, mod_FCBetterThanWolves.fcConcentratedHellfire.id, -1, 1);
        FCUtilsInventory.ConsumeItemsInInventory(this, ItemBase.ironIngot.id, -1, 3);
        ItemInstance forgedSteelStack = new ItemInstance(mod_FCBetterThanWolves.fcSteel, 4);
        if(!FCUtilsInventory.AddItemInstanceToInventory(this, forgedSteelStack))
        {
            FCUtilsMisc.EjectStackWithRandomOffset(level, x, y + 1, z, forgedSteelStack);
        }
    }

    private final int iCrucibleInventorySize = 27;
    private final int iCrucibleStackSizeLimit = 64;
    private final double dCrucibleMaxPlayerInteractionDist = 64D;
    private final int iPrimaryFireFactor = 5;
    private final int iSecondaryFireFactor = 1;
    private final int iCrucibleTimeToCook = 1950;
    private ItemInstance crucibleContents[];
    public int m_iCrucibleCookCounter;
    public boolean m_bOverStokedFire;
}