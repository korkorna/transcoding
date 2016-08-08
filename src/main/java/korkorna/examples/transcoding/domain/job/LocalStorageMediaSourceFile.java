package korkorna.examples.transcoding.domain.job;

import java.io.File;

public class LocalStorageMediaSourceFile extends MediaSourceFile{

	public LocalStorageMediaSourceFile(String url) {
		super(url);
	}

	public File getSourceFile() {
		// TODO Auto-generated method stub
		return new File(getUrl());
	}

}
