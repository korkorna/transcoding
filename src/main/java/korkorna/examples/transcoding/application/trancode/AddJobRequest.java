package korkorna.examples.transcoding.application.trancode;

import java.util.List;

import korkorna.examples.transcoding.domain.job.OutputFormat;

public class AddJobRequest {

	private String mediaSource;
	private String destinationStorage;
	private List<OutputFormat> outputFormats;
	private String resultCallback;
	
	public String getMediaSource() {
		// TODO Auto-generated method stub
		return mediaSource;
	}

	public String getDestinationStorage() {
		// TODO Auto-generated method stub
		return destinationStorage;
	}

	public List<OutputFormat> getOutputFormats() {
		// TODO Auto-generated method stub
		return outputFormats;
	}

	public String getResultCallback() {
		// TODO Auto-generated method stub
		return resultCallback;
	}

	public void setResultCallback(String resultCallback) {
		this.resultCallback = resultCallback;
	}

	@Override
	public String toString() {
		return "AddJobRequest [mediaSource=" + mediaSource + ", destinationStorage=" + destinationStorage
				+ ", outputFormats=" + outputFormats + ", resultCallback=" + resultCallback + "]";
	}
	
}
