package korkorna.examples.transcoding.domain.job.callback;

import korkorna.examples.transcoding.domain.job.Job.State;
import korkorna.examples.transcoding.domain.job.ResultCallback;

public class HttpResultCallback extends ResultCallback{

	public HttpResultCallback(String url) {
		super(url);
	}
	
	public void notifyFailedResult(long anyLong, State mediasourcecopying, String string) {
		// TODO Auto-generated method stub
		
	}

	public void notifySurccessResult(long anyLong) {
		// TODO Auto-generated method stub
		
	}

}
