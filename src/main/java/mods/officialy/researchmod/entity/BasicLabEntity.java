package mods.officialy.researchmod.entity;

import mods.officialy.researchmod.Constants;
import mods.officialy.researchmod.ResearchMod;
import mods.officialy.researchmod.capability.lab.LabCapability;
import mods.officialy.researchmod.capability.lab.LabCapabilityImpl;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BasicLabEntity extends BlockEntity {

    private final LabCapability backend = new LabCapabilityImpl();
    private final LazyOptional<LabCapability> optionalData = LazyOptional.of(() -> backend);

    public BasicLabEntity(BlockPos pPos, BlockState pBlockState) {
        super(ResearchMod.LAB_TYPE.get(), pPos, pBlockState);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap.isRegistered() && cap == Constants.INSTANCE) {
            Constants.INSTANCE.orEmpty(cap, optionalData);
        }
        return super.getCapability(cap,side);
    }

    public static void tickLab(Level pLevel, BlockPos pPos, BlockState pState, BasicLabEntity pBlockEntity) {
//        System.out.println(pBlockEntity.optionalData.orElseThrow(RuntimeException::new).getString());
    }
}
