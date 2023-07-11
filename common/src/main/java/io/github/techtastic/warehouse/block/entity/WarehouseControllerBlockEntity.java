package io.github.techtastic.warehouse.block.entity;

import dev.architectury.networking.NetworkManager;
import io.github.techtastic.warehouse.block.WarehouseBlockEntities;
import io.github.techtastic.warehouse.network.WarehouseNetworking;
import io.github.techtastic.warehouse.network.packet.AABBSyncS2CPacket;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class WarehouseControllerBlockEntity extends BlockEntity {
    private AABB aabb;
    private List<ItemCount> counts;
    private int page = 0;
    private int pageCount = 0;
    private Sorting sort = Sorting.ALPHABET;
    private boolean ascendingSort = true;
    private boolean prevSort = true;
    private int ticks = 0;

    public WarehouseControllerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(WarehouseBlockEntities.WAREHOUSE_CONTROLLER_BLOCK_ENTITY.get(), blockPos, blockState);
        this.aabb = getDefaultOrientation(blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite());
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putDouble("Warehouse$maxX", this.aabb.maxX);
        compoundTag.putDouble("Warehouse$minX", this.aabb.minX);
        compoundTag.putDouble("Warehouse$maxY", this.aabb.maxY);
        compoundTag.putDouble("Warehouse$minY", this.aabb.minY);
        compoundTag.putDouble("Warehouse$maxZ", this.aabb.maxZ);
        compoundTag.putDouble("Warehouse$minZ", this.aabb.minZ);

        compoundTag.putString("Warehouse$sort", this.sort.toString());
        compoundTag.putBoolean("Warehouse$ascending", this.ascendingSort);

        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);

        this.aabb = new AABB(
                compoundTag.getDouble("Warehouse$minX"),
                compoundTag.getDouble("Warehouse$minY"),
                compoundTag.getDouble("Warehouse$minZ"),
                compoundTag.getDouble("Warehouse$maxX"),
                compoundTag.getDouble("Warehouse$maxY"),
                compoundTag.getDouble("Warehouse$maxZ")
        );

        this.sort = Sorting.valueOf(compoundTag.getString("Warehouse$sort"));
        this.ascendingSort = compoundTag.getBoolean("Warehouse$ascending");
    }

    @Override
    public void setChanged() {
        super.setChanged();

        assert level != null;
        if (level.isClientSide) return;

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        new AABBSyncS2CPacket(this.aabb, this.worldPosition).toBytes(buf);

        NetworkManager.sendToPlayers(level.getServer().getPlayerList().getPlayers(), WarehouseNetworking.AABB_SYNC_S2C_PACKET_ID, buf);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, WarehouseControllerBlockEntity entity) {
        if (level.isClientSide)
            return;

        if (entity.counts == null && entity.ticks != 0 && entity.ticks % 5 == 0) {
            List<SimpleStorageBlockEntity> storages = entity.getAllStorage(level, pos);
            entity.counts = entity.populateItemCounts(storages);

            entity.ticks = 0;
        }
        entity.ticks++;

        if (entity.counts != null) {
            switch (entity.sort) {
                case ALPHABET -> entity.sortCountsByName();
                case AMOUNT -> entity.sortCountsByAmount();
                case MOD -> entity.sortCountsByModId();
            }
        }

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        new AABBSyncS2CPacket(entity.getBoundingBox(), pos).toBytes(buf);

        NetworkManager.sendToPlayers(level.getServer().getPlayerList().getPlayers(), WarehouseNetworking.AABB_SYNC_S2C_PACKET_ID, buf);
    }

    public AABB getBoundingBox() {
        return this.aabb;
    }

    public void setBoundingBox(AABB aabb) {
        this.aabb = aabb;
        this.setChanged();
    }

    public static AABB moveInDirection(AABB aabb, Direction direction) {
        return switch (direction) {
            case DOWN -> aabb.move(0, -0.5, 0);
            case UP -> aabb.move(0, 0.5, 0);
            case NORTH -> aabb.move(0, 0, -0.5);
            case SOUTH -> aabb.move(0,0,0.5);
            case WEST -> aabb.move(-0.5, 0, 0);
            case EAST -> aabb.move(0.5, 0, 0);
        };
    }

    public static AABB getDefaultOrientation(Direction direction) {
        return switch (direction) {
            case SOUTH -> new AABB(-1, 0, 1, 2, 3, 4);
            case WEST -> new AABB(-3, 0, -1, 0, 3, 2);
            case EAST -> new AABB(1, 0, -1, 4, 3, 2);
            default -> new AABB(-1, 0, -3, 2, 3, 0);
        };
    }

    public List<SimpleStorageBlockEntity> getAllStorage(Level level, BlockPos controller) {
        List<SimpleStorageBlockEntity> storageBlocks = new ArrayList<>();

        AABB trueArea = this.aabb.move(controller.offset(
                this.aabb.minX, this.aabb.minY, this.aabb.minZ));

        // Grab all SimpleStorageBlockEntities in the area and their inventories
        for (double x = trueArea.minX; x <= trueArea.maxX; x++) {
            for (double y = trueArea.minY; y <= trueArea.maxY; y++) {
                for (double z = trueArea.minZ; z <= trueArea.maxZ; z++) {
                    BlockEntity be = level.getBlockEntity(new BlockPos(x, y, z));
                    if (be instanceof SimpleStorageBlockEntity storage)
                        storageBlocks.add(storage);
                }
            }
        }

        return storageBlocks;
    }

    public List<ItemCount> populateItemCounts(List<SimpleStorageBlockEntity> storages) {
        List<ItemCount> counts = new ArrayList<>();

        storages.forEach(storage -> {
            storage.getItems().forEach(stack -> {
                if (counts.isEmpty()) {
                    counts.add(new ItemCount(stack.getItem(), 0, storages));
                } else {
                    boolean hasCount = false;
                    for(ItemCount count : counts) {
                        if (count.item.equals(stack.getItem())) {
                            hasCount = true;
                            break;
                        }
                    }
                    if (!hasCount)
                        counts.add(new ItemCount(stack.getItem(), 0, storages));
                }
            });
        });

        return counts;
    }

    public void sortCountsByAmount() {
        this.counts.sort(this.ascendingSort ?
                Comparator.comparingInt(ItemCount::count) :
                Comparator.comparingInt(ItemCount::count).reversed());
    }

    public void sortCountsByName() {
        this.counts.sort(this.ascendingSort ?
                Comparator.comparing(ItemCount::getName) :
                Comparator.comparing(ItemCount::getName).reversed());
    }

    public void sortCountsByModId() {
        this.counts.sort(this.ascendingSort ?
                Comparator.comparing(ItemCount::getModId) :
                Comparator.comparing(ItemCount::getModId).reversed());
    }

    public record ItemCount(Item item, int count, List<SimpleStorageBlockEntity> inventoryMap) {
        @Override
        public int count() {
            AtomicInteger count = new AtomicInteger();

            getAllContainingItem().forEach(storage ->
                count.addAndGet(storage.countItem(item))
            );

            return count.get();
        }

        public List<SimpleStorageBlockEntity> getAllContainingItem() {
            List<SimpleStorageBlockEntity> filtered = new ArrayList<>();

            inventoryMap.forEach(storage -> {
                if (storage.countItem(item) > 0)
                    filtered.add(storage);
            });

            return filtered;
        }

        public ItemStack takeItem(int count) {
            ItemStack result = new ItemStack(item, 0);

            getAllContainingItem().forEach(storage -> {
                        if (result.getCount() == count)
                            return;

                        storage.getItems().forEach(stack -> {
                            if (!stack.is(item))
                                return;

                            int remaining = count - result.getCount();
                            if (stack.getCount() > remaining) {
                                stack.shrink(remaining);
                                result.grow(remaining);
                            } else if (stack.getCount() == remaining) {
                                storage.setItem(storage.getItems().indexOf(stack), ItemStack.EMPTY);
                                result.grow(remaining);
                            } else {
                                result.grow(stack.getCount());
                                storage.setItem(storage.getItems().indexOf(stack), ItemStack.EMPTY);
                            }
                        });
                    });

            return result;
        }

        public ItemStack addItem(ItemStack input) {
            AtomicBoolean hasRemaining = new AtomicBoolean(true);
            getAllContainingItem().forEach(storage -> storage.getItems().forEach(stack -> {
                if (stack.is(item) && stack.getCount() < stack.getMaxStackSize()) {
                    int space = stack.getCount() - stack.getMaxStackSize();
                    input.shrink(space);
                    stack.grow(space);
                } else if (stack.isEmpty()) {
                    storage.setItem(storage.getItems().indexOf(stack), input);
                    hasRemaining.set(false);
                }
            }));

            return hasRemaining.get() ? input : ItemStack.EMPTY;
        }

        public String getName() {
            return item.getName(new ItemStack(item)).getString();
        }

        public String getModId() {
            return Registry.ITEM.getKey(item).getNamespace();
        }
    }

    public record Pages(int index, List<ItemCount> counts) {

    }

    public enum Sorting {
        ALPHABET,
        AMOUNT,
        MOD
    }
}
