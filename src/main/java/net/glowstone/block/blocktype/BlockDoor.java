package net.glowstone.block.blocktype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Door;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class BlockDoor extends BlockType {

    @Override
    public boolean canPlaceAt(GlowBlock block, BlockFace against) {
        return (against == BlockFace.UP);
    }

    @Override
    public void blockDestroy(GlowPlayer player, GlowBlock block, BlockFace face) {
        GlowBlockState state = block.getState();
        MaterialData data = state.getData();

        if (data instanceof Door) {
            Door door = (Door) data;
            Block b = block.getRelative(door.isTopHalf() ? BlockFace.DOWN : BlockFace.UP);
            if (b.getType() == block.getType()) {
                b.setType(Material.AIR);
            }
        } else {
            warnMaterialData(Door.class, data);
        }
    }

    @Override
    public Collection<ItemStack> getDrops(GlowBlock block, ItemStack tool) {
        Material dropType = null;
        switch (block.getType()) {
            case WOODEN_DOOR:
                dropType = Material.WOOD_DOOR;
                break;
            case IRON_DOOR_BLOCK:
                dropType = Material.IRON_DOOR;
                break;
            case SPRUCE_DOOR:
                dropType = Material.SPRUCE_DOOR_ITEM;
                break;
            case BIRCH_DOOR:
                dropType = Material.BIRCH_DOOR_ITEM;
                break;
            case JUNGLE_DOOR:
                dropType = Material.JUNGLE_DOOR_ITEM;
                break;
            case ACACIA_DOOR:
                dropType = Material.ACACIA_DOOR_ITEM;
                break;
            case DARK_OAK_DOOR:
                dropType = Material.DARK_OAK_DOOR_ITEM;
                break;
            default:
                break;
        }

        if (dropType != null) {
            return Arrays.asList(new ItemStack(dropType, 1));
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        super.placeBlock(player, state, face, holding, clickedLoc);

        MaterialData data = state.getData();
        if (data instanceof Door) {
            BlockFace facing = player.getDirection();
            ((Door) data).setFacingDirection(facing.getOppositeFace());

            GlowBlock leftBlock = null;
            switch (facing) {
                case NORTH:
                    leftBlock = state.getBlock().getRelative(BlockFace.WEST);
                    break;
                case WEST:
                    leftBlock = state.getBlock().getRelative(BlockFace.SOUTH);
                    break;
                case SOUTH:
                    leftBlock = state.getBlock().getRelative(BlockFace.EAST);
                    break;
                case EAST:
                    leftBlock = state.getBlock().getRelative(BlockFace.NORTH);
                    break;
            }

            if (leftBlock != null && leftBlock.getState().getData() instanceof Door) {
                switch (facing) {
                    case NORTH:
                        data.setData((byte) 6);
                        break;
                    case WEST:
                        data.setData((byte) 5);
                        break;
                    case SOUTH:
                        data.setData((byte) 4);
                        break;
                    case EAST:
                        data.setData((byte) 7);
                        break;
                }
            }

            GlowBlock topHalf = state.getBlock().getRelative(BlockFace.UP);
            topHalf.setType(state.getType());

            GlowBlockState topState = topHalf.getState();
            ((Door) topState.getData()).setTopHalf(true);
            topState.update();
        } else {
            warnMaterialData(Door.class, data);
        }
    }

    @Override
    public boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
        super.blockInteract(player, block, face, clickedLoc);

        if (block.getType() == Material.IRON_DOOR_BLOCK) {
            return false;
        }

        GlowBlockState state = block.getState();
        MaterialData data = state.getData();

        if (data instanceof Door) {
            Door door = (Door) data;
            if (door.isTopHalf()) {
                door = null;
                state = block.getRelative(BlockFace.DOWN).getState();
                if (state.getData() instanceof Door) {
                    door = (Door) state.getData();
                }
            }

            if (door != null) {
                door.setOpen(!door.isOpen());
            }
            state.update(true);
        }

        return true;
    }

}
