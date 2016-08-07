package korkorna.examples.transcoding.application.trancode;

import korkorna.examples.transcoding.domain.job.ResultCallback;

public interface ResultCallbackFactory {

	ResultCallback create(String resultCallback);

}
