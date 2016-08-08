package korkorna.examples.transcoding.application.trancode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import korkorna.examples.transcoding.domain.job.DestinationStorage;
import korkorna.examples.transcoding.domain.job.DestinationStorageFactory;
import korkorna.examples.transcoding.domain.job.Job;
import korkorna.examples.transcoding.domain.job.JobRepository;
import korkorna.examples.transcoding.domain.job.MediaSourceFile;
import korkorna.examples.transcoding.domain.job.MediaSourceFileFactory;
import korkorna.examples.transcoding.domain.job.ResultCallback;

public class AddJobServiceImpl implements AddJobService{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private MediaSourceFileFactory mediaSourceFileFactory;
	private DestinationStorageFactory destinationStorageFactory;
	private ResultCallbackFactory resultCallbackFactory;
	private JobRepository jobRepository;
	private JobQueue jobQueue;

	public AddJobServiceImpl(MediaSourceFileFactory mediaSourceFileFactory,
			DestinationStorageFactory destinationStorageFactory, ResultCallbackFactory resultCallbackFactory,
			JobRepository jobRepository, JobQueue jobQueue) {
		super();
		this.mediaSourceFileFactory = mediaSourceFileFactory;
		this.destinationStorageFactory = destinationStorageFactory;
		this.resultCallbackFactory = resultCallbackFactory;
		this.jobRepository = jobRepository;
		this.jobQueue = jobQueue;
	}

	public Long addJob(AddJobRequest request) {
		Job job = createJob(request);
		Job saveJob = saveJob(job);
		jobQueue.add(saveJob.getId());
		return saveJob.getId();
	}

	private Job createJob(AddJobRequest request) {
		try {
			//Job 생성
			MediaSourceFile mediaSourceFile = mediaSourceFileFactory.create(request.getMediaSource());
			DestinationStorage destinationStorage = destinationStorageFactory.create(request.getDestinationStorage());
			ResultCallback resultCallback = resultCallbackFactory.create(request.getResultCallback());
			return new Job(mediaSourceFile, destinationStorage, resultCallback, request.getOutputFormats());
		} catch (RuntimeException e) {
			// TODO: handle exception
			logger.error("fail to create job from request {}", request, e);
			throw e;
		}
	}

	private Job saveJob(Job job) {
		try {
			//Job 저장
			return jobRepository.save(job);
		} catch (RuntimeException e) {
			// TODO: handle exception
			logger.error("fail to save job to repository", e);
			throw e;
		}
	}

}
