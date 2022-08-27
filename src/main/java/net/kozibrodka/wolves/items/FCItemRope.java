
package net.kozibrodka.wolves.items;

import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.utils.FCUtilsWorld;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;

public class FCItemRope extends TemplateItemBase
{

    public FCItemRope(Identifier iItemID)
    {
        super(iItemID);
    }

    public boolean useOnTile(ItemInstance ItemInstance, PlayerBase entityplayer, Level world, int i, int j, int k, int iFacing)
    {
        if(ItemInstance.count == 0)
        {
            return false;
        }
        int iTargetid = world.getTileId(i, j, k);
        if(iTargetid == mod_FCBetterThanWolves.fcAnchor.id || iTargetid == mod_FCBetterThanWolves.fcRopeBlock.id)
        {
            for(int tempj = j - 1; tempj >= 0; tempj--)
            {
                int iTempid = world.getTileId(i, tempj, k);
                if(FCUtilsWorld.IsReplaceableBlock(world, i, tempj, k))
                {
                    if(world.setTile(i, tempj, k, mod_FCBetterThanWolves.fcRopeBlock.id))
                    {
                        mod_FCBetterThanWolves.fcRopeBlock.onBlockPlaced(world, i, tempj, k, iFacing);
                        mod_FCBetterThanWolves.fcRopeBlock.afterPlaced(world, i, tempj, k, entityplayer);
                        world.playSound((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, mod_FCBetterThanWolves.fcRopeBlock.sounds.getWalkSound(), (mod_FCBetterThanWolves.fcRopeBlock.sounds.getVolume() + 1.0F) / 2.0F, mod_FCBetterThanWolves.fcRopeBlock.sounds.getPitch() * 0.8F);
                        ItemInstance.count--;
                        return true;
                    } else
                    {
                        return false;
                    }
                }
                if(iTempid != mod_FCBetterThanWolves.fcRopeBlock.id)
                {
                    return false;
                }
            }

        }
        return false;
    }

}
