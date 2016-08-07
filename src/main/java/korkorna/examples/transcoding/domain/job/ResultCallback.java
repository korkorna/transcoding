package korkorna.examples.transcoding.domain.job;

import korkorna.examples.transcoding.domain.job.Job.State;

public interface ResultCallback {
	
	void notifyFailedResult(long anyLong, State mediasourcecopying, String string);

	void notifySurccessResult(long anyLong);
}
