package korkorna.examples.transcoding.domain.job;

public interface JobResultNotifier {

	void notifyToRequester(Long jobId);

}
