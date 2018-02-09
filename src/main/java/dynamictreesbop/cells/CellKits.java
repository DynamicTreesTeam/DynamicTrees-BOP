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
		TreeRegistry.registerCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "bare"), bare);
		TreeRegistry.registerCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "sparse"), sparse);
		TreeRegistry.registerCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "deciduous"), deciduous);
	}
	
	private final ICellKit bare = new ICellKit() {
		
		private final ICellSolver solver = new ICellSolver() {
			public int solve(ICell[] cells) {
				return 0;
			}
		};
		
		@Override
		public ICell getCellForLeaves(int hydro) {
			return CellNull.NULLCELL;
		}
		
		@Override
		public ICell getCellForBranch(int radius) {
			return CellNull.NULLCELL;
		}
		
		@Override
		public SimpleVoxmap getLeafCluster() {
			return DTBOPLeafClusters.bare;
		}
		
		@Override
		public ICellSolver getCellSolver() {
			return solver;
		}
		
		@Override
		public int getDefaultHydration() {
			return 0;
		}
		
	};
	
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
			return radius == 1 ? sparseBranch : CellNull.NULLCELL;
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
	
	private final ICellKit deciduous = new ICellKit() {
		
		private final ICell normalCells[] = {
				CellNull.NULLCELL,
				new CellNormal(1),
				new CellNormal(2),
				new CellNormal(3),
				new CellNormal(4),
				new CellNormal(5),
				new CellNormal(6),
				new CellNormal(7)
		};
		
		/** Typical branch with hydration 5 */
		private final ICell branchCell = new CellNormal(5);
		
		private final BasicSolver deciduousSolver = new BasicSolver(new short[]{0x0514, 0x0423, 0x0322, 0x0411, 0x0311, 0x0211});
		
		@Override
		public ICell getCellForLeaves(int hydro) {
			return normalCells[hydro];
		}
		
		@Override
		public ICell getCellForBranch(int radius) {
			return radius == 1 || radius == 128 ? branchCell : CellNull.NULLCELL;
		}
		
		@Override
		public SimpleVoxmap getLeafCluster() {
			return LeafClusters.deciduous;
		}
		
		@Override
		public ICellSolver getCellSolver() {
			return deciduousSolver;
		}
		
		@Override
		public int getDefaultHydration() {
			return 4;
		}
		
	};
	
}
