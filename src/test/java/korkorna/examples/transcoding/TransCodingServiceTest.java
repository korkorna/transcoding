package korkorna.examples.transcoding;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import korkorna.examples.transcoding.Job.State;

@RunWith(MockitoJUnitRunner.class)
public class TransCodingServiceTest {

	private Long jobId = new Long(1);

	@Mock
	private MediaSourceCopier mediaSourceCopier;
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
	@Mock
	private JobStateChanger jobStateChanger;
	@Mock
	private TranscodingExceptionHandler transcodingExceptionHandler;

	private Job mockJob = new Job();

	private TranscodingService trancdoingService;

	@Before
	public void setUp() {
		trancdoingService = new TranscodingServiceImpl(mediaSourceCopier, transcoder, thumnailExtractor,
				createdFileSender, jobResultNotifier, jobStateChanger, transcodingExceptionHandler);
		
		doAnswer(new Answer<Object>() {

			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				Job.State newState = (State) invocation.getArguments()[1];
				mockJob.changeState(newState);
				return null;
			}
		}).when(jobStateChanger).changeJobState(anyLong(), any(Job.State.class));
		
		doAnswer(new Answer<Object>() {

			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				RuntimeException e = (RuntimeException) invocation.getArguments()[1];
				mockJob.exceptionOccurred(e);
				return null;
			}
		}).when(transcodingExceptionHandler).notifiyToJob(anyLong(), any(RuntimeException.class));
	}

	@Test
	public void transcodingSuccessfully() {
		when(jobRepository.findById(jobId)).thenReturn(mockJob);

		File mockMultimediaFile = mock(File.class);
		when(mediaSourceCopier.copy(jobId)).thenReturn(mockMultimediaFile);

		List<File> mockMultimediaFiles = new ArrayList<File>();
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenReturn(mockMultimediaFiles);

		List<File> mockThumnailFiles = new ArrayList<File>();
		when(thumnailExtractor.extract(mockMultimediaFile, jobId)).thenReturn(mockThumnailFiles);

		Job job = jobRepository.findById(jobId);
		assertTrue(job.isWaiting());

		trancdoingService.transcode(jobId);

		assertTrue(job.isFinish());
		assertTrue(job.isSuccess());
		assertEquals(Job.State.COMPLETED, job.getLastState());
		assertNull(job.getOccurredException());

		verify(mediaSourceCopier, only()).copy(jobId);
		verify(transcoder, only()).transcode(mockMultimediaFile, jobId);
		verify(thumnailExtractor, only()).extract(mockMultimediaFile, jobId);
		verify(createdFileSender, only()).send(mockMultimediaFiles, mockThumnailFiles, jobId);
		verify(jobResultNotifier, only()).notifyToRequester(jobId);
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtMediaSourceCopier() {
		when(jobRepository.findById(jobId)).thenReturn(mockJob);

		File mockMultimediaFile = mock(File.class);
		when(mediaSourceCopier.copy(jobId)).thenReturn(mockMultimediaFile);

		List<File> mockMultimediaFiles = new ArrayList<File>();
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenReturn(mockMultimediaFiles);

		List<File> mockThumnailFiles = new ArrayList<File>();
		when(thumnailExtractor.extract(mockMultimediaFile, jobId)).thenReturn(mockThumnailFiles);
		
		RuntimeException mockException = new RuntimeException();
		when(mediaSourceCopier.copy(jobId)).thenThrow(mockException);

		Job job = jobRepository.findById(jobId);
		assertTrue(job.isWaiting());

		try {
			trancdoingService.transcode(jobId);
			fail("발생해야함.");
		} catch (Exception e) {
			// TODO: handle exception
			assertSame(mockException, e);
		}

		assertTrue(job.isFinish());
		assertFalse(job.isSuccess());
		assertEquals(Job.State.MEDIASOURCECOPYING, job.getLastState());
		assertNotNull(job.getOccurredException());

		verify(mediaSourceCopier, only()).copy(jobId);
		verify(transcoder, never()).transcode(any(File.class), anyLong());
		verify(thumnailExtractor, never()).extract(any(File.class), anyLong());
		verify(createdFileSender, never()).send(anyListOf(File.class), anyListOf(File.class), anyLong());
		verify(jobResultNotifier, never()).notifyToRequester(anyLong());
	}
	
	@Test
	public void trnascodeFailBecauseExceptionOccuredAtTranscoder() {
		when(jobRepository.findById(jobId)).thenReturn(mockJob);

		File mockMultimediaFile = mock(File.class);
		when(mediaSourceCopier.copy(jobId)).thenReturn(mockMultimediaFile);

		List<File> mockMultimediaFiles = new ArrayList<File>();
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenReturn(mockMultimediaFiles);

		List<File> mockThumnailFiles = new ArrayList<File>();
		when(thumnailExtractor.extract(mockMultimediaFile, jobId)).thenReturn(mockThumnailFiles);
		
		RuntimeException mockException = new RuntimeException();
		when(transcoder.transcode(any(File.class), anyLong())).thenThrow(mockException);

		Job job = jobRepository.findById(jobId);
		assertTrue(job.isWaiting());

		try {
			trancdoingService.transcode(jobId);
			fail("발생해야함.");
		} catch (Exception e) {
			// TODO: handle exception
			assertSame(mockException, e);
		}

		assertTrue(job.isFinish());
		assertFalse(job.isSuccess());
		assertEquals(Job.State.TRANSCODING, job.getLastState());
		assertNotNull(job.getOccurredException());

		verify(mediaSourceCopier, only()).copy(jobId);
		verify(transcoder, only()).transcode(any(File.class), anyLong());
		verify(thumnailExtractor, never()).extract(any(File.class), anyLong());
		verify(createdFileSender, never()).send(anyListOf(File.class), anyListOf(File.class), anyLong());
		verify(jobResultNotifier, never()).notifyToRequester(anyLong());
	}
	
	@Test
	public void transcodeFailBecauseExceptionOccuredAtThumnailExtractor() {
		when(jobRepository.findById(jobId)).thenReturn(mockJob);

		File mockMultimediaFile = mock(File.class);
		when(mediaSourceCopier.copy(jobId)).thenReturn(mockMultimediaFile);

		List<File> mockMultimediaFiles = new ArrayList<File>();
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenReturn(mockMultimediaFiles);

		List<File> mockThumnailFiles = new ArrayList<File>();
		when(thumnailExtractor.extract(mockMultimediaFile, jobId)).thenReturn(mockThumnailFiles);
		
		RuntimeException mockException = new RuntimeException();
		when(thumnailExtractor.extract(any(File.class), anyLong())).thenThrow(mockException);

		Job job = jobRepository.findById(jobId);
		assertTrue(job.isWaiting());

		try {
			trancdoingService.transcode(jobId);
			fail("발생해야함.");
		} catch (Exception e) {
			// TODO: handle exception
			assertSame(mockException, e);
		}

		assertTrue(job.isFinish());
		assertFalse(job.isSuccess());
		assertEquals(Job.State.THUMNAILEXTRACTING, job.getLastState());
		assertNotNull(job.getOccurredException());

		verify(mediaSourceCopier, only()).copy(jobId);
		verify(transcoder, only()).transcode(any(File.class), anyLong());
		verify(thumnailExtractor, only()).extract(any(File.class), anyLong());
		verify(createdFileSender, never()).send(anyListOf(File.class), anyListOf(File.class), anyLong());
		verify(jobResultNotifier, never()).notifyToRequester(anyLong());
	}
	
	@Test
	public void transcodeFailBecauseExceptionOccuredAtCreatedFileSender() {
		when(jobRepository.findById(jobId)).thenReturn(mockJob);

		File mockMultimediaFile = mock(File.class);
		when(mediaSourceCopier.copy(jobId)).thenReturn(mockMultimediaFile);

		List<File> mockMultimediaFiles = new ArrayList<File>();
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenReturn(mockMultimediaFiles);

		List<File> mockThumnailFiles = new ArrayList<File>();
		when(thumnailExtractor.extract(mockMultimediaFile, jobId)).thenReturn(mockThumnailFiles);
		
		RuntimeException mockException = new RuntimeException();
		doThrow(mockException).when(createdFileSender).send(anyListOf(File.class), anyListOf(File.class), anyLong());
		
		Job job = jobRepository.findById(jobId);
		assertTrue(job.isWaiting());

		try {
			trancdoingService.transcode(jobId);
			fail("발생해야함.");
		} catch (Exception e) {
			// TODO: handle exception
			assertSame(mockException, e);
		}

		assertTrue(job.isFinish());
		assertFalse(job.isSuccess());
		assertEquals(Job.State.CREATEDFILESENDING, job.getLastState());
		assertNotNull(job.getOccurredException());

		verify(mediaSourceCopier, only()).copy(jobId);
		verify(transcoder, only()).transcode(any(File.class), anyLong());
		verify(thumnailExtractor, only()).extract(any(File.class), anyLong());
		verify(createdFileSender, only()).send(anyListOf(File.class), anyListOf(File.class), anyLong());
		verify(jobResultNotifier, never()).notifyToRequester(anyLong());
	}
	
	@Test
	public void transcodeFailBecauseExceptionOccuredAtJobResultNotifier() {
		when(jobRepository.findById(jobId)).thenReturn(mockJob);

		File mockMultimediaFile = mock(File.class);
		when(mediaSourceCopier.copy(jobId)).thenReturn(mockMultimediaFile);

		List<File> mockMultimediaFiles = new ArrayList<File>();
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenReturn(mockMultimediaFiles);

		List<File> mockThumnailFiles = new ArrayList<File>();
		when(thumnailExtractor.extract(mockMultimediaFile, jobId)).thenReturn(mockThumnailFiles);
		
		RuntimeException mockException = new RuntimeException();
		doThrow(mockException).when(jobResultNotifier).notifyToRequester(anyLong());

		Job job = jobRepository.findById(jobId);
		assertTrue(job.isWaiting());

		try {
			trancdoingService.transcode(jobId);
			fail("발생해야함.");
		} catch (Exception e) {
			// TODO: handle exception
			assertSame(mockException, e);
		}

		assertTrue(job.isFinish());
		assertFalse(job.isSuccess());
		assertEquals(Job.State.NOTIFING, job.getLastState());
		assertNotNull(job.getOccurredException());

		verify(mediaSourceCopier, only()).copy(jobId);
		verify(transcoder, only()).transcode(any(File.class), anyLong());
		verify(thumnailExtractor, only()).extract(any(File.class), anyLong());
		verify(createdFileSender, only()).send(anyListOf(File.class), anyListOf(File.class), anyLong());
		verify(jobResultNotifier, only()).notifyToRequester(anyLong());
	}

}
