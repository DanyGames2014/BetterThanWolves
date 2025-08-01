package net.kozibrodka.wolves.events;

import net.kozibrodka.wolves.entity.*;
import net.kozibrodka.wolves.render.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.render.entity.EntityRendererRegisterEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

public class TextureListener {

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @EventListener
    public void registerTextures(TextureRegisterEvent event) {
        axle_horizontal = registerBlockTexture("block/fcBlockAxle_side");
        axle_vertical = registerBlockTexture("block/fcBlockAxle_top");
        axle_side = registerBlockTexture("block/fcBlockAxle_front");
        gearbox_front = registerBlockTexture("block/fcBlockGearBox_in");
        gearbox_output = registerBlockTexture("block/fcBlockGearBox_out");
        gearbox_side = registerBlockTexture("block/fcBlockGearBox_side");
        anvil = registerBlockTexture("block/fcBlockAnvil");
        bulb_on = registerBlockTexture("block/fcBlockLightBulb_on");
        bulb_off = registerBlockTexture("block/fcBlockLightBulb_off");
        hibachi_side = registerBlockTexture("block/fcBlockHibachi_side");
        hibachi_top = registerBlockTexture("block/fcBlockHibachi_top");
        hibachi_bottom = registerBlockTexture("block/fcBlockHibachi_down");
        bellows_front = registerBlockTexture("block/fcBlockBellows_front");
        bellows_side = registerBlockTexture("block/fcBlockBellows_side");
        bellows_top = registerBlockTexture("block/fcBlockBellows_top");
        handcrack_top = registerBlockTexture("block/fcBlockHandCrank_top");
        handcrack_side = registerBlockTexture("block/fcBlockHandCrank_side");
        handcrack_bottom = registerBlockTexture("block/fcBlockHandCrank_down");
        handcrack_lever = registerBlockTexture("block/fcBlockHandCrank_lever");
        millstone_top = registerBlockTexture("block/fcBlockMillStone_top");
        millstone_bottom = registerBlockTexture("block/fcBlockMillStone_down");
        millstone_side = registerBlockTexture("block/fcBlockMillStone_side");
        anchor_side = registerBlockTexture("block/fcBlockAnchor_side");
        anchor_top = registerBlockTexture("block/fcBlockAnchor_top");
        anchor_button = registerBlockTexture("block/fcBlockAnchor_button");
        rope = registerBlockTexture("block/fcBlockRope");
        pulley_bottom = registerBlockTexture("block/fcBlockPulley_down");
        pulley_top = registerBlockTexture("block/fcBlockPulley_top");
        pulley_side = registerBlockTexture("block/fcBlockPulley_side");
        turntable_bottom = registerBlockTexture("block/fcBlockTurntable_down");
        turntable_top = registerBlockTexture("block/fcBlockTurntable_top");
        turntable_side = registerBlockTexture("block/fcBlockTurntable_side");
        turntable_button = registerBlockTexture("block/fcBlockTurntable_button");
        unfiredpottery = registerBlockTexture("block/fcBlockUnfiredPottery");
        unfiredpottery_cook = registerBlockTexture("block/fcBlockUnfiredPottery_cook");
        crucible_bottom = registerBlockTexture("block/fcBlockCrucible_down");
        crucible_side = registerBlockTexture("block/fcBlockCrucible_side");
        crucible_top = registerBlockTexture("block/fcBlockCrucible_top");
        companion_face = registerBlockTexture("block/fcBlockCompanionCube_face");
        companion_side = registerBlockTexture("block/fcBlockCompanionCube_side");
        companion_meat = registerBlockTexture("block/fcBlockCompanionCube_meat");
        planter = registerBlockTexture("block/fcBlockPlanter");
        planter_soil = registerBlockTexture("block/fcBlockPlanter_soil");
        filler = registerBlockTexture("block/fcBlockFiller");
        cement = registerBlockTexture("block/fcBlockCement");
        cement_dry = registerBlockTexture("block/fcBlockCement_dry");
        vase_white = registerBlockTexture("block/fcBlockVase_white");
        vase_orange = registerBlockTexture("block/fcBlockVase_orange");
        vase_magenta = registerBlockTexture("block/fcBlockVase_magenta");
        vase_light_blue = registerBlockTexture("block/fcBlockVase_light_blue");
        vase_yellow = registerBlockTexture("block/fcBlockVase_yellow");
        vase_lime = registerBlockTexture("block/fcBlockVase_lime");
        vase_pink = registerBlockTexture("block/fcBlockVase_pink");
        vase_gray = registerBlockTexture("block/fcBlockVase_gray");
        vase_light_gray = registerBlockTexture("block/fcBlockVase_light_gray");
        vase_cyan = registerBlockTexture("block/fcBlockVase_cyan");
        vase_purple = registerBlockTexture("block/fcBlockVase_purple");
        vase_blue = registerBlockTexture("block/fcBlockVase_blue");
        vase_brown = registerBlockTexture("block/fcBlockVase_brown");
        vase_green = registerBlockTexture("block/fcBlockVase_green");
        vase_red = registerBlockTexture("block/fcBlockVase_red");
        vase_black = registerBlockTexture("block/fcBlockVase_black");
        cauldron_top = registerBlockTexture("block/fcBlockCauldron_top");
        cauldron_side = registerBlockTexture("block/fcBlockCauldron_side");
        cauldron_bottom = registerBlockTexture("block/fcBlockCauldron_down");
        hopper_bottom = registerBlockTexture("block/fcBlockHopper_down");
        hopper_side = registerBlockTexture("block/fcBlockHopper_side");
        hopper_top = registerBlockTexture("block/fcBlockHopper_top");
        hopper_wicker = registerBlockTexture("block/fcBlockHopper_wicker");
        hopper_grate = registerBlockTexture("block/fcBlockHopper_grate");
        hopper_rollers = registerBlockTexture("block/fcBlockHopper_rollers");
        hopper_trapdoor = registerBlockTexture("block/fcBlockHopper_trapdoor");
        hopper_ladder = registerBlockTexture("block/fcBlockHopper_ladder");
        hopper_soulsand = registerBlockTexture("block/fcBlockHopper_soulsand");
        platform_top = registerBlockTexture("block/fcBlockPlatform_top");
        platform_side = registerBlockTexture("block/fcBlockPlatform_side");
        hemp_0 = registerBlockTexture("block/fcBlockHemp_0");
        hemp_1 = registerBlockTexture("block/fcBlockHemp_1");
        hemp_2 = registerBlockTexture("block/fcBlockHemp_2");
        hemp_3 = registerBlockTexture("block/fcBlockHemp_3");
        hemp_4 = registerBlockTexture("block/fcBlockHemp_4");
        hemp_5 = registerBlockTexture("block/fcBlockHemp_5");
        hemp_6 = registerBlockTexture("block/fcBlockHemp_6");
        hemp_7 = registerBlockTexture("block/fcBlockHemp_7");
        dispenser_face = registerBlockTexture("block/fcBlockDispenser_face");
        dispenser_side = registerBlockTexture("block/fcBlockDispenser_side");
        dispenser_top = registerBlockTexture("block/fcBlockDispenser_top");
        dispenser_bottom = registerBlockTexture("block/fcBlockDispenser_down");
        saw_side = registerBlockTexture("block/fcBlockSaw_side");
        saw_saw = registerBlockTexture("block/fcBlockSaw_saw");
        saw_face = registerBlockTexture("block/fcBlockSaw_face");
        rail_wood = registerBlockTexture("block/fcRailDetector_wood");
        rail_obsidian = registerBlockTexture("block/fcRailDetector_obsidian");
        detector_on = registerBlockTexture("block/fcBlockDetector_on");
        detector_off = registerBlockTexture("block/fcBlockDetector_off");
        detector_side = registerBlockTexture("block/fcBlockDetector_side");
        detector_top = registerBlockTexture("block/fcBlockDetector_top");
        detector_bottom = registerBlockTexture("block/fcBlockDetector_down");
        BlockListener.blockOfGrates.specifyTextures(getBlockTexture("item/fcGrate"));
        BlockListener.collisionBlock.specifyTextures(getBlockTexture("block/blank"));
        BlockListener.obstructionBlock.specifyTextures(getBlockTexture("block/blank"));
        BlockListener.blockOfSteel.specifyTextures(getBlockTexture("block/steel"));

        ItemListener.bucketCement.setTexture(Identifier.of(NAMESPACE, "item/bucketCement"));
        ItemListener.soulFilter.setTexture(Identifier.of(NAMESPACE, "item/soulFilter"));
        ItemListener.nothing.setTexture(Identifier.of(NAMESPACE, "item/nothing"));
        ItemListener.wickerWeaving.setTexture(Identifier.of(NAMESPACE, "item/wickerWeaving"));
        ItemListener.wickerSheet.setTexture(Identifier.of(NAMESPACE, "item/wickerSheet"));

        BlockListener.detectorRailObsidian.asItem().setTextureId(registerBlockTexture("block/fcRailDetector_obsidian"));
        BlockListener.detectorRailWood.asItem().setTextureId(registerBlockTexture("block/fcRailDetector_wood"));
        BlockListener.cement.asItem().setTextureId(cement);
        BlockListener.hempCrop.asItem().setTextureId(registerBlockTexture("block/fcBlockHemp_7"));
    }

