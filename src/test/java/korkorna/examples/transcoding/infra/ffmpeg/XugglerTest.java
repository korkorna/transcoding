package korkorna.examples.transcoding.infra.ffmpeg;

import org.junit.Test;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

public class XugglerTest {

	@Test
	public void transcode() {
		// create a media reader
		IMediaReader reader = ToolFactory.makeReader("src/test/resources/sample.avi");
		
		// create a media write
		IMediaWriter writer = ToolFactory.makeWriter("target/sample.mp4", reader);
		
		// add a write to the reader, to create the output file
		reader.addListener(writer);
		
		// read and decode packets from the source file and
		// dispatch decoded audio and video to the writer
		while(reader.readPacket() == null) {
			do {
			} while(false);
		}
		
		reader.setCloseOnEofOnly(false);
		
		writer.setForceInterleave(false);
		
		System.out.println("closed...");
	}
	
	@Test
	public void getMetadataOfExistingAVIFile() {
		IContainer container = IContainer.make();
		int openResult = container.open("src/test/resources/sample.avi", IContainer.Type.READ, null);
		if (openResult < 0) {
			throw new RuntimeException("Xuggler file open failed: " + openResult);
		}
		int numStreams = container.getNumStreams();
		System.out.printf("file \"%s\": %d stream%s; ",
	                "src/test/resources/sample.avi", numStreams,
	                numStreams == 1 ? "" : "s");
	    System.out.printf("bit rate: %d; ", container.getBitRate());
	    System.out.printf("\n");
	    
	    for(int i = 0; i < numStreams; i++) {
	    	IStream stream = container.getStream(i);
	    	IStreamCoder coder = stream.getStreamCoder();
	    	
            System.out.printf("stream %d: ", i);
            System.out.printf("type: %s; ", coder.getCodecType());
            System.out.printf("codec: %s; ", coder.getCodecID());
            System.out.printf("duration: %s; ",
                    stream.getDuration() == Global.NO_PTS ? "unknown" : ""
                            + stream.getDuration());
            System.out.printf("start time: %s; ",
                    container.getStartTime() == Global.NO_PTS ? "unknown" : ""
                            + stream.getStartTime());
            System.out.printf("timebase: %d/%d; ", stream.getTimeBase()
                    .getNumerator(), stream.getTimeBase().getDenominator());
            System.out.printf("coder tb: %d/%d; ", coder.getTimeBase()
                    .getNumerator(), coder.getTimeBase().getDenominator());
            
            if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
            	System.out.printf("sample reate: %d", coder.getSampleRate());
            	System.out.printf("channels: %d", coder.getChannels());
            	System.out.printf("format: %s", coder.getSampleFormat());
            } else if(coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
            	System.out.printf("width: %d", coder.getWidth());
            	System.out.printf("height: %d", coder.getHeight());
            	System.out.printf("format: %s", coder.getPixelType());
            	System.out.printf("frame-rate: %5.2f", coder.getFrameRate().getDouble());
            }
            System.out.print("\n");
	    }
	    
	    container.close();
	      
	}
}
