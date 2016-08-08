package korkorna.examples.transcoding.domain.job;

import korkorna.examples.transcoding.domain.job.Job.State;

public abstract class ResultCallback {
	
	private String url;
	
	public ResultCallback(String url) {
		super();
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
	
	public abstract void notifyFailedResult(long anyLong, State mediasourcecopying, String string);

	public abstract void notifySurccessResult(long anyLong);
}
