package korkorna.examples.transcoding.domain.job;

import java.io.File;
import java.util.List;

public class FileDestinationStorage implements DestinationStorage{

	private String directory;
	
	public FileDestinationStorage(String directory) {
		super();
		this.directory = directory;
	}

	public void save(List<File> multimediaFiles, List<File> thumnails) {
		// TODO Auto-generated method stub
		
	}

}
