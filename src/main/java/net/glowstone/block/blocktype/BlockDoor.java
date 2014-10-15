package net.glowstone.block.blocktype;

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

<<<<<<< HEAD
    public boolean canPlaceAt(GlowBlock block, BlockFace against) {
        return (against == BlockFace.UP);
    }

    /**
     * Removes the adjacent door block to the door
     * 
     * @Override
     */
    public void blockDestroy(GlowPlayer player, GlowBlock block, BlockFace face, Vector blockLoc) {
        GlowBlockState state = block.getState();
        MaterialData data = state.getData();

        if (data instanceof Door) {
            Door door = (Door) data;
            if (door.isTopHalf()) {
                Block b = block.getRelative(BlockFace.DOWN);
                if (b.getType() == block.getType())
                    b.setType(Material.AIR);
            } else {
                Block b = block.getRelative(BlockFace.UP);
                if (b.getType() == block.getType())
                    b.setType(Material.AIR);
            }
        }
    }

    /**
     * Places the door held by the player, sets the direction it's facing and
     * creates the top half of the door.
     */
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
                    ((Door) data).setData((byte) 6);
                    break;
                case WEST:
                    ((Door) data).setData((byte) 5);
                    break;
                case SOUTH:
                    ((Door) data).setData((byte) 4);
                    break;
                case EAST:
                    ((Door) data).setData((byte) 7);
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

    /**
     * Opens and closes the door when right-clicked by the player.
     */
    @Override
    public boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
        super.blockInteract(player, block, face, clickedLoc);

        if (block.getType() == Material.IRON_DOOR_BLOCK)
            return false;

        GlowBlockState state = block.getState();
        MaterialData data = state.getData();

        if (data instanceof Door) {
            Door door = (Door) data;
            if (door.isTopHalf()) {
                door = null;
                block = block.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ());
                if (block != null) {
                    state = block.getState();
                    data = state.getData();
                    if (data instanceof Door) {
                        door = (Door) data;
                    }
                }
            }

            if (door != null)
                door.setOpen(!door.isOpen());

            state.update(true);
        }

        return true;
    }
}
=======
     public boolean canPlaceAt(GlowBlock block, BlockFace against) {
          return (against == BlockFace.UP);
     }

     /**
      * Removes the adjacent door block to the door
      * 
      * I propose this method or similar is added to the BlockType class. Should
      * be called from the end of DiggingHandler:handle. Beds will also need
      * this implementation.
      * 
      * @Override
      */
     public void blockDestroy(GlowPlayer player, GlowBlock block, BlockFace face, Vector blockLoc) {
          GlowBlockState state = block.getState();
          MaterialData data = state.getData();

          if (data instanceof Door) {
               Door door = (Door) data;
               if (door.isTopHalf()) {
                    Block b = block.getRelative(BlockFace.DOWN);
                    if (b.getType() == block.getType())
                         b.setType(Material.AIR);
               } else {
                    Block b = block.getRelative(BlockFace.UP);
                    if (b.getType() == block.getType())
                         b.setType(Material.AIR);
               }
          }
     }

     /**
      * Places the door held by the player, sets the direction it's facing and
      * creates the top half of the door.
      */
     @Override
     public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
          super.placeBlock(player, state, face, holding, clickedLoc);

          MaterialData data = state.getData();
          if (data instanceof Door) {
               BlockFace facing = player.getDirection();
               ((Door) data).setFacingDirection(facing.getOppositeFace());

               // I have the feeling that my code that detects adjacent doors and flips the hinges, could have been done better. :(
               // START HINGE FLIPING

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
                         ((Door) data).setData((byte) 6);
                         break;
                    case WEST:
                         ((Door) data).setData((byte) 5);
                         break;
                    case SOUTH:
                         ((Door) data).setData((byte) 4);
                         break;
                    case EAST:
                         ((Door) data).setData((byte) 7);
                         break;
                    }
               }

               // END HINGE FLIPING

               GlowBlock topHalf = state.getBlock().getRelative(BlockFace.UP);

               topHalf.setType(state.getType());
               GlowBlockState topState = topHalf.getState();
               ((Door) topState.getData()).setTopHalf(true);
               topState.update();
          } else {
               warnMaterialData(Door.class, data);
          }
     }

     /**
      * Opens and closes the door when right-clicked by the player.
      */
     @Override
     public boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
          super.blockInteract(player, block, face, clickedLoc);

          if (block.getType() == Material.IRON_DOOR_BLOCK)
               return false;

          GlowBlockState state = block.getState();
          MaterialData data = state.getData();

          if (data instanceof Door) {
               Door door = (Door) data;
               if (door.isTopHalf()) {
                    door = null;
                    block = block.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ());
                    if (block != null) {
                         state = block.getState();
                         data = state.getData();
                         if (data instanceof Door) {
                              door = (Door) data;
                         }
                    }
               }

               if (door != null)
                    door.setOpen(!door.isOpen());

               state.update(true);
          }

          return true;
     }

     /*
      * Should provide redstone support to the doors, if and when the Redstone Framework is implemented. UNTESTED!
     @Override
     public void traceBlockPower(GlowBlock block, RSManager rsManager, Material srcMat, BlockFace flowDir, int inPower, boolean isDirect) {
          GlowBlock orginalBlock = block;
          GlowBlockState state = block.getState();
          MaterialData data = state.getData();
          if (data instanceof Door) {
               Door door = (Door) data;

               if (door.isTopHalf()) {
                    door = null;
                    block = block.getRelative(BlockFace.DOWN);
                    if (block != null) {
                         state = block.getState();
                         data = state.getData();
                         if (data instanceof Door) {
                              door = (Door) data;
                         }
                    }
               }

               if (door != null) {
                    int power = block.getBlockPower();
                    GlowBlock topBlock = block.getRelative(BlockFace.UP);
                    int topPower = (topBlock.getState().getData() instanceof Door) ? topBlock.getBlockPower() : 0;

                    if (topPower > power)
                         power = topPower;

                    int oldPower = ((block.getData() & 0x4) > 0) ? 15 : 0;

                    if (((oldPower == 0 ? 1 : 0) ^ (power == 0 ? 1 : 0)) != 0) {
                         BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(orginalBlock, oldPower, power);
                         EventFactory.callEvent(eventRedstone);

                         door.setOpen(eventRedstone.getNewCurrent() > 0);
                    }
               }
          } else {
               warnMaterialData(Door.class, data);
          }
     }
     */
}
>>>>>>> b6ad16a738facb7556b42eddfeece51ca84ecbe4
