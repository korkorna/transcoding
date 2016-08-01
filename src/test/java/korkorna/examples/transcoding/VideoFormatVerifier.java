package korkorna.examples.transcoding;

import static org.junit.Assert.assertEquals;

import java.io.File;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

public class VideoFormatVerifier {

	public static void verifyVideoFormat(OutputFormat expectedFormat, File videoFile) {
		new VideoFormatVerifier(expectedFormat, videoFile).verify();
	}

    private IContainer container;
    private int width;
    private int height;
    private ICodec.ID videoCodec;
    private ICodec.ID audioCodec;

    private OutputFormat expectedFormat;
	private File videoFile;

	public VideoFormatVerifier(OutputFormat expectedFormat, File videoFile) {
		this.expectedFormat = expectedFormat;
		this.videoFile = videoFile;
	}
	
	private void verify() {
		// TODO Auto-generated method stub
		try {
			makeContainer();
			extractMetainfoOfVideo();
			assertVideoFile();
		} finally {
			// TODO: handle exception
			closeContainer();
		}
	}

	private void closeContainer() {
		// TODO Auto-generated method stub
		if (container != null) {
			container.close();
		}
	}

	private void assertVideoFile() {
		// TODO Auto-generated method stub
//		assertEquals(outputFormat.getWidth(), width);
//		assertEquals(outputFormat.getHeight(), height);
//		assertEquals(outputFormat.getVideoCodec(), videoCodec.toString());
//		assertEquals(outputFormat.getAudioCodec(), audioCodec.toString());
		
	}

	private void extractMetainfoOfVideo() {
		// TODO Auto-generated method stub
		int numStreams = container.getNumStreams();
		for (int i = 0; i < numStreams; i++) {
			IStream stream = container.getStream(i);
			IStreamCoder coder = stream.getStreamCoder();
			
			if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
				audioCodec = coder.getCodecID();
			} else if (coder.getCodecType()  == ICodec.Type.CODEC_TYPE_VIDEO) {
				videoCodec = coder.getCodecID();
				width = coder.getWidth();
				height = coder.getHeight();
			}
		}
	}

	private void makeContainer() {
		// TODO Auto-generated method stub
		container = IContainer.make();
		int openReult = container.open(videoFile.getAbsolutePath(), IContainer.Type.READ, null);
		if (openReult < 0) {
			throw new RuntimeException("Xuggler file open failed "+ openReult);
		}
	}
	
	
}