    private int registerBlockTexture(String s) {
        if(s == null) {
            return 0;
        }
        return Atlases.getTerrain().addTexture(Identifier.of(NAMESPACE, s)).index;
    }

    @EventListener
    public static void registerEntityRenderers(EntityRendererRegisterEvent event) {
        event.renderers.put(WaterWheelEntity.class, new WaterWheelRenderer());
        event.renderers.put(WindMillEntity.class, new WindMillRenderer());
        event.renderers.put(BroadheadArrowEntity.class, new BroadHeadArrowRenderer());
        event.renderers.put(LiftedBlockEntity.class, new LiftedBlockRenderer());
        event.renderers.put(MovingPlatformEntity.class, new MovingPlatformRenderer());
        event.renderers.put(MovingAnchorEntity.class, new MovingAnchorRenderer());
        event.renderers.put(FallingAnvilEntity.class, new FallingAnvilRenderer());
        event.renderers.put(FCEntityTEST.class, new FCEntityTestRenderer());
    }

    public int getBlockTexture(String path) {
        return Atlases.getTerrain().addTexture(Identifier.of(NAMESPACE, path)).index;
    }

    public static int axle_vertical;
    public static int axle_horizontal;
    public static int axle_side;
    public static int gearbox_front;
    public static int gearbox_output;
    public static int gearbox_side;
    public static int anvil;
    public static int bulb_on;
    public static int bulb_off;
    public static int hibachi_side;
    public static int hibachi_top;
    public static int hibachi_bottom;
    public static int bellows_front;
    public static int bellows_side;
    public static int bellows_top;
    public static int handcrack_top;
    public static int handcrack_side;
    public static int handcrack_bottom;
    public static int handcrack_lever;
    public static int millstone_top;
    public static int millstone_bottom;
    public static int millstone_side;
    public static int anchor_side;
    public static int anchor_top;
    public static int anchor_button;
    public static int rope;
    public static int pulley_bottom;
    public static int pulley_top;
    public static int pulley_side;
    public static int turntable_bottom;
    public static int turntable_top;
    public static int turntable_side;
    public static int turntable_button;
    public static int unfiredpottery;
    public static int unfiredpottery_cook;
    public static int crucible_side;
    public static int crucible_top;
    public static int crucible_bottom;
    public static int companion_face;
    public static int companion_side;
    public static int companion_meat;
    public static int planter;
    public static int planter_soil;
    public static int filler;
    public static int cement;
    public static int cement_dry;
    public static int vase_white;
    public static int vase_orange;
    public static int vase_magenta;
    public static int vase_light_blue;
    public static int vase_yellow;
    public static int vase_lime;
    public static int vase_pink;
    public static int vase_gray;
    public static int vase_light_gray;
    public static int vase_cyan;
    public static int vase_purple;
    public static int vase_blue;
    public static int vase_brown;
    public static int vase_green;
    public static int vase_red;
    public static int vase_black;
    public static int cauldron_top;
    public static int cauldron_side;
    public static int cauldron_bottom;
    public static int hopper_side;
    public static int hopper_top;
    public static int hopper_bottom;
    public static int hopper_rollers;
    public static int hopper_grate;
    public static int hopper_wicker;
    public static int hopper_ladder;
    public static int hopper_trapdoor;
    public static int hopper_soulsand;
    public static int platform_side;
    public static int platform_top;
    public static int hemp_0;
    public static int hemp_1;
    public static int hemp_2;
    public static int hemp_3;
    public static int hemp_4;
    public static int hemp_5;
    public static int hemp_6;
    public static int hemp_7;
    public static int dispenser_face;
    public static int dispenser_side;
    public static int dispenser_top;
    public static int dispenser_bottom;
    public static int saw_face;
    public static int saw_side;
    public static int saw_saw;
    public static int rail_wood;
    public static int rail_obsidian;
    public static int detector_on;
    public static int detector_off;
    public static int detector_side;
    public static int detector_top;
    public static int detector_bottom;
}
