////////////////////////////////////////////////////////
// AnalysisDialogBatchProcess.java
// Written by Bill Duren
// September 2019
////////////////////////////////////////////////////////////

package edu.umich.wld;

public class SharedAnalysisSettings {
	private boolean secondLatticeColIsDest = true;
	private boolean readFileOrder = false;
	
	public SharedAnalysisSettings() {	
	}
	
	public boolean isSecondLatticeColIsDest() {
		return secondLatticeColIsDest;
	}

	public void setSecondLatticeColIsDest(boolean secondLatticeColIsDest) {
		this.secondLatticeColIsDest = secondLatticeColIsDest;
	}

	public boolean isReadFileOrder() {
		return readFileOrder;
	}

	public void setReadFileOrder(boolean readFileOrder) {
		this.readFileOrder = readFileOrder;
	}
}
