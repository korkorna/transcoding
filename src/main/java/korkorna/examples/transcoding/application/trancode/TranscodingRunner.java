package korkorna.examples.transcoding.application.trancode;

public class TranscodingRunner {
	private TranscodingService transcodingService;
	private JobQueue jobQueue;

	public TranscodingRunner(TranscodingService transcodingService, JobQueue jobQueue) {
		super();
		this.transcodingService = transcodingService;
		this.jobQueue = jobQueue;
	}

	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			Long jobId = null;
			try {
				jobId = getNextWaitingJob();
			} catch (JobQueue.ClosedException e) {
				// TODO: handle exception
				break;
			}
			runTranscoding(jobId);
		}
	}

	private void runTranscoding(Long jobId) {
		// TODO Auto-generated method stub
		try {
			transcodingService.transcode(jobId);
		} catch (RuntimeException e) {
			// TODO: handle exception
		}
	}

	private Long getNextWaitingJob() {
		// TODO Auto-generated method stub
		return jobQueue.nextJobId();
	}
	
	public void stop() {}
}
