package korkorna.examples.transcoding.domain.job.callback;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import korkorna.examples.transcoding.domain.job.ResultCallback;

public class DefaultResultCallbackFactoryTest {

	private DefaultResultCallbackFactory callbackFactory = new DefaultResultCallbackFactory();
	
	@Test
	public void shouldCreatedHttpResultCallbackWHenUrlIsHttp() {
		ResultCallback callback = callbackFactory.create("http://localhost:9999/transcode/callback");
		assertTrue(callback instanceof HttpResultCallback);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenUrlIsNotSupported() {
		callbackFactory.create("xxxx:localhost:9999/transcode/callback");
	}
	
}
