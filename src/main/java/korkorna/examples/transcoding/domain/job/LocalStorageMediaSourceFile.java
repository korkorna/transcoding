package korkorna.examples.transcoding.domain.job;

import java.io.File;

public class LocalStorageMediaSourceFile implements MediaSourceFile{

	private String filePath;
	
	public LocalStorageMediaSourceFile(String filePath) {
		super();
		this.filePath = filePath;
	}

	public File getSourceFile() {
		// TODO Auto-generated method stub
		return new File(filePath);
	}

}
