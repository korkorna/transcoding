package korkorna.examples.transcoding;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TransCodingServiceTest {

	@Mock
	private MediaSourceCopier mediaSourceCopier;
	@Mock
	private Transcoder transcoder;
	@Mock
	private ThumnailExtractor thumnailExtractor;
	@Mock
	private CreatedFileSender createdFileSender;
	@Mock
	private JobResultNotifier jobResultNotifier;

	private TranscodingService trancdoingService;

	@Before
	public void setUp() {
		trancdoingService = new TranscodingService(mediaSourceCopier, transcoder, thumnailExtractor, createdFileSender,
				jobResultNotifier);
	}

	@Test
	public void transcodingSuccessfully() {
		Long jobId = new Long(1);
		File mockMultimediaFile = mock(File.class);
		;
		when(mediaSourceCopier.copy(jobId)).thenReturn(mockMultimediaFile);

		List<File> mockMultimediaFiles = new ArrayList<File>();
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenReturn(mockMultimediaFiles);

		List<File> mockThumnailFiles = new ArrayList<File>();
		when(thumnailExtractor.extract(mockMultimediaFile, jobId)).thenReturn(mockThumnailFiles);

		trancdoingService.transcode(jobId);

		verify(mediaSourceCopier, only()).copy(jobId);
		verify(transcoder, only()).transcode(mockMultimediaFile, jobId);
		verify(thumnailExtractor, only()).extract(mockMultimediaFile, jobId);
		verify(createdFileSender, only()).send(mockMultimediaFiles, mockThumnailFiles, jobId);
		verify(jobResultNotifier, only()).notifyToRequester(jobId);
	}

	public void transcode(Long jobId) {
		// 미디어 원본으로 부터 파일을 로컬에 복사한다.
		File multimediaFile = copyMultimediaSourceTolocal(jobId);

		// 로컬에 복사된 파일을 변환처리한다.
		List<File> multimediaFiles = transcode(multimediaFile, jobId);

		// 로컬에 복사된 파일로 부터 이미지를 추출한다.
		List<File> thumnails = extractThumnail(multimediaFile, jobId);

		// 변환된 결과 파일과 썸네일 이미지를 목적지에 저장
		sendCreatedFileToDestination(multimediaFiles, thumnails, jobId);

		// 결과를 통지
		notifyJobResultToRequester(jobId);
	}

	private void notifyJobResultToRequester(Long jobId) {
		// TODO Auto-generated method stub
		jobResultNotifier.notifyToRequester(jobId);
	}

	private void sendCreatedFileToDestination(List<File> multimediaFiles, List<File> thumnails, Long jobId) {
		// TODO Auto-generated method stub
		createdFileSender.send(multimediaFiles, thumnails, jobId);
	}

	private List<File> extractThumnail(File multimediaFile, Long jobId) {
		// TODO Auto-generated method stub
		return thumnailExtractor.extract(multimediaFile, jobId);
	}

	private List<File> transcode(File multimediaFile, Long jobId) {
		// TODO Auto-generated method stub
		return transcoder.transcode(multimediaFile, jobId);
	}

	private File copyMultimediaSourceTolocal(Long jobId) {
		// TODO Auto-generated method stub
		return mediaSourceCopier.copy(jobId);
	}
}
