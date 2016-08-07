package korkorna.examples.transcoding.domain.job;

public interface DestinationStorageFactory {

	DestinationStorage create(String destinationStorage);

}
