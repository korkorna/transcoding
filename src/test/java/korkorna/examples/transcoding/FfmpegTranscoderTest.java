package korkorna.examples.transcoding;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

public class FfmpegTranscoderTest {

	private static final int WIDTH = 160;
    private static final int HEIGHT = 120;
    private static final int BITRATE = 150;
    private static final String SOURCE_FILE = "src/test/resources/sample.avi";
    private static final String TRANSCODED_FILE = "target/sample.mp4";
	
	private Transcoder transcorder;
	private File multimediaFile;
	private List<OutputFormat> outputFormats;
	
	private OutputFormat mp4Format;
	private OutputFormat aviFormat;
	
	@Before
	public void setup() {
		outputFormats = new ArrayList<OutputFormat>();
		mp4Format = new OutputFormat(WIDTH, HEIGHT, BITRATE, Container.MP4, VideoCodec.H264, AudioCodec.AAC);
		aviFormat = new OutputFormat(WIDTH, HEIGHT, BITRATE, Container.AVI, VideoCodec.MPEG4, AudioCodec.MP3);
		multimediaFile = new File(SOURCE_FILE);
		
		transcorder = new FfmpegTranscoder();
	}
	
	@Test
	public void transcodeWithOneMp4OutputFormat() {
		outputFormats.add(mp4Format);
		executeTranscoderAndAssert();
	}
	
	@Test
	public void transcodeWithOneAviOutputFormat() {
		outputFormats.add(aviFormat);
		executeTranscoderAndAssert();
	}
	
	private void executeTranscoderAndAssert() {
		// TODO Auto-generated method stub
		List<File> transcodedFiles = transcorder.transcode(multimediaFile, outputFormats);
		assertEquals(1, transcodedFiles.size());
		assertTrue(transcodedFiles.get(0).exists());
		
		VideoFormatVerifier.verifyVideoFormat(outputFormats.get(0), transcodedFiles.get(0));
	}

}
