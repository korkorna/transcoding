package korkorna.examples.transcoding.application.trancode;

@SuppressWarnings("serial")
public class JobNotFoundException extends RuntimeException{

	public JobNotFoundException(Long jobId) {
		super("Not found Job[" +jobId + "]");
	}
}
