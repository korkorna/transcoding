package korkorna.examples.transcoding.application.trancode;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import korkorna.examples.transcoding.domain.job.Job;

@RunWith(MockitoJUnitRunner.class)
public class TranscodingRunnerTest {

	@Mock
	private Job job;
	@Mock
	private TranscodingService transcodingService;
	@Mock
	private JobQueue jobQueue;
	
	private TranscodingRunner runner;
	
	@Before
	public void setup() {
		runner = new TranscodingRunner(transcodingService, jobQueue);
	}
	
	@Test
	public void runTranscodingWhenJobIsExists() {
		when(job.getId()).thenReturn(1L);
		when(jobQueue.nextJobId()).thenReturn(job.getId());
		
		runner.run();
		
		verify(transcodingService, only()).transcode(1L);
	}
	
	@Test
	public void dontRunTranscodingWhenJobIsNotExists() {
		when(jobQueue.nextJobId()).thenReturn(null);
		
		runner.run();
		
		verify(transcodingService, never()).transcode(anyLong());
	}
}
