package korkorna.examples.transcoding.domain.job.destinationstorage;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import korkorna.examples.transcoding.domain.job.DestinationStorage;
import korkorna.examples.transcoding.domain.job.DestinationStorageFactory;
import korkorna.examples.transcoding.domain.job.FileDestinationStorage;
import korkorna.examples.transcoding.domain.job.destinationstorage.DefaultDestinationStorageFactory;

public class DestinationStorageFactoryDefaultTest {

	private DestinationStorageFactory factory = new DefaultDestinationStorageFactory();

	@Test
	public void createFileDestinationStorage() {
		DestinationStorage destinationStorage = factory.create("file://./src/test/resources");
		
		assertTrue(destinationStorage instanceof FileDestinationStorage);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void createNotSupportedStorage() {
		DestinationStorage destinationStorage = factory.create("ftp://www.google.com");
		fail("must throw exception");
	}
}
