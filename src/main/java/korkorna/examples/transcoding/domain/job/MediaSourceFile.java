package korkorna.examples.transcoding.domain.job;

import java.io.File;

public abstract class MediaSourceFile {

	private String url;
	
	public MediaSourceFile(String url) {
		super();
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public abstract File getSourceFile();
}
