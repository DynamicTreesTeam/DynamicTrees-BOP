package dynamictreesbop.cells;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.cells.CellNull;
import com.ferreusveritas.dynamictrees.api.cells.ICell;
import com.ferreusveritas.dynamictrees.api.cells.ICellKit;
import com.ferreusveritas.dynamictrees.api.cells.ICellSolver;
import com.ferreusveritas.dynamictrees.cells.CellKits.BasicSolver;
import com.ferreusveritas.dynamictrees.cells.CellNormal;
import com.ferreusveritas.dynamictrees.cells.LeafClusters;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;

import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.util.ResourceLocation;

public class CellKits {
	
	public static void init() {
		new CellKits();
	}
	
	public CellKits() {
		TreeRegistry.registerCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "sparse"), sparse);
		TreeRegistry.registerCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "poplar"), poplar);
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
	
}
