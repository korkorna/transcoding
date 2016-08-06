package korkorna.examples.transcoding.domain.job;

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

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import korkorna.examples.transcoding.domain.job.Job.State;

@RunWith(MockitoJUnitRunner.class)
public class JobTest {

	private Long jobId = 1L;
	@Mock
	private MediaSourceFile mediaSourceFile;
	@Mock
	private DestinationStorage destinationStorage;
	@Mock
	private List<OutputFormat> outputFormats;
	@Mock
	private Transcoder transcoder;
	@Mock
	private ThumnailExtractor thumnailExtractor;
	@Mock
	private JobResultNotifier jobResultNotifier;

	@Test
	public void jobShouldBeCreatedStateWhenCreated() {
		Job job = new Job(jobId , mediaSourceFile, destinationStorage, outputFormats);
		
		assertEquals(Job.State.WAITING, job.getLastState());
		assertTrue(job.isWaiting());
		assertFalse(job.isFinished());
		assertFalse(job.isSuccess());
		assertFalse(job.isExceptionOccurred());
	}
	
	@Test
	public void transcodeSuccessfully() {
		Job job = new Job(jobId, mediaSourceFile, destinationStorage, outputFormats);
		
		job.transcode(transcoder, thumnailExtractor, jobResultNotifier);
		
		assertEquals(Job.State.COMPLETED, job.getLastState());
		assertTrue(job.isFinished());
		assertTrue(job.isSuccess());
		
		verify(mediaSourceFile, only()).getSourceFile();
		verify(destinationStorage, only()).save(anyListOf(File.class), anyListOf(File.class));
		verify(jobResultNotifier, only()).notifyToRequester(anyLong());
	}
	
	@Test
	public void jobShouldThrowExceptionWhenFailGetSourceFile() {
		RuntimeException mockException = new RuntimeException();
		when(mediaSourceFile.getSourceFile()).thenThrow(mockException);
		
		Job job = new Job(jobId, mediaSourceFile, destinationStorage, outputFormats);
		try {
			job.transcode(transcoder, thumnailExtractor, jobResultNotifier);
			fail("발생해야함.");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		assertEquals(Job.State.MEDIASOURCECOPYING, job.getLastState());
		assertTrue(job.isFinished());
		assertFalse(job.isSuccess());
		
		verify(mediaSourceFile, only()).getSourceFile();
		verify(destinationStorage, never()).save(anyListOf(File.class), anyListOf(File.class));
		verify(jobResultNotifier, never()).notifyToRequester(anyLong());
	}
}
