package edu.umich.wld;

public class ClusterForChecking {
	private Integer width = null;
	private Integer iFirstRow = null;
	private Integer iLastRow = null;
	
	public ClusterForChecking(Integer width) {
		this.setWidth(width);
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getIFirstRow() {
		return iFirstRow;
	}

	public void setIFirstRow(Integer iFirstRow) {
		this.iFirstRow = iFirstRow;
	}

	public Integer getILastRow() {
		return iLastRow;
	}

	public void setILastRow(Integer iLastRow) {
		this.iLastRow = iLastRow;
	}
}
