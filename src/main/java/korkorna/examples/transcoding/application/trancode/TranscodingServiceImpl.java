package korkorna.examples.transcoding.application.trancode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import korkorna.examples.transcoding.domain.job.Job;
import korkorna.examples.transcoding.domain.job.JobRepository;
import korkorna.examples.transcoding.domain.job.ThumnailExtractor;
import korkorna.examples.transcoding.domain.job.Transcoder;

public class TranscodingServiceImpl implements TranscodingService{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private Transcoder transcoder;
	private ThumnailExtractor thumnailExtractor;
	private JobRepository jobRepository;

	public TranscodingServiceImpl(Transcoder transcoder, ThumnailExtractor thumnailExtractor,
			JobRepository jobRepository) {
		super();
		this.transcoder = transcoder;
		this.thumnailExtractor = thumnailExtractor;
		this.jobRepository = jobRepository;
	}

	public void transcode(Long jobId) {
		Job job = jobRepository.findById(jobId);
		checkJobExists(jobId, job);
		transcode(job);
	}

	private void transcode(Job job) {
		// TODO Auto-generated method stub
		try {
			job.transcode(transcoder, thumnailExtractor);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("fail to do transcoding job {}", job.getId(), e);
		}
	}

	private void checkJobExists(Long jobId, Job job) {
		// TODO Auto-generated method stub
		if (job == null) {
			throw new JobNotFoundException(jobId);
		}
	}

}
