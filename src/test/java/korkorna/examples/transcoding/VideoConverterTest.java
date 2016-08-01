package korkorna.examples.transcoding;

import java.io.File;

import org.junit.Test;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;

public class VideoConverterTest {
	private static final int WIDTH = 160;
    private static final int HEIGHT = 120;
    private static final int BITRATE = 150;
    private static final String SOURCE_FILE = "src/test/resources/sample.avi";
    private static final String TRANSCODED_FILE = "target/sample.mp4";
    
	@Test
	public void transcode() {
		IMediaReader reader = ToolFactory.makeReader(SOURCE_FILE);

		OutputFormat outputFormat = new OutputFormat(WIDTH, HEIGHT, BITRATE, VideoCodec.H264, AudioCodec.AAC);
		
		VideoConverter writer = new VideoConverter(TRANSCODED_FILE, reader, outputFormat);
		reader.addListener(writer);
		while (reader.readPacket() == null) {
			do {
			} while(false);
		}
		
		VideoFormatVerifier.verifyVideoFormat(outputFormat, new File(TRANSCODED_FILE));
	}
}
