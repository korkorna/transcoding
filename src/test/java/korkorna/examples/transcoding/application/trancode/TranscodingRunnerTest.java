package korkorna.examples.transcoding.application.trancode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyListOf;
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
import korkorna.examples.transcoding.domain.job.JobRepository;

@RunWith(MockitoJUnitRunner.class)
public class TranscodingRunnerTest {

	@Mock
	private Job job;
	@Mock
	private JobRepository jobRepository;
	
	private TranscodingRunner runner;
	private TranscodingService transcodingService;
	
	@Before
	public void setup() {
		runner = new TranscodingRunner();
	}
	
	@Test
	public void runTranscodingWhenJobIsExists() {
		when(job.getId()).thenReturn(1L);
		when(jobRepository.findEldestJobOfCreatedState()).thenReturn(job);
		
		runner.run();
		
		verify(transcodingService, only()).transcode(1L);
	}
	
	@Test
	public void dontRunTranscodingWhenJobIsNotExists() {
		when(jobRepository.findEldestJobOfCreatedState()).thenReturn(null);
		
		runner.run();
		
		verify(transcodingService, never()).transcode(anyLong());
	}
}
