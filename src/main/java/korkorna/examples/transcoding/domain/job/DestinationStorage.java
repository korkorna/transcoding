package korkorna.examples.transcoding.domain.job;

import java.io.File;
import java.util.List;

public abstract class DestinationStorage {

	private String url;
	
	public DestinationStorage(String url) {
		super();
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public abstract void save(List<File> multimediaFiles, List<File> thumnails);

}
