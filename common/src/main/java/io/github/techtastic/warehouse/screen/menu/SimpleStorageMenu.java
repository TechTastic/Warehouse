package io.github.techtastic.warehouse.screen.menu;

import io.github.techtastic.warehouse.block.WarehouseBlocks;
import io.github.techtastic.warehouse.block.entity.SimpleStorageBlockEntity;
import io.github.techtastic.warehouse.screen.WarehouseMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SimpleStorageMenu extends AbstractContainerMenu {
    private final SimpleStorageBlockEntity storage;
    private final Level level;
    private final ContainerData data;

    public SimpleStorageMenu(int i, Inventory inv, FriendlyByteBuf extraData) {
        this(i, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public SimpleStorageMenu(int i, Inventory inv, BlockEntity entity, ContainerData data) {
        super(WarehouseMenuTypes.SIMPLE_STORAGE_MENU_TYPE.get(), i);
        checkContainerSize(inv, 5);
        this.storage = (SimpleStorageBlockEntity) entity;
        this.data = data;
        this.level = inv.player.getLevel();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        addDataSlots(data);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < 36) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, 36, 40, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index > 36) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, 0, 35, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, storage.getBlockPos()),
                pPlayer, WarehouseBlocks.SMALL_STORAGE_BLOCK.get()) ||
                stillValid(ContainerLevelAccess.create(level, storage.getBlockPos()),
                        pPlayer, WarehouseBlocks.MEDIUM_STORAGE_BLOCK.get()) ||
                stillValid(ContainerLevelAccess.create(level, storage.getBlockPos()),
                        pPlayer, WarehouseBlocks.LARGE_STORAGE_BLOCK.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }

    public boolean isFirstPage() {
        return this.data.get(1) == 0 && this.data.get(2) > 1;
    }

    public boolean isLastPage() {
        return this.data.get(1) == this.data.get(2) - 1 && this.data.get(2) > 1;
    }
}