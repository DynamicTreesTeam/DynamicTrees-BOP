package dynamictreesbop.cells;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.cells.CellNull;
import com.ferreusveritas.dynamictrees.api.cells.ICell;
import com.ferreusveritas.dynamictrees.api.cells.ICellKit;
import com.ferreusveritas.dynamictrees.api.cells.ICellSolver;
import com.ferreusveritas.dynamictrees.cells.CellNormal;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;

import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class CellKits {
	
	public static void init() {
		new CellKits();
	}
	
	public CellKits() {
		TreeRegistry.registerCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "sparse"), sparse);
		TreeRegistry.registerCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "poplar"), poplar);
		TreeRegistry.registerCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "mahogany"), mahogany);
		TreeRegistry.registerCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "brush"), brush);
		TreeRegistry.registerCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "eucalyptus"), eucalyptus);
	}
	
	private final ICellKit sparse = new ICellKit() {
		
		private final ICell sparseBranch = new CellSparseBranch();
		private final ICell sparseLeaves = new CellNormal(1);
		
		private final ICellSolver solver = new com.ferreusveritas.dynamictrees.cells.CellKits.BasicSolver(new short[] {0x0211});
		
		@Override
		public ICell getCellForLeaves(int hydro) {
			return hydro > 0 ? sparseLeaves : CellNull.NULLCELL;
		}
		
		@Override
		public ICell getCellForBranch(int radius) {
			return radius == 1 || radius == 128 ? sparseBranch : CellNull.NULLCELL;
		}
		
		@Override
		public SimpleVoxmap getLeafCluster() {
			return DTBOPLeafClusters.sparse;
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
	
	private final ICellKit poplar = new ICellKit() {
		
		private final ICell poplarBranch = new CellPoplarBranch();
		private final ICell poplarTopBranch = new CellPoplarTopBranch();
		private final ICell poplarUpperTrunk = new CellNormal(4);
		
		private final ICell[] poplarLeaves = new ICell[] {
				CellNull.NULLCELL,
				new CellPoplarLeaf(1),
				new CellPoplarLeaf(2),
				new CellPoplarLeaf(3),
				new CellPoplarLeaf(4),
		};
		
		private final ICellSolver solver = new com.ferreusveritas.dynamictrees.cells.CellKits.BasicSolver(new short[] {
				0x0412, 0x0311, 0x0211
		});
		
		@Override
		public ICell getCellForLeaves(int hydro) {
			return poplarLeaves[hydro];
		}
		
		@Override
		public ICell getCellForBranch(int radius) {
			if (radius == 128) return poplarTopBranch;
			if (radius == 1) return poplarBranch;
			if (radius < 4) return poplarUpperTrunk;
			return CellNull.NULLCELL;
		}
		
		@Override
		public SimpleVoxmap getLeafCluster() {
			return DTBOPLeafClusters.poplar;
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
	
	private final ICellKit mahogany = new ICellKit() {
		
		private final ICell mahoganyBranch = new CellMahoganyBranch();
		
		private final ICell mahoganyLeafCells[] = {
				CellNull.NULLCELL,
				new CellMahoganyLeaf(1),
				new CellMahoganyLeaf(2),
				new CellMahoganyLeaf(3),
				new CellMahoganyLeaf(4)
		}; 
		
		private final ICellSolver solver = new com.ferreusveritas.dynamictrees.cells.CellKits.BasicSolver(new short[] {
				0x0513, 0x0413, 0x0322, 0x0311, 0x0211
		});
		
		@Override
		public ICell getCellForLeaves(int hydro) {
			return mahoganyLeafCells[hydro];
		}
		
		@Override
		public ICell getCellForBranch(int radius) {
			if (radius == 1 || radius == 128) return mahoganyBranch;
			return CellNull.NULLCELL;
		}
		
		@Override
		public SimpleVoxmap getLeafCluster() {
			return DTBOPLeafClusters.mahogany;
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
	
	private final ICellKit brush = new ICellKit() {
		
		private final ICell branch = new ICell() {
			@Override
			public int getValue() {
				return 5;
			}
			
			final int map[] = {3, 3, 5, 5, 5, 5};
			
			@Override
			public int getValueFromSide(EnumFacing side) {
				return map[side.ordinal()];
			}
		};
		
		private final ICell normalCells[] = {
				CellNull.NULLCELL,
				new CellNormal(1),
				new CellNormal(2),
				new CellNormal(3),
				new CellNormal(4),
		};
		
		private final ICellSolver solver = new com.ferreusveritas.dynamictrees.cells.CellKits.BasicSolver(new short[] {
				0x0513, 0x0412, 0x0322, 0x0311, 0x0211,
		});
		
		@Override
		public ICell getCellForLeaves(int hydro) {
			return normalCells[hydro];
		}
		
		@Override
		public ICell getCellForBranch(int radius) {
			if (radius == 1 || radius == 128) return branch;
			return CellNull.NULLCELL;
		}
		
		@Override
		public SimpleVoxmap getLeafCluster() {
			return DTBOPLeafClusters.brush;
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
	
	private final ICellKit eucalyptus = new ICellKit() {
		
		private final ICell eucalyptusTopBranch = new CellEucalyptusTopBranch();
		private final ICell eucalyptusBranch = new CellNormal(2);
		private final ICell eucalyptusUpperTrunk = new CellNormal(3);
		
		private final ICell[] eucalyptusLeaves = new ICell[] {
				CellNull.NULLCELL,
				new CellEucalyptusLeaf(1),
				new CellEucalyptusLeaf(2),
				new CellEucalyptusLeaf(3),
				new CellEucalyptusLeaf(4),
		};
		
		private final ICellSolver solver = new com.ferreusveritas.dynamictrees.cells.CellKits.BasicSolver(new short[] {
				0x0514, 0x0423, 0x0411, 0x0312, 0x0211
		});
		
		@Override
		public ICell getCellForLeaves(int hydro) {
			return eucalyptusLeaves[hydro];
		}
		
		@Override
		public ICell getCellForBranch(int radius) {
			if (radius == 128) return eucalyptusTopBranch;
			if (radius == 1) return eucalyptusBranch;
			if (radius <= 3) return eucalyptusUpperTrunk;
			return CellNull.NULLCELL;
		}
		
		@Override
		public SimpleVoxmap getLeafCluster() {
			return DTBOPLeafClusters.eucalyptus;
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
