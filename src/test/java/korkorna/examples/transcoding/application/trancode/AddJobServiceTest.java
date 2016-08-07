package korkorna.examples.transcoding.application.trancode;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import korkorna.examples.transcoding.domain.job.DestinationStorage;
import korkorna.examples.transcoding.domain.job.DestinationStorageFactory;
import korkorna.examples.transcoding.domain.job.Job;
import korkorna.examples.transcoding.domain.job.JobRepository;
import korkorna.examples.transcoding.domain.job.MediaSourceFile;
import korkorna.examples.transcoding.domain.job.MediaSourceFileFactory;

@RunWith(MockitoJUnitRunner.class)
public class AddJobServiceTest {

	@Mock
	private MediaSourceFileFactory mediaSourceFileFactory;
	@Mock
	private MediaSourceFile mockMediaSourceFile;
	@Mock
	private DestinationStorageFactory destinationStorageFactory;
	@Mock
	private DestinationStorage mockDestinationStorage;
	@Mock
	private JobRepository jobRepository;
	
	private AddJobService addJobService;
	
	@Before
	public void setup() {
		addJobService = new AddJobServiceImpl(mediaSourceFileFactory, destinationStorageFactory, jobRepository);
	}

	@Test
	public void addJobSuccessfully() {
		AddJobRequest request = new AddJobRequest();
		
		when(mediaSourceFileFactory.create(request.getMediaSource()))
			.thenReturn(mockMediaSourceFile);
		when(destinationStorageFactory.create(request.getDestinationStorage()))
			.thenReturn(mockDestinationStorage);
		
		final Long mockJobId = 1L;
		Job mockSavedJob = mock(Job.class);
		when(mockSavedJob.getId()).thenReturn(mockJobId);
		when(jobRepository.save(any(Job.class))).thenReturn(mockSavedJob);
		
		Long jobId = addJobService.addJob(request);
		
		assertNotNull(jobId);
		
		verify(jobRepository, only()).save(any(Job.class));
		verify(mediaSourceFileFactory, only()).create(request.getMediaSource());
		verify(destinationStorageFactory, only()).create(request.getDestinationStorage());
		
	}
}
