package korkorna.examples.transcoding.domain.job;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class MediaSourceFileFactoryDefaultTtest {
	
	private MediaSourceFileFactory factory;

	@Before
	public void setup() {
		factory = MediaSourceFileFactory.DEFAULT;
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
