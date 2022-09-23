package net.kozibrodka.wolves.blocks;

import net.kozibrodka.wolves.entity.FCEntityBlockLiftedByPlatform;
import net.kozibrodka.wolves.entity.FCEntityMovingAnchor;
import net.kozibrodka.wolves.entity.FCEntityMovingPlatform;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.utils.FCBlockPos;
import net.kozibrodka.wolves.utils.FCIBlock;
import net.minecraft.block.material.Material;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

public class FCBlockPlatform extends TemplateBlockBase
    implements FCIBlock
{

    public FCBlockPlatform(Identifier iid)
    {
        super(iid, Material.WOOD);
        setHardness(2.0F);
        setSounds(WOOD_SOUNDS);
        bPlatformAlreadyConsideredForEntityConversion = new boolean[5][5][5];
        bPlatformAlreadyConsideredForConnectedTest = new boolean[5][5][5];
        ResetPlatformConsideredForEntityConversionArray();
        ResetPlatformConsideredForConnectedTestArray();
        texture = 73;
    }

    public boolean isFullOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public boolean canSuffocate(Level world, int i, int j, int l)
    {
        return true;
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
        return true;
    }

    public void Rotate(Level world1, int l, int i1, int j1, boolean flag)
    {
    }

    private void ConvertToEntity(Level world, int i, int j, int k, FCEntityMovingAnchor associatedAnchorEntity)
    {
        FCEntityMovingPlatform entityPlatform = new FCEntityMovingPlatform(world, (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, associatedAnchorEntity);
        world.spawnEntity(entityPlatform);
        AttemptToLiftBlockWithPlatform(world, i, j + 1, k);
        world.setTile(i, j, k, 0);
    }

    private void AttemptToLiftBlockWithPlatform(Level world, int i, int j, int k)
    {
        if(FCEntityBlockLiftedByPlatform.CanBlockBeConvertedToEntity(world, i, j, k))
        {
            new FCEntityBlockLiftedByPlatform(world, i, j, k);
        }
    }

    private int GetDistToClosestConnectedAnchorPoint(Level world, int i, int j, int k)
    {
        int iClosestDist = -1;
        for(int tempI = i - 2; tempI <= i + 2; tempI++)
        {
            for(int tempJ = j - 2; tempJ <= j + 2; tempJ++)
            {
                for(int tempK = k - 2; tempK <= k + 2; tempK++)
                {
                    int iTempid = world.getTileId(tempI, tempJ, tempK);
                    if(iTempid != id)
                    {
                        continue;
                    }
                    int iUpwardsid = world.getTileId(tempI, tempJ + 1, tempK);
                    if(iUpwardsid != mod_FCBetterThanWolves.fcAnchor.id || ((FCBlockAnchor)mod_FCBetterThanWolves.fcAnchor).GetAnchorFacing(world, tempI, tempJ + 1, tempK) != 1 || !IsPlatformConnectedToAnchorPoint(world, i, j, k, tempI, tempJ, tempK))
                    {
                        continue;
                    }
                    int iTempDist = Math.abs(tempI - i) + Math.abs(tempJ - j) + Math.abs(tempK - k);
                    if(iClosestDist == -1 || iTempDist < iClosestDist)
                    {
                        iClosestDist = iTempDist;
                    }
                }
            }
        }
        return iClosestDist;
    }

    private boolean IsPlatformConnectedToAnchorPoint(Level world, int platformI, int platformJ, int platformK, int anchorPointI, int anchorPointJ, int anchorPointK)
    {
        ResetPlatformConsideredForConnectedTestArray();
        if(platformI == anchorPointI && platformJ == anchorPointJ && platformK == anchorPointK)
        {
            return true;
        } else
        {
            return PropogateTestForConnected(world, anchorPointI, anchorPointJ, anchorPointK, anchorPointI, anchorPointJ, anchorPointK, platformI, platformJ, platformK);
        }
    }

    private boolean PropogateTestForConnected(Level world, int i, int j, int k, int sourceI, int sourceJ, int sourceK, 
            int targetI, int targetJ, int targetK)
    {
        int iDeltaI = i - sourceI;
        int iDeltaJ = j - sourceJ;
        int iDeltaK = k - sourceK;
        if(bPlatformAlreadyConsideredForConnectedTest[iDeltaI + 2][iDeltaJ + 2][iDeltaK + 2])
        {
            return false;
        }
        bPlatformAlreadyConsideredForConnectedTest[iDeltaI + 2][iDeltaJ + 2][iDeltaK + 2] = true;
        for(int iFacing = 0; iFacing < 6; iFacing++)
        {
            FCBlockPos tempPos = new FCBlockPos(i, j, k);
            tempPos.AddFacingAsOffset(iFacing);
            if(tempPos.i == targetI && tempPos.j == targetJ && tempPos.k == targetK)
            {
                return true;
            }
            int iTempid = world.getTileId(tempPos.i, tempPos.j, tempPos.k);
            if(iTempid != id)
            {
                continue;
            }
            int tempDistI = Math.abs(sourceI - tempPos.i);
            int tempDistJ = Math.abs(sourceJ - tempPos.j);
            int tempDistK = Math.abs(sourceK - tempPos.k);
            if(tempDistI <= 2 && tempDistJ <= 2 && tempDistK <= 2 && PropogateTestForConnected(world, tempPos.i, tempPos.j, tempPos.k, sourceI, sourceJ, sourceK, targetI, targetJ, targetK))
            {
                return true;
            }
        }
        return false;
    }

    void ResetPlatformConsideredForEntityConversionArray()
    {
        for(int tempI = 0; tempI < 5; tempI++)
        {
            for(int tempJ = 0; tempJ < 5; tempJ++)
            {
                for(int tempK = 0; tempK < 5; tempK++)
                {
                    bPlatformAlreadyConsideredForEntityConversion[tempI][tempJ][tempK] = false;
                }

            }

        }

    }

    void ResetPlatformConsideredForConnectedTestArray()
    {
        for(int tempI = 0; tempI < 5; tempI++)
        {
            for(int tempJ = 0; tempJ < 5; tempJ++)
            {
                for(int tempK = 0; tempK < 5; tempK++)
                {
                    bPlatformAlreadyConsideredForConnectedTest[tempI][tempJ][tempK] = false;
                }
            }
        }
    }

    public void CovertToEntitiesFromThisPlatform(Level world, int i, int j, int k, FCEntityMovingAnchor associatedAnchorEntity)
    {
        ResetPlatformConsideredForEntityConversionArray();
        PropogateCovertToEntity(world, i, j, k, associatedAnchorEntity, i, j, k);
    }

    private void PropogateCovertToEntity(Level world, int i, int j, int k, FCEntityMovingAnchor associatedAnchorEntity, int sourceI, int sourceJ,
            int sourceK)
    {
        int iDeltaI = i - sourceI;
        int iDeltaJ = j - sourceJ;
        int iDeltaK = k - sourceK;
        if(bPlatformAlreadyConsideredForEntityConversion[iDeltaI + 2][iDeltaJ + 2][iDeltaK + 2])
        {
            return;
        }
        bPlatformAlreadyConsideredForEntityConversion[iDeltaI + 2][iDeltaJ + 2][iDeltaK + 2] = true;
        int distToSource = Math.abs(iDeltaI) + Math.abs(iDeltaJ) + Math.abs(iDeltaK);
        int closestAnchorDist = GetDistToClosestConnectedAnchorPoint(world, i, j, k);
        if(closestAnchorDist == -1 || distToSource <= closestAnchorDist)
        {
            ConvertToEntity(world, i, j, k, associatedAnchorEntity);
        } else
        {
            return;
        }
        for(int iFacing = 0; iFacing < 6; iFacing++)
        {
            FCBlockPos tempPos = new FCBlockPos(i, j, k);
            tempPos.AddFacingAsOffset(iFacing);
            int iTempid = world.getTileId(tempPos.i, tempPos.j, tempPos.k);
            if(iTempid != id)
            {
                continue;
            }
            int tempDistI = Math.abs(sourceI - tempPos.i);
            int tempDistJ = Math.abs(sourceJ - tempPos.j);
            int tempDistK = Math.abs(sourceK - tempPos.k);
            if(tempDistI <= 2 && tempDistJ <= 2 && tempDistK <= 2)
            {
                PropogateCovertToEntity(world, tempPos.i, tempPos.j, tempPos.k, associatedAnchorEntity, sourceI, sourceJ, sourceK);
            }
        }

    }

    private final int iPlatformTopTextureIndex = 72;
    private final int iPlatformSideTextureIndex = 73;
    private final int iPlatformBottomTextureIndex = 74;
    private boolean bPlatformAlreadyConsideredForEntityConversion[][][];
    private boolean bPlatformAlreadyConsideredForConnectedTest[][][];
}