package korkorna.examples.transcoding.domain.job.mediasource;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import korkorna.examples.transcoding.domain.job.LocalStorageMediaSourceFile;
import korkorna.examples.transcoding.domain.job.MediaSourceFile;
import korkorna.examples.transcoding.domain.job.MediaSourceFileFactory;
import korkorna.examples.transcoding.domain.job.mediasource.DefaultMediaSourceFileFactory;

public class MediaSourceFileFactoryDefaultTtest {
	
	private MediaSourceFileFactory factory;

	@Before
	public void setup() {
		factory = new DefaultMediaSourceFileFactory();
	}
	
	@Test
	public void createLocalStroageMediaSourceFile() {
		MediaSourceFile mediaSourceFile = factory
				.create("file://./src/test/resources/sample.avi");
		
		assertTrue(mediaSourceFile instanceof LocalStorageMediaSourceFile);
		assertTrue(mediaSourceFile.getSourceFile().exists());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void createNotSupportedSource() {
		MediaSourceFile mediaSourceFile = factory
				.create("ftp://tmp/resources/sample.avi");
		fail("must throw exception");
	}
}
