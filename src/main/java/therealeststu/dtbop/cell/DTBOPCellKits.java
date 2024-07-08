package therealeststu.dtbop.cell;

import com.ferreusveritas.dynamictrees.api.cell.Cell;
import com.ferreusveritas.dynamictrees.api.cell.CellKit;
import com.ferreusveritas.dynamictrees.api.cell.CellNull;
import com.ferreusveritas.dynamictrees.api.cell.CellSolver;
import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.cell.CellKits;
import com.ferreusveritas.dynamictrees.cell.MetadataCell;
import com.ferreusveritas.dynamictrees.cell.NormalCell;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import therealeststu.dtbop.DynamicTreesBOP;

public class DTBOPCellKits {

    public static class SparseCellKit extends CellKit {
        protected final Cell sparseBranch = new SparseBranchCell();
        protected final Cell sparseLeaves = new NormalCell(1);

        protected final CellSolver solver = new CellKits.BasicSolver(new short[]{0x0211});

        public SparseCellKit(ResourceLocation registryName) {
            super(registryName);
        }

        @Override
        public Cell getCellForLeaves(int hydro) {
            return hydro > 0 ? sparseLeaves : CellNull.NULL_CELL;
        }

        @Override
        public Cell getCellForBranch(int radius, int meta) {
            return radius == 1 ? sparseBranch : CellNull.NULL_CELL;
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return DTBOPLeafClusters.SPARSE;
        }

        @Override
        public CellSolver getCellSolver() {
            return solver;
        }

        @Override
        public int getDefaultHydration() {
            return 1;
        }
    }

    public static final CellKit SPARSE = new SparseCellKit(new ResourceLocation(DynamicTreesBOP.MOD_ID, "sparse"));
    public static final CellKit HELLBARK_SPARSE = new SparseCellKit(new ResourceLocation(DynamicTreesBOP.MOD_ID, "hellbark_sparse")) {
        @Override
        public Cell getCellForBranch(int radius, int meta) {
            return radius <= 3 ? sparseBranch : CellNull.NULL_CELL;
        }
    };

    public static final CellKit POPLAR = new CellKit(new ResourceLocation(DynamicTreesBOP.MOD_ID, "poplar")) {

        private final Cell poplarBranch = new PoplarBranchCell();
        private final Cell poplarTopBranch = new PoplarTopBranchCell();
        private final Cell poplarUpperTrunk = new NormalCell(4);

        private final Cell[] poplarLeaves = new Cell[]{
                CellNull.NULL_CELL,
                new PoplarLeafCell(1),
                new PoplarLeafCell(2),
                new PoplarLeafCell(3),
                new PoplarLeafCell(4),
                new PoplarLeafCell(5),
                new PoplarLeafCell(6),
                new PoplarLeafCell(7),
        };

        private final CellSolver solver = new CellKits.BasicSolver(new short[]{
                0x0412, 0x0311, 0x0211
        });

        @Override
        public Cell getCellForLeaves(int hydro) {
            return poplarLeaves[hydro];
        }

        @Override
        public Cell getCellForBranch(int radius, int meta) {
            if (meta == MetadataCell.TOP_BRANCH) return poplarTopBranch;
            if (radius == 1) return poplarBranch;
            if (radius < 4) return poplarUpperTrunk;
            return CellNull.NULL_CELL;
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return DTBOPLeafClusters.POPLAR;
        }

        @Override
        public CellSolver getCellSolver() {
            return solver;
        }

        @Override
        public int getDefaultHydration() {
            return 4;
        }

    };

    public static final CellKit MAHOGANY = new CellKit(new ResourceLocation(DynamicTreesBOP.MOD_ID, "mahogany")) {

        private final Cell mahoganyBranch = new MahoganyBranchCell();

        private final Cell[] mahoganyLeafCells = {
                CellNull.NULL_CELL,
                new MahoganyLeafCell(1),
                new MahoganyLeafCell(2),
                new MahoganyLeafCell(3),
                new MahoganyLeafCell(4),
                new MahoganyLeafCell(5),
                new MahoganyLeafCell(6),
                new MahoganyLeafCell(7),
        };

        private final CellSolver solver = new CellKits.BasicSolver(new short[]{
                0x0513, 0x0413, 0x0322, 0x0311, 0x0211
        });

        @Override
        public Cell getCellForLeaves(int hydro) {
            return mahoganyLeafCells[hydro];
        }

        @Override
        public Cell getCellForBranch(int radius, int meta) {
            if (radius == 1) return mahoganyBranch;
            return CellNull.NULL_CELL;
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return DTBOPLeafClusters.MAHOGANY;
        }

        @Override
        public CellSolver getCellSolver() {
            return solver;
        }

        @Override
        public int getDefaultHydration() {
            return 3;
        }

    };

