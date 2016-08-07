package korkorna.examples.transcoding.domain.job.mediasource;

import korkorna.examples.transcoding.domain.job.LocalStorageMediaSourceFile;
import korkorna.examples.transcoding.domain.job.MediaSourceFile;
import korkorna.examples.transcoding.domain.job.MediaSourceFileFactory;

public class DefaultMediaSourceFileFactory implements MediaSourceFileFactory {

	public MediaSourceFile create(String mediaSource) {
		// TODO Auto-generated method stub
		if (mediaSource.startsWith("file://")) {
			String filePath = mediaSource.substring("file://".length());
			return new LocalStorageMediaSourceFile(filePath);
		}
		
		throw new IllegalArgumentException("not supported media source: " 
				+ mediaSource);
	}

}
