package korkorna.examples.transcoding.domain.job;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class DestinationStorageFactoryDefaultTest {

	private DestinationStorageFactory factory = DestinationStorageFactory.DEFAULT;;

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
