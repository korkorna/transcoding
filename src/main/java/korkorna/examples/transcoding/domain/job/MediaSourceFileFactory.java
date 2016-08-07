package korkorna.examples.transcoding.domain.job;

public interface MediaSourceFileFactory {

	MediaSourceFile create(String mediaSource);
	
	MediaSourceFileFactory DEFAULT = new MediaSourceFileFactory() {
		
		public MediaSourceFile create(String mediaSource) {
			// TODO Auto-generated method stub
			if (mediaSource.startsWith("file://")) {
				String filePath = mediaSource.substring("file://".length());
				return new LocalStorageMediaSourceFile(filePath);
			}
			
			throw new IllegalArgumentException("not supported media source: " 
					+ mediaSource);
		}
	};

}
