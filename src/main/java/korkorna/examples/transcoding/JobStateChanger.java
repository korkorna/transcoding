package korkorna.examples.transcoding;

import korkorna.examples.transcoding.Job.State;

public interface JobStateChanger {

	void changeJobState(Long jobId, State newJobState);

}
