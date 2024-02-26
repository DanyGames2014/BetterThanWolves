// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCBlockCrucible.java

package net.kozibrodka.wolves.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.container.CrucibleContainer;
import net.kozibrodka.wolves.events.GUIListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.network.GuiPacket;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.tileentity.CrucibleTileEntity;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Item;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;

import java.util.List;

public class Crucible extends TemplateBlockWithEntity
    implements RotatableBlock, BlockWithInventoryRenderer
{

    public Crucible(Identifier iid)
    {
        super(iid, Material.GLASS);
        setHardness(0.6F);
        setSounds(GLASS_SOUNDS);
        setDefaultState(getDefaultState()
                .with(LAVA, false)
                .with(FULL, 0)
        );
    }

    public boolean isFullOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public int getTextureForSide(int i)
    {
        if(i == 0)
        {
            return TextureListener.crucible_bottom;
        }
        return i != 1 ? TextureListener.crucible_side : TextureListener.crucible_top;
    }

    public boolean canUse(Level world, int i, int j, int k, PlayerBase entityPlayer)
    {
        CrucibleTileEntity tileEntityCrucible = (CrucibleTileEntity)world.getTileEntity(i, j, k);
        GUIListener.TempGuiX = i;
        GUIListener.TempGuiY = j;
        GUIListener.TempGuiZ = k;
        if(world.isServerSide){
            PacketHelper.send(new GuiPacket("crucible",0, i, j, k));
        }
        GuiHelper.openGUI(entityPlayer, Identifier.of("wolves:openCrucible"), (InventoryBase) tileEntityCrucible, new CrucibleContainer(entityPlayer.inventory, (CrucibleTileEntity) tileEntityCrucible));
        return true;
    }

    protected TileEntityBase createTileEntity()
    {
        return new CrucibleTileEntity();
    }

    public void onBlockRemoved(Level world, int i, int j, int k)
    {
        if (!SETTING_TILE) {
            InventoryHandler.ejectInventoryContents(world, i, j, k, (InventoryBase) world.getTileEntity(i, j, k));
        }
        super.onBlockRemoved(world, i, j, k);
    }

    public Box getCollisionShape(Level world, int i, int j, int k)
    {
        return Box.createButWasteMemory(i, j, k, i + 1, (double)j + 0.99000000953674316D, (double)k + 1.0D);
    }

    public void onEntityCollision(Level world, int i, int j, int k, EntityBase entity)
    {
        List collisionList = null;
        collisionList = world.getEntities(Item.class, Box.createButWasteMemory((float)i, (double)(float)j + 0.99000000953674316D, (float)k, (float)(i + 1), (double)(float)j + 0.99000000953674316D + 0.05000000074505806D, (float)(k + 1)));
        if(collisionList != null && collisionList.size() > 0)
        {
            CrucibleTileEntity tileEntityCrucible = (CrucibleTileEntity)world.getTileEntity(i, j, k);
            for(int listIndex = 0; listIndex < collisionList.size(); listIndex++)
            {
                Item targetEntityItem = (Item)collisionList.get(listIndex);
                if(targetEntityItem.removed)
                {
                    continue;
                }
                if(InventoryHandler.addItemInstanceToInventory(tileEntityCrucible, targetEntityItem.item))
                {
                     world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.pop", 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                        voicePacket(world, "random.pop", i, j, k, 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    }
                    targetEntityItem.remove();
                    continue;
                }
                int iBlockAboveID = world.getTileId(i, j + 1, k);
                if(iBlockAboveID != BlockBase.FLOWING_WATER.id && iBlockAboveID != BlockBase.STILL_WATER.id)
                {
                    continue;
                }
                double fFullBoxTop = (double)j + 1.05D;
                if(targetEntityItem.boundingBox.minY < fFullBoxTop)
                {
                    double offset = fFullBoxTop - targetEntityItem.boundingBox.minY;
                    targetEntityItem.setPosition(targetEntityItem.x, targetEntityItem.y + offset, targetEntityItem.z);
                }
            }

        }
    }

    @Environment(EnvType.SERVER)
    public void voicePacket(Level world, String name, int x, int y, int z, float g, float h){
        List list2 = world.players;
        if(list2.size() != 0) {
            for(int k = 0; k < list2.size(); k++)
            {
                ServerPlayer player1 = (ServerPlayer) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g,h));
            }
        }
    }

    public int GetFacing(BlockView iBlockAccess, int i, int j, int l)
    {
        return 0;
    }

    public void SetFacing(Level world1, int l, int i1, int j1, int k1)
    {
    }

    public boolean CanRotate(BlockView iBlockAccess, int i, int j, int l)
    {
        return false;
    }

    public boolean CanTransmitRotation(BlockView iBlockAccess, int i, int j, int l)
    {
        return false;
    }

    public void Rotate(Level world1, int l, int i1, int j1, boolean flag)
    {
    }

    public static final float m_fCrucibleHeight = 1F;
    public static final float m_fCrucibleWidth = 0.875F;
    public static final float m_fCrucibleHalfWidth = 0.4375F;
    public static final float m_fCrucibleBandHeight = 0.75F;
    public static final float m_fCrucibleBandHalfHeight = 0.375F;
    private final int m_iCrucibleTopTextureID = 42;
    private final int m_iCrucibleSideTextureID = 43;
    private final int m_iCrucibleBottomTextureID = 44;
    private final int m_iCrucibleContentsTextureID = 45;
    public static final double m_dCrucibleCollisionBoxHeight = 0.99000000953674316D;
    private static boolean SETTING_TILE = false;

    public boolean renderWorld(BlockRenderer tileRenderer, BlockView tileView, int x, int y, int z) {
        this.setBoundingBox(0.0625F, 0.0F, 0.0625F, 0.1875F, 1.0F, 0.8125F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.0625F, 0.0F, 0.8125F, 0.8125F, 1.0F, 0.9375F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.8125F, 0.0F, 0.1875F, 0.9375F, 1.0F, 0.9375F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.1875F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.1875F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.1875F, 0.0F, 0.1875F, 0.8125F, 0.125F, 0.8125F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.0F, 0.125F, 0.0F, 0.125F, 0.875F, 0.875F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.0F, 0.125F, 0.875F, 0.875F, 0.875F, 1.0F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.875F, 0.125F, 0.125F, 1.0F, 0.875F, 1.0F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.125F, 0.125F, 0.0F, 1.0F, 0.875F, 0.125F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        CrucibleTileEntity fctileentitycrucible = (CrucibleTileEntity)tileView.getTileEntity(x, y, z);
        int l = InventoryHandler.getOccupiedStacksCount(fctileentitycrucible);
        if(l > 0)
        {
            float f = (float)l / 27F;
            float f1 = 0.125F;
            float f2 = f1 + 0.0625F + (0.875F - (f1 + 0.0625F)) * f;
            this.setBoundingBox(0.125F, f1, 0.125F, 0.875F, f2, 0.875F);
            if(fctileentitycrucible.overStokedFire)
            {
//                MinecraftForgeClient.unbindTexture();
                CustomBlockRendering.RenderStandardBlockWithTexture(tileRenderer, this, x, y, z, 237);
//                MinecraftForgeClient.bindTexture(getTextureFile());
            } else
            {
                CustomBlockRendering.RenderStandardBlockWithTexture(tileRenderer, this, x, y, z, TextureListener.filler);
            }
        }
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.99F, 1.0F);
        return true;
    }

    @Override
    public void renderInventory(BlockRenderer tileRenderer, int meta) {
        this.setBoundingBox(0.0625F, 0.0F, 0.0625F, 0.1875F, 1.0F, 0.8125F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        this.setBoundingBox(0.0625F, 0.0F, 0.8125F, 0.8125F, 1.0F, 0.9375F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        this.setBoundingBox(0.8125F, 0.0F, 0.1875F, 0.9375F, 1.0F, 0.9375F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        this.setBoundingBox(0.1875F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.1875F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        this.setBoundingBox(0.1875F, 0.0F, 0.1875F, 0.8125F, 0.125F, 0.8125F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        this.setBoundingBox(0.0F, 0.125F, 0.0F, 0.125F, 0.875F, 0.875F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        this.setBoundingBox(0.0F, 0.125F, 0.875F, 0.875F, 0.875F, 1.0F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        this.setBoundingBox(0.875F, 0.125F, 0.125F, 1.0F, 0.875F, 1.0F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        this.setBoundingBox(0.125F, 0.125F, 0.0F, 1.0F, 0.875F, 0.125F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void SetHasFull(Level world, int i, int j, int k, int slots)
    {
        TileEntityBase tileEntityBase = world.getTileEntity(i, j, k);
        SETTING_TILE = true;

        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i,j,k, currentState.with(FULL, slots));

        SETTING_TILE = false;
        tileEntityBase.validate();
        world.setTileEntity(i, j, k, tileEntityBase);
    }

    public void SetHasLava(Level world, int i, int j, int k, boolean bOn)
    {
        TileEntityBase tileEntityBase = world.getTileEntity(i, j, k);
        SETTING_TILE = true;

        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i,j,k, currentState.with(LAVA, bOn));

        SETTING_TILE = false;
        tileEntityBase.validate();
        world.setTileEntity(i, j, k, tileEntityBase);
    }

    public static final BooleanProperty LAVA = BooleanProperty.of("lava");
    public static final IntProperty FULL = IntProperty.of("full",0,27);

    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder) {
        builder.add(LAVA);
        builder.add(FULL);
    }

}
