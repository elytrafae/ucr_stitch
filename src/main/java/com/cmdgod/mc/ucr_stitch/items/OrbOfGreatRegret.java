package com.cmdgod.mc.ucr_stitch.items;

public class OrbOfGreatRegret extends TooltippedItem {

    public OrbOfGreatRegret(Settings settings) {
        super(settings);
    }

    /*
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.consume(user.getStackInHand(hand));
        }
        world.getServer().getCommandManager().executeWithPrefix(user.getCommandSource().withMaxLevel(2).withOutput(CommandOutput.DUMMY), "function ucr_stitch:skill_point_reset/start");
        user.getStackInHand(hand).decrement(1);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }
    */
    
}
