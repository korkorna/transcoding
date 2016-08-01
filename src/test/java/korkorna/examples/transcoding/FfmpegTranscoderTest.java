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
	
	@Before
	public void setup() {
		transcorder = new FfmpegTranscoder();
	}
	
	@Test
	public void transcodeWithOnfOutputFormat() {
		File multisourceFile = new File(SOURCE_FILE);
		List<OutputFormat> outputFormats = new ArrayList<OutputFormat>();
		outputFormats.add(new OutputFormat(WIDTH, HEIGHT, BITRATE, VideoCodec.H264, AudioCodec.AAC));
		List<File> transcodedFiles = transcorder.transcode(multisourceFile, outputFormats);
		assertEquals(1, transcodedFiles.size());
		assertTrue(transcodedFiles.get(0).exists());
		
		VideoFormatVerifier.verifyVideoFormat(outputFormats.get(0), transcodedFiles.get(0));
	}

	private void verifyTranscodedFile(OutputFormat outputFormat, File file) {
		// TODO Auto-generated method stub
		IContainer container = IContainer.make();
		int openResult = container.open(file.getAbsolutePath(), IContainer.Type.READ, null);
		
		if (openResult < 0) {
			throw new RuntimeException("Xuggler file open failed " + openResult);
		}
		
		int numStreams = container.getNumStreams();
		
		int width = 0;
		int height = 0;
		ICodec.ID videoCodec = null;
		ICodec.ID audioCodec = null;
		
		for (int i = 0; i < numStreams; i++) {
			IStream stream = container.getStream(i);
			IStreamCoder coder = stream.getStreamCoder();
			
			if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
				audioCodec = coder.getCodecID();
			} else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
				videoCodec = coder.getCodecID();
				width = coder.getWidth();
				height = coder.getHeight();
			}
		}
		
		container.close();
		
		assertEquals(outputFormat.getWidth(), width);
		assertEquals(outputFormat.getHeight(), height);
		assertEquals(outputFormat.getVideoCodec(), videoCodec.toString());
		assertEquals(outputFormat.getAudioCodec(), audioCodec.toString());
		
	}
	
}
