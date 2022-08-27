
package net.kozibrodka.wolves.items;

import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.projectile.Arrow;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;

public class FCItemCompositeBow extends TemplateItemBase

{
    public FCItemCompositeBow(Identifier i)
    {
        super(i);
        maxStackSize = 1;
    }

    public ItemInstance use(ItemInstance ItemInstance, Level world, PlayerBase entityplayer)
    {
        if(entityplayer.inventory.decreaseAmountOfItem(mod_FCBetterThanWolves.fcBroadheadArrow.id))
        {
            world.playSound(entityplayer, "random.bow", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
            if(!world.isServerSide)
            {
                /**
                 * Entity later
                 */
//                FCEntityBroadheadArrow arrow = new FCEntityBroadheadArrow(world, entityplayer);
//                arrow.velocityX *= 1.5D;
//                arrow.velocityY *= 1.5D;
//                arrow.velocityZ *= 1.5D;
//                world.spawnEntity(arrow);
            }
        } else
        if(entityplayer.inventory.decreaseAmountOfItem(ItemBase.arrow.id))
        {
            world.playSound(entityplayer, "random.bow", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
            if(!world.isServerSide)
            {
                Arrow arrow = new Arrow(world, entityplayer);
                arrow.velocityX *= 1.5D;
                arrow.velocityY *= 1.5D;
                arrow.velocityZ *= 1.5D;
                world.spawnEntity(arrow);
            }
        }
        return ItemInstance;
    }
}