package therealeststu.dtbop.cells;

import com.ferreusveritas.dynamictrees.api.cells.CellKit;
import com.ferreusveritas.dynamictrees.api.cells.CellNull;
import com.ferreusveritas.dynamictrees.api.cells.ICell;
import com.ferreusveritas.dynamictrees.api.cells.ICellSolver;
import com.ferreusveritas.dynamictrees.cells.CellKits;
import com.ferreusveritas.dynamictrees.cells.MetadataCell;
import com.ferreusveritas.dynamictrees.cells.NormalCell;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import therealeststu.dtbop.DynamicTreesBOP;
import therealeststu.dtbop.cells.cell.*;

public class DTBOPCellKits {

    public static final CellKit SPARSE = new CellKit(new ResourceLocation(DynamicTreesBOP.MOD_ID, "sparse")) {

        private final ICell sparseBranch = new SparseBranchCell();
        private final ICell sparseLeaves = new NormalCell(1);

        private final ICellSolver solver = new com.ferreusveritas.dynamictrees.cells.CellKits.BasicSolver(new short[] {0x0211});

        @Override
        public ICell getCellForLeaves(int hydro) {
            return hydro > 0 ? sparseLeaves : CellNull.NULL_CELL;
        }

        @Override
        public ICell getCellForBranch(int radius, int meta) {
            return radius == 1 ? sparseBranch : CellNull.NULL_CELL;
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return DTBOPLeafClusters.SPARSE;
        }

        @Override
        public ICellSolver getCellSolver() {
            return solver;
        }

        @Override
        public int getDefaultHydration() {
            return 1;
        }
    };

    public static final CellKit POPLAR = new CellKit(new ResourceLocation(DynamicTreesBOP.MOD_ID, "poplar")) {

        private final ICell poplarBranch = new PoplarBranchCell();
        private final ICell poplarTopBranch = new PoplarTopBranchCell();
        private final ICell poplarUpperTrunk = new NormalCell(4);

        private final ICell[] poplarLeaves = new ICell[] {
                CellNull.NULL_CELL,
                new PoplarLeafCell(1),
                new PoplarLeafCell(2),
                new PoplarLeafCell(3),
                new PoplarLeafCell(4),
        };

        private final ICellSolver solver = new CellKits.BasicSolver(new short[] {
                0x0412, 0x0311, 0x0211
        });

        @Override
        public ICell getCellForLeaves(int hydro) {
            return poplarLeaves[hydro];
        }

        @Override
        public ICell getCellForBranch(int radius, int meta) {
            if (meta == MetadataCell.CONIFERTOP) return poplarTopBranch;
            if (radius == 1) return poplarBranch;
            if (radius < 4) return poplarUpperTrunk;
            return CellNull.NULL_CELL;
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return DTBOPLeafClusters.POPLAR;
        }

        @Override
        public ICellSolver getCellSolver() {
            return solver;
        }

        @Override
        public int getDefaultHydration() {
            return 4;
        }

    };

    public static final CellKit MAHOGANY = new CellKit(new ResourceLocation(DynamicTreesBOP.MOD_ID, "mahogany")) {

        private final ICell mahoganyBranch = new MahoganyBranchCell();

        private final ICell[] mahoganyLeafCells = {
                CellNull.NULL_CELL,
                new MahoganyLeafCell(1),
                new MahoganyLeafCell(2),
                new MahoganyLeafCell(3),
                new MahoganyLeafCell(4)
        };

        private final ICellSolver solver = new com.ferreusveritas.dynamictrees.cells.CellKits.BasicSolver(new short[] {
                0x0513, 0x0413, 0x0322, 0x0311, 0x0211
        });

        @Override
        public ICell getCellForLeaves(int hydro) {
            return mahoganyLeafCells[hydro];
        }

        @Override
        public ICell getCellForBranch(int radius, int meta) {
            if (radius == 1) return mahoganyBranch;
            return CellNull.NULL_CELL;
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return DTBOPLeafClusters.MAHOGANY;
        }

        @Override
        public ICellSolver getCellSolver() {
            return solver;
        }

        @Override
        public int getDefaultHydration() {
            return 3;
        }

    };

    public static final CellKit BRUSH = new CellKit(new ResourceLocation(DynamicTreesBOP.MOD_ID, "brush")) {

        private final ICell branch = new ICell() {
            @Override public int getValue() { return 5; }
            final int[] map = {3, 3, 5, 5, 5, 5};
            @Override public int getValueFromSide(Direction side) { return map[side.ordinal()]; }
        };

        private final ICell[] normalCells = {
                CellNull.NULL_CELL,
                new NormalCell(1),
                new NormalCell(2),
                new NormalCell(3),
                new NormalCell(4),
        };

        private final ICellSolver solver = new com.ferreusveritas.dynamictrees.cells.CellKits.BasicSolver(new short[] {
                0x0513, 0x0412, 0x0322, 0x0311, 0x0211,
        });

        @Override
        public ICell getCellForLeaves(int hydro) {
            return normalCells[hydro];
        }

        @Override
        public ICell getCellForBranch(int radius, int meta) {
            if (radius == 1) return branch;
            return CellNull.NULL_CELL;
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return DTBOPLeafClusters.BRUSH;
        }

        @Override
        public ICellSolver getCellSolver() {
            return solver;
        }

        @Override
        public int getDefaultHydration() {
            return 3;
        }

    };

    public static final CellKit EUCALYPTUS = new CellKit(new ResourceLocation(DynamicTreesBOP.MOD_ID, "eucalyptus")) {

        private final ICell eucalyptusTopBranch = new EucalyptusTopBranchCell();
        private final ICell eucalyptusBranch = new NormalCell(2);
        private final ICell eucalyptusUpperTrunk = new NormalCell(3);

        private final ICell[] eucalyptusLeaves = new ICell[] {
                CellNull.NULL_CELL,
                new EucalyptusLeafCell(1),
                new EucalyptusLeafCell(2),
                new EucalyptusLeafCell(3),
                new EucalyptusLeafCell(4),
        };

        private final ICellSolver solver = new com.ferreusveritas.dynamictrees.cells.CellKits.BasicSolver(new short[] {
                0x0514, 0x0423, 0x0411, 0x0312, 0x0211
        });

        @Override
        public ICell getCellForLeaves(int hydro) {
            return eucalyptusLeaves[hydro];
        }

        @Override
        public ICell getCellForBranch(int radius, int meta) {
            if (meta == MetadataCell.CONIFERTOP) return eucalyptusTopBranch;
            if (radius == 1) return eucalyptusBranch;
            if (radius <= 3) return eucalyptusUpperTrunk;
            return CellNull.NULL_CELL;
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return DTBOPLeafClusters.EUCALYPTUS;
        }

        @Override
        public ICellSolver getCellSolver() {
            return solver;
        }

        @Override
        public int getDefaultHydration() {
            return 4;
        }

    };

}
