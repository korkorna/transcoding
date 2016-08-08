package korkorna.examples.transcoding.application.trancode;

public interface JobQueue {

	Long nextJobId();

	void add(Long id);

	@SuppressWarnings("serial")
	public class ClosedException extends RuntimeException {}
}
