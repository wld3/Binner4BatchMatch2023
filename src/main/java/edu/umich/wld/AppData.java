package edu.umich.wld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppData {

	private List<FileData> expFileDataStore = new ArrayList<FileData>(Arrays.asList(new FileData()));
	private FileData expFileData = new FileData();
	private List<FileData> libFileDataStore = new ArrayList<FileData>(Arrays.asList(new FileData()));
	private FileData libFileData = new FileData();
	private List<FileData> annotFileDataStore = new ArrayList<FileData>(Arrays.asList(new FileData()));
	private FileData annotFileData = new FileData();
	
	public List<FileData> getExpFileDataStore() {
		return expFileDataStore;
	}
	
	public void setExpFileDataStore(List<FileData> fileDataStore) {
		this.expFileDataStore = fileDataStore;
	}
	
	public FileData getExpFileData() {
		return expFileData;
	}
	
	public void setExpFileData(FileData fileData) {
		this.expFileData = fileData;
	}
	
	public List<FileData> getLibFileDataStore() {
		return libFileDataStore;
	}
	
	public void setLibFileDataStore(List<FileData> fileDataStore) {
		this.libFileDataStore = fileDataStore;
	}
	
	public FileData getLibFileData() {
		return libFileData;
	}
	
	public void setLibFileData(FileData fileData) {
		this.libFileData = fileData;
	}
	
	public List<FileData> getAnnotFileDataStore() {
		return annotFileDataStore;
	}
	
	public void setAnnotFileDataStore(List<FileData> fileDataStore) {
		this.annotFileDataStore = fileDataStore;
	}
	
	public FileData getAnnotFileData() {
		return annotFileData;
	}
	
	public void setAnnotFileData(FileData fileData) {
		this.annotFileData = fileData;
	}
}
