package korkorna.examples.transcoding.application.trancode;

import korkorna.examples.transcoding.domain.job.DestinationStorage;
import korkorna.examples.transcoding.domain.job.DestinationStorageFactory;
import korkorna.examples.transcoding.domain.job.Job;
import korkorna.examples.transcoding.domain.job.JobRepository;
import korkorna.examples.transcoding.domain.job.MediaSourceFile;
import korkorna.examples.transcoding.domain.job.MediaSourceFileFactory;

public class AddJobServiceImpl implements AddJobService{

	private MediaSourceFileFactory mediaSourceFileFactory;
	private DestinationStorageFactory destinationStorageFactory;
	private JobRepository jobRepository;

	public AddJobServiceImpl(MediaSourceFileFactory mediaSourceFileFactory,
			DestinationStorageFactory destinationStorageFactory, JobRepository jobRepository) {
		super();
		this.mediaSourceFileFactory = mediaSourceFileFactory;
		this.destinationStorageFactory = destinationStorageFactory;
		this.jobRepository = jobRepository;
	}

	public Long addJob(AddJobRequest request) {
		Job job = createJob(request);
		Job saveJob = saveJob(job);
		return saveJob.getId();
	}

	private Job createJob(AddJobRequest request) {
		//Job 생성
		MediaSourceFile mediaSourceFile = mediaSourceFileFactory.create(request.getMediaSource());
		DestinationStorage destinationStorage = destinationStorageFactory.create(request.getDestinationStorage());
		return new Job(mediaSourceFile, destinationStorage, request.getOutputFormats());
	}

	private Job saveJob(Job job) {
		//Job 저장.
		return jobRepository.save(job);
	}

}
