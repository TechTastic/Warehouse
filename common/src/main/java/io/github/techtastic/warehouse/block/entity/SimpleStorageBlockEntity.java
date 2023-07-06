package io.github.techtastic.warehouse.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SimpleStorageBlockEntity extends RandomizableContainerBlockEntity {
    private final NonNullList<ItemStack> inventory;

    public SimpleStorageBlockEntity(BlockPos pos, BlockState state, int size) {
        super(, pos, state);
        this.inventory = NonNullList.createWithCapacity(size);
    }

    public SimpleStorageBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, 0);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        for (ItemStack stack : nonNullList) {
            this.inventory.set(nonNullList.indexOf(stack), stack);
        }
    }

    @Override
    protected Component getDefaultName() {
        return this.getBlockState().getBlock().getName();
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return this.inventory.size();
    }
}