    public static final CellKit BRUSH = new CellKit(new ResourceLocation(DynamicTreesBOP.MOD_ID, "brush")) {

        private final Cell branch = new Cell() {
            @Override
            public int getValue() {
                return 5;
            }

            final int[] map = {3, 3, 5, 5, 5, 5};

            @Override
            public int getValueFromSide(Direction side) {
                return map[side.ordinal()];
            }
        };

        private final Cell[] normalCells = {
                CellNull.NULL_CELL,
                new NormalCell(1),
                new NormalCell(2),
                new NormalCell(3),
                new NormalCell(4),
                new NormalCell(5),
                new NormalCell(6),
                new NormalCell(7),
        };

        private final CellSolver solver = new CellKits.BasicSolver(new short[]{
                0x0513, 0x0412, 0x0322, 0x0311, 0x0211,
        });

        @Override
        public Cell getCellForLeaves(int hydro) {
            return normalCells[hydro];
        }

        @Override
        public Cell getCellForBranch(int radius, int meta) {
            if (radius == 1) return branch;
            return CellNull.NULL_CELL;
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return DTBOPLeafClusters.BRUSH;
        }

        @Override
        public CellSolver getCellSolver() {
            return solver;
        }

        @Override
        public int getDefaultHydration() {
            return 3;
        }

    };

    public static final CellKit EUCALYPTUS = new CellKit(new ResourceLocation(DynamicTreesBOP.MOD_ID, "eucalyptus")) {

        private final Cell eucalyptusTopBranch = new EucalyptusTopBranchCell();
        private final Cell eucalyptusBranch = new NormalCell(2);
        private final Cell eucalyptusUpperTrunk = new NormalCell(3);

        private final Cell[] eucalyptusLeaves = new Cell[]{
                CellNull.NULL_CELL,
                new EucalyptusLeafCell(1),
                new EucalyptusLeafCell(2),
                new EucalyptusLeafCell(3),
                new EucalyptusLeafCell(4),
                new EucalyptusLeafCell(5),
                new EucalyptusLeafCell(6),
                new EucalyptusLeafCell(7),
        };

        private final CellSolver solver = new CellKits.BasicSolver(new short[]{
                0x0514, 0x0423, 0x0411, 0x0312, 0x0211
        });

        @Override
        public Cell getCellForLeaves(int hydro) {
            return eucalyptusLeaves[hydro];
        }

        @Override
        public Cell getCellForBranch(int radius, int meta) {
            if (meta == MetadataCell.TOP_BRANCH) return eucalyptusTopBranch;
            if (radius == 1) return eucalyptusBranch;
            if (radius <= 3) return eucalyptusUpperTrunk;
            return CellNull.NULL_CELL;
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return DTBOPLeafClusters.EUCALYPTUS;
        }

        @Override
        public CellSolver getCellSolver() {
            return solver;
        }

        @Override
        public int getDefaultHydration() {
            return 4;
        }

    };

    public static final CellKit HELLBARK = new CellKit(new ResourceLocation(DynamicTreesBOP.MOD_ID, "hellbark")) {

        private final Cell hellbarkBranch = new Cell() {
            @Override
            public int getValue() {
                return 5;
            }

            final int[] map = {0, 7, 5, 5, 5, 5};

            @Override
            public int getValueFromSide(Direction side) {
                return map[side.ordinal()];
            }

        };

        private final Cell[] hellbarkLeafCells = {
                CellNull.NULL_CELL,
                new HellbarkLeafCell(1),
                new HellbarkLeafCell(2),
                new HellbarkLeafCell(3),
                new HellbarkLeafCell(4),
                new HellbarkLeafCell(5),
                new HellbarkLeafCell(6),
                new HellbarkLeafCell(7)
        };

        private final CellKits.BasicSolver hellbarkSolver = new CellKits.BasicSolver(new short[]{0x0716, 0x0615, 0x0514, 0x0423, 0x0312, 0x0221});

        @Override
        public Cell getCellForLeaves(int hydro) {
            return hellbarkLeafCells[hydro];
        }

        @Override
        public Cell getCellForBranch(int radius, int meta) {
            return radius <= 4 ? hellbarkBranch : CellNull.NULL_CELL;
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return DTBOPLeafClusters.HELLBARK;
        }

        @Override
        public CellSolver getCellSolver() {
            return hellbarkSolver;
        }

        @Override
        public int getDefaultHydration() {
            return 6;
        }

    };

    public static void register(final Registry<CellKit> registry) {
        registry.registerAll(SPARSE, POPLAR, MAHOGANY, BRUSH, EUCALYPTUS, HELLBARK, HELLBARK_SPARSE);
    }

}
