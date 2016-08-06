package korkorna.examples.transcoding.domain.job;

public class OutputFormat {

	private int width;
	private int height;
	private int bitrate;
	private Container container;
	private VideoCodec videoCodec;
	private AudioCodec audioCodec;
	
	public OutputFormat(int width, int height, int bitrate, Container container, VideoCodec videoCodec, AudioCodec audioCodec) {
		super();
		this.width = width;
		this.height = height;
		this.bitrate = bitrate;
		this.container = container;
		this.videoCodec = videoCodec;
		this.audioCodec = audioCodec;
	}

	public OutputFormat(int width, int height, int bitrate, Container container) {
		// TODO Auto-generated constructor stub
		this(width, height, bitrate, container, container.getDefaultVideoCodec(), container.getDefaultAudioCodec());
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getBitrate() {
		return bitrate;
	}
	
	public Container getContainer() {
		return container;
	}

	public VideoCodec getVideoCodec() {
		return videoCodec;
	}

	public AudioCodec getAudioCodec() {
		return audioCodec;
	}

	public String getFileExtension() {
		// TODO Auto-generated method stub
		return container.getFileExtension();
	}
	

}
