package korkorna.examples.transcoding;

public interface TranscodingExceptionHandler {

	void notifiyToJob(Long jobId, RuntimeException e);

}
