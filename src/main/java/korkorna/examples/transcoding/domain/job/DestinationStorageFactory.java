package korkorna.examples.transcoding.domain.job;

public interface DestinationStorageFactory {

	DestinationStorage create(String destinationStorage);

	DestinationStorageFactory DEFAULT = new DestinationStorageFactory() {
		
		public DestinationStorage create(String destinationStorage) {
			// TODO Auto-generated method stub
			if (destinationStorage.startsWith("file://")) {
				return new FileDestinationStorage(
						destinationStorage.substring("file://".length()));
			}
			
			throw new IllegalArgumentException("not supported destination storage: "
					+ destinationStorage);
		}
	};
}
