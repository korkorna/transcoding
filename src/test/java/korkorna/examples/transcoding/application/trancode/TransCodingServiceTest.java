package korkorna.examples.transcoding.application.trancode;

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

import korkorna.examples.transcoding.domain.job.DestinationStorage;
import korkorna.examples.transcoding.domain.job.Job;
import korkorna.examples.transcoding.domain.job.Job.State;
import korkorna.examples.transcoding.domain.job.JobRepository;
import korkorna.examples.transcoding.domain.job.MediaSourceFile;
import korkorna.examples.transcoding.domain.job.OutputFormat;
import korkorna.examples.transcoding.domain.job.ResultCallback;
import korkorna.examples.transcoding.domain.job.ThumnailExtractor;
import korkorna.examples.transcoding.domain.job.Transcoder;

@RunWith(MockitoJUnitRunner.class)
public class TransCodingServiceTest {

	private Long jobId = new Long(1);
	
	@Mock
	private MediaSourceFile mediaSourceFile;
	@Mock
	private DestinationStorage destinationStorage;
	@Mock
	private ResultCallback callback;
	@Mock
	private Transcoder transcoder;
	@Mock
	private ThumnailExtractor thumnailExtractor;
	@Mock
	private JobRepository jobRepository;
	@Mock
	private List<OutputFormat> outputForamts;

	private Job mockJob;
	private File mockMultimediaFile = mock(File.class);
	private List<File> mockMultimediaFiles = new ArrayList<File>();
	private List<File> mockThumnailFiles = new ArrayList<File>();
	private RuntimeException mockException = new RuntimeException("error ");

	private TranscodingService trancdoingService;


	@Before
	public void setUp() {
		mockJob = new Job(jobId, mediaSourceFile, destinationStorage, callback, outputForamts);
		
		trancdoingService = new TranscodingServiceImpl(transcoder, thumnailExtractor,
				jobRepository);
		
		when(jobRepository.findById(jobId)).thenReturn(mockJob);

		when(mediaSourceFile.getSourceFile()).thenReturn(mockMultimediaFile);
		when(transcoder.transcode(mockMultimediaFile, outputForamts)).thenReturn(mockMultimediaFiles);
		when(thumnailExtractor.extract(mockMultimediaFile, jobId)).thenReturn(mockThumnailFiles);
		
	}

	@Test
	public void transcodingSuccessfully() {
		assertJobIsWaitingState();
		trancdoingService.transcode(jobId);

		Job job = jobRepository.findById(jobId);
		assertTrue(job.isFinished());
		assertTrue(job.isSuccess());
		assertEquals(Job.State.COMPLETED, job.getLastState());
		assertNull(job.getExceptionMessage());

		CollaborationVerifier collaborationVerifier = new CollaborationVerifier();
		collaborationVerifier.transcoderNever = false;
		collaborationVerifier.thumbnailExtractorNever = false;
		collaborationVerifier.destinationStorageNever = false;
		
		collaborationVerifier.verifyCollaboration(collaborationVerifier);
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtMediaSourceFile() {
		when(mediaSourceFile.getSourceFile()).thenThrow(mockException);

		assertJobIsWaitingState();
		executeFailingTranscodeAndAssertFail(Job.State.MEDIASOURCECOPYING);

		CollaborationVerifier collaborationVerifier = new CollaborationVerifier();
		collaborationVerifier.transcoderNever = true;
		collaborationVerifier.thumbnailExtractorNever = true;
		collaborationVerifier.destinationStorageNever = true;
		
		collaborationVerifier.verifyCollaboration(collaborationVerifier);
	}
	
	@Test
	public void trnascodeFailBecauseExceptionOccuredAtTranscoder() {
		when(transcoder.transcode(any(File.class), anyListOf(OutputFormat.class))).thenThrow(mockException);

		assertJobIsWaitingState();
		executeFailingTranscodeAndAssertFail(Job.State.TRANSCODING);

		CollaborationVerifier collaborationVerifier = new CollaborationVerifier();
		collaborationVerifier.transcoderNever = false;
		collaborationVerifier.thumbnailExtractorNever = true;
		collaborationVerifier.destinationStorageNever = true;
		
		collaborationVerifier.verifyCollaboration(collaborationVerifier);
	}
	
	@Test
	public void transcodeFailBecauseExceptionOccuredAtThumnailExtractor() {
		when(thumnailExtractor.extract(any(File.class), anyLong())).thenThrow(mockException);

		assertJobIsWaitingState();
		executeFailingTranscodeAndAssertFail(Job.State.THUMNAILEXTRACTING);

		CollaborationVerifier collaborationVerifier = new CollaborationVerifier();
		collaborationVerifier.transcoderNever = false;
		collaborationVerifier.thumbnailExtractorNever = false;
		collaborationVerifier.destinationStorageNever = true;
		
		collaborationVerifier.verifyCollaboration(collaborationVerifier);
	}
	
	@Test
	public void transcodeFailBecauseExceptionOccuredAtDestinationStorage() {
		doThrow(mockException).when(destinationStorage).save(anyListOf(File.class), anyListOf(File.class));
		
		assertJobIsWaitingState();
		executeFailingTranscodeAndAssertFail(Job.State.STORING);

		CollaborationVerifier collaborationVerifier = new CollaborationVerifier();
		collaborationVerifier.transcoderNever = false;
		collaborationVerifier.thumbnailExtractorNever = false;
		collaborationVerifier.destinationStorageNever = false;
		
		collaborationVerifier.verifyCollaboration(collaborationVerifier);
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
		assertTrue(job.isFinished());
		assertFalse(job.isSuccess());
		assertEquals(executeState, job.getLastState());
		assertNotNull(job.getExceptionMessage());
	}
	
	
	public class CollaborationVerifier {
		public boolean transcoderNever;
		public boolean thumbnailExtractorNever;
		public boolean destinationStorageNever;
		public boolean jobResultNotifierNever;
		
		public void verifyCollaboration(CollaborationVerifier collaborationVerifier) {
			if (this.transcoderNever) {
				verify(transcoder, never()).transcode(any(File.class), anyListOf(OutputFormat.class));
			} else {
				verify(transcoder, only()).transcode(any(File.class), anyListOf(OutputFormat.class));
			}
			
			if (this.thumbnailExtractorNever) {
				verify(thumnailExtractor, never()).extract(any(File.class), anyLong());
			} else {
				verify(thumnailExtractor, only()).extract(any(File.class), anyLong());
			}
			
			if (this.destinationStorageNever) {
				verify(destinationStorage, never()).save(anyListOf(File.class), anyListOf(File.class));
			} else {
				verify(destinationStorage, only()).save(anyListOf(File.class), anyListOf(File.class));
			}
			
		}
	}

}
