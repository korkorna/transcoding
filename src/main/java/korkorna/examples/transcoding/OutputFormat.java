package korkorna.examples.transcoding;

public class OutputFormat {

	private int width;
	private int height;
	private int bitrate;
	private VideoCodec videoCodec;
	private AudioCodec audioCodec;
	
	public OutputFormat(int width, int height, int bitrate, VideoCodec videoCodec, AudioCodec audioCodec) {
		super();
		this.width = width;
		this.height = height;
		this.bitrate = bitrate;
		this.videoCodec = videoCodec;
		this.audioCodec = audioCodec;
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

	public VideoCodec getVideoCodec() {
		return videoCodec;
	}

	public AudioCodec getAudioCodec() {
		return audioCodec;
	}
	

}
