package net.rose.steamporium.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.rose.steamporium.api.util.ItemUtil;
import net.rose.steamporium.common.entity.SteambombEntity;

public class EnduriumSteambombItem extends Item {
    public EnduriumSteambombItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName() {
        return ((MutableText) super.getName()).styled(style -> style.withColor(0xFEEEBA));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        final var stack = user.getStackInHand(hand);

        if (user.isSneaking()) {
            return TypedActionResult.pass(stack);
        }

        if (world instanceof ServerWorld serverWorld) {
            final var bombEntity = new SteambombEntity(serverWorld, user);
            bombEntity.setItem(stack);
            bombEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0, 1.2F + (float)(user.getVelocity().length() * 0.1d), 0);
            serverWorld.spawnEntity(bombEntity);
        }

        ItemUtil.decrementUnlessCreative(stack, 1, user);

        return TypedActionResult.success(stack);
    }
}
