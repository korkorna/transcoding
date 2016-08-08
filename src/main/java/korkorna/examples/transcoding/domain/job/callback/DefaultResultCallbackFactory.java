package korkorna.examples.transcoding.domain.job.callback;

import korkorna.examples.transcoding.application.trancode.ResultCallbackFactory;
import korkorna.examples.transcoding.domain.job.ResultCallback;

public class DefaultResultCallbackFactory implements ResultCallbackFactory{

	public ResultCallback create(String url) {
		// TODO Auto-generated method stub
		if (url.startsWith("http://") || url.startsWith("https://")) {
			return new HttpResultCallback(url);
		}
		
		throw new IllegalArgumentException();
	}

}
