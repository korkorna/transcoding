package korkorna.examples.transcoding.infra.ffmpeg;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;

import korkorna.examples.transcoding.domain.job.AudioCodec;
import korkorna.examples.transcoding.domain.job.Container;
import korkorna.examples.transcoding.domain.job.OutputFormat;
import korkorna.examples.transcoding.domain.job.VideoCodec;

public class VideoConverterTest {
	private static final int WIDTH = 160;
    private static final int HEIGHT = 120;
    private static final int BITRATE = 150;
    private static final String SOURCE_FILE = "src/test/resources/sample.avi";
    private static final String TRANSCODED_FILE = "target/sample.mp4";
	
    private IMediaReader reader;
	private OutputFormat outputFormat;
	private String outputFile;
    
	@Before
	public void setup() {
		reader = ToolFactory.makeReader(SOURCE_FILE);
	}
	
	@Test
	public void transcode() {
		outputFile = TRANSCODED_FILE;

		initOutput(Container.MP4, VideoCodec.H264, AudioCodec.AAC, TRANSCODED_FILE);
		testVideoConverter();
	}
	
	@Test
	public void transcodeWithOnlyContainer() {
		initOutput(Container.AVI, "target/sample.avi");
		
		testVideoConverter();
	}

	private void initOutput(Container outputContainer, String outputFileName) {
		// TODO Auto-generated method stub
		initOutput(outputContainer, null, null, outputFileName);
	}

	private void initOutput(Container outputContainer, VideoCodec videoCodec, AudioCodec audioCodec, String outputFileName) {
		if (videoCodec == null && audioCodec == null) {
			outputFormat = new OutputFormat(WIDTH, HEIGHT, BITRATE, outputContainer);
		} else {
			outputFormat = new OutputFormat(WIDTH, HEIGHT, BITRATE, outputContainer, videoCodec, audioCodec);
		}
		outputFile = outputFileName;
	}
	
	private void testVideoConverter() {
		VideoConverter writer = new VideoConverter(outputFile, reader, outputFormat);
		reader.addListener(writer);
		while (reader.readPacket() == null) {
			do {
			} while(false);
		}
		
		VideoFormatVerifier.verifyVideoFormat(outputFormat, new File(outputFile));
	}
}
