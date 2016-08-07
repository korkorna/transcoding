package korkorna.examples.transcoding.domain.job.destinationstorage;

import korkorna.examples.transcoding.domain.job.DestinationStorage;
import korkorna.examples.transcoding.domain.job.DestinationStorageFactory;
import korkorna.examples.transcoding.domain.job.FileDestinationStorage;

public class DefaultDestinationStorageFactory implements DestinationStorageFactory {

	public DestinationStorage create(String destinationStorage) {
		if (destinationStorage.startsWith("file://")) {
			return new FileDestinationStorage(
					destinationStorage.substring("file://".length()));
		}
		
		throw new IllegalArgumentException("not supported destination storage: "
				+ destinationStorage);
	}

}
