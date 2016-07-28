package korkorna.examples.transcoding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import korkorna.examples.transcoding.Job.State;

@RunWith(MockitoJUnitRunner.class)
public class TransCodingServiceTest {

	private Long jobId = new Long(1);
	
	@Mock
	private MediaSourceFile mediaSourceFile;
	@Mock
	private Transcoder transcoder;
	@Mock
	private ThumnailExtractor thumnailExtractor;
	@Mock
	private CreatedFileSender createdFileSender;
	@Mock
	private JobResultNotifier jobResultNotifier;
	@Mock
	private JobRepository jobRepository;

	private Job mockJob;
	private File mockMultimediaFile = mock(File.class);
	private List<File> mockMultimediaFiles = new ArrayList<File>();
	private List<File> mockThumnailFiles = new ArrayList<File>();
	private RuntimeException mockException = new RuntimeException();

	private TranscodingService trancdoingService;


	@Before
	public void setUp() {
		mockJob = new Job(jobId, mediaSourceFile);
		
		trancdoingService = new TranscodingServiceImpl(transcoder, thumnailExtractor,
				createdFileSender, jobResultNotifier, jobRepository);
		
		when(jobRepository.findById(jobId)).thenReturn(mockJob);

		when(mediaSourceFile.getSourceFile()).thenReturn(mockMultimediaFile);
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenReturn(mockMultimediaFiles);
		when(thumnailExtractor.extract(mockMultimediaFile, jobId)).thenReturn(mockThumnailFiles);
		
	}

	@Test
	public void transcodingSuccessfully() {
		assertJobIsWaitingState();
		trancdoingService.transcode(jobId);

		Job job = jobRepository.findById(jobId);
		assertTrue(job.isFinish());
		assertTrue(job.isSuccess());
		assertEquals(Job.State.COMPLETED, job.getLastState());
		assertNull(job.getOccurredException());

		VerifyOption verifyOption = new VerifyOption();
		verifyOption.transcoderNever = false;
		verifyOption.thumbnailExtractorNever = false;
		verifyOption.createFileSenderNever = false;
		verifyOption.jobResultNotifierNever = false;
		
		verifyCollaboration(verifyOption);
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtMediaSourceCopier() {
		when(mediaSourceFile.getSourceFile()).thenThrow(mockException);

		assertJobIsWaitingState();
		executeFailingTranscodeAndAssertFail(Job.State.MEDIASOURCECOPYING);

		VerifyOption verifyOption = new VerifyOption();
		verifyOption.transcoderNever = true;
		verifyOption.thumbnailExtractorNever = true;
		verifyOption.createFileSenderNever = true;
		verifyOption.jobResultNotifierNever = true;
		
		verifyCollaboration(verifyOption);
	}
	
	@Test
	public void trnascodeFailBecauseExceptionOccuredAtTranscoder() {
		when(transcoder.transcode(any(File.class), anyLong())).thenThrow(mockException);

		assertJobIsWaitingState();
		executeFailingTranscodeAndAssertFail(Job.State.TRANSCODING);

		VerifyOption verifyOption = new VerifyOption();
		verifyOption.transcoderNever = false;
		verifyOption.thumbnailExtractorNever = true;
		verifyOption.createFileSenderNever = true;
		verifyOption.jobResultNotifierNever = true;
		
		verifyCollaboration(verifyOption);
	}
	
	@Test
	public void transcodeFailBecauseExceptionOccuredAtThumnailExtractor() {
		when(thumnailExtractor.extract(any(File.class), anyLong())).thenThrow(mockException);

		assertJobIsWaitingState();
		executeFailingTranscodeAndAssertFail(Job.State.THUMNAILEXTRACTING);

		VerifyOption verifyOption = new VerifyOption();
		verifyOption.transcoderNever = false;
		verifyOption.thumbnailExtractorNever = false;
		verifyOption.createFileSenderNever = true;
		verifyOption.jobResultNotifierNever = true;
		
		verifyCollaboration(verifyOption);
	}
	
	@Test
	public void transcodeFailBecauseExceptionOccuredAtCreatedFileSender() {
		doThrow(mockException).when(createdFileSender).send(anyListOf(File.class), anyListOf(File.class), anyLong());
		
		assertJobIsWaitingState();
		executeFailingTranscodeAndAssertFail(Job.State.CREATEDFILESENDING);

		VerifyOption verifyOption = new VerifyOption();
		verifyOption.transcoderNever = false;
		verifyOption.thumbnailExtractorNever = false;
		verifyOption.createFileSenderNever = false;
		verifyOption.jobResultNotifierNever = true;
		
		verifyCollaboration(verifyOption);
	}
	
	@Test
	public void transcodeFailBecauseExceptionOccuredAtJobResultNotifier() {
		doThrow(mockException).when(jobResultNotifier).notifyToRequester(anyLong());

		assertJobIsWaitingState();
		executeFailingTranscodeAndAssertFail(Job.State.NOTIFING);

		VerifyOption verifyOption = new VerifyOption();
		verifyOption.transcoderNever = false;
		verifyOption.thumbnailExtractorNever = false;
		verifyOption.createFileSenderNever = false;
		verifyOption.jobResultNotifierNever = false;
		
		verifyCollaboration(verifyOption);
	}

	private void assertJobIsWaitingState() {
		Job job = jobRepository.findById(jobId);
		assertTrue(job.isWaiting());
	}
	
	private void executeFailingTranscodeAndAssertFail(State executeState) {

		try {
			trancdoingService.transcode(jobId);
			fail("발생해야함.");
		} catch (Exception e) {
			// TODO: handle exception
			assertSame(mockException, e);
		}

		Job job = jobRepository.findById(jobId);
		assertTrue(job.isFinish());
		assertFalse(job.isSuccess());
		assertEquals(executeState, job.getLastState());
		assertNotNull(job.getOccurredException());
	}
	
	
	private void verifyCollaboration(VerifyOption verifyOption) {
		if (verifyOption.transcoderNever) {
			verify(transcoder, never()).transcode(any(File.class), anyLong());
		} else {
			verify(transcoder, only()).transcode(any(File.class), anyLong());
		}
		
		if (verifyOption.thumbnailExtractorNever) {
			verify(thumnailExtractor, never()).extract(any(File.class), anyLong());
		} else {
			verify(thumnailExtractor, only()).extract(any(File.class), anyLong());
		}
		
		if (verifyOption.createFileSenderNever) {
			verify(createdFileSender, never()).send(anyListOf(File.class), anyListOf(File.class), anyLong());
		} else {
			verify(createdFileSender, only()).send(anyListOf(File.class), anyListOf(File.class), anyLong());
		}
		
		if (verifyOption.jobResultNotifierNever) {
			verify(jobResultNotifier, never()).notifyToRequester(anyLong());
		} else {
			verify(jobResultNotifier, only()).notifyToRequester(anyLong());
		}
	}
	
	public class VerifyOption {
		public boolean transcoderNever;
		public boolean thumbnailExtractorNever;
		public boolean createFileSenderNever;
		public boolean jobResultNotifierNever;
	}

}
