package io.github.techtastic.warehouse.block.entity;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import io.github.techtastic.warehouse.block.WarehouseBlockEntities;
import io.github.techtastic.warehouse.screen.menu.SimpleStorageMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SimpleStorageBlockEntity extends RandomizableContainerBlockEntity implements ExtendedMenuProvider {
    private final NonNullList<ItemStack> inventory;
    private int page = 0;
    private final int pageCount;
    private final ContainerData data;

    public SimpleStorageBlockEntity(BlockPos pos, BlockState state, int size) {
        super(WarehouseBlockEntities.SIMPLE_STORAGE_BLOCK_ENTITY.get(), pos, state);
        this.inventory = NonNullList.createWithCapacity(size);
        this.pageCount = (int) Math.ceil((double) size / 27);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> size;
                    case 1 -> SimpleStorageBlockEntity.this.page;
                    case 2 -> SimpleStorageBlockEntity.this.pageCount;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int j) {
                if (i == 1)
                    SimpleStorageBlockEntity.this.page = j;
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
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
        return new SimpleStorageMenu(i, inventory, this, this.data);
    }

    @Override
    public int getContainerSize() {
        return this.inventory.size();
    }

    @Override
    public void saveExtraData(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.worldPosition);
    }
}
