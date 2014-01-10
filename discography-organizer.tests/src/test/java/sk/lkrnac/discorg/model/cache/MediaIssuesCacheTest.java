package sk.lkrnac.discorg.model.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import sk.lkrnac.discorg.general.constants.MediaIssueCode;
import sk.lkrnac.discorg.model.interfaces.IMediaIssue;
import sk.lkrnac.discorg.test.utils.CollectionsComparator;

/**
 * Test for class {@link MediaIssuesCache}
 * 
 * @author sitko
 * 
 */
public class MediaIssuesCacheTest {
	private static final int MAX_ISSUES_COUNT = 20;
	private static final String DP_GENERATE_MEDIA_ISSUES = "generateMediaIssues";

	/**
	 * Data provider for various tests.
	 * 
	 * @return test cases
	 */
	@DataProvider(name = DP_GENERATE_MEDIA_ISSUES)
	public final Object[][] generateMediaIssues() {
		//@formatter:off
		return new Object [][]{
			new Object [] { null, null, },
			new Object [] { generateMediaIssues(randomPath(), 0), null, },
			new Object [] { generateMediaIssues(randomPath(), 1), null, },
			new Object [] { generateMediaIssues(randomPath(), MAX_ISSUES_COUNT), null, },
			new Object [] { null, generateMediaIssues(randomPath(), 0) },
			new Object [] { generateMediaIssues(randomPath(), 0), 
					generateMediaIssues(randomPath(), 0) },
			new Object [] { generateMediaIssues(randomPath(), 1), 
					generateMediaIssues(randomPath(), 0) },
			new Object [] { generateMediaIssues(randomPath(), MAX_ISSUES_COUNT), 
					generateMediaIssues(randomPath(), 0) },
			new Object [] { null, generateMediaIssues(randomPath(), 1) },
			new Object [] { generateMediaIssues(randomPath(), 0), 
					generateMediaIssues(randomPath(), 1) },
			new Object [] { generateMediaIssues(randomPath(), 1), 
					generateMediaIssues(randomPath(), 1) },
			new Object [] { generateMediaIssues(randomPath(), MAX_ISSUES_COUNT), 
					generateMediaIssues(randomPath(), 1) },
			new Object [] { null, generateMediaIssues(randomPath(), MAX_ISSUES_COUNT) },
			new Object [] { generateMediaIssues(randomPath(), 0), 
					generateMediaIssues(randomPath(), MAX_ISSUES_COUNT) },
			new Object [] { generateMediaIssues(randomPath(), 1), 
					generateMediaIssues(randomPath(), MAX_ISSUES_COUNT) },
			new Object [] { generateMediaIssues(randomPath(), MAX_ISSUES_COUNT), 
					generateMediaIssues(randomPath(), MAX_ISSUES_COUNT) },
		};
		//@formatter:on
	}

	/**
	 * Generates random path
	 * 
	 * @return random path
	 */
	String randomPath() {
		return RandomStringUtils.randomAlphabetic(MAX_ISSUES_COUNT);
	}

	/**
	 * Generates media issues for testing
	 * 
	 * @param path
	 *            testing path
	 * @param count
	 *            number of issues in collection
	 * @return collection of issues for testing
	 */
	public final Collection<MediaIssue> generateMediaIssues(String path, int count) {
		Collection<MediaIssue> mediaIssues = new ArrayList<>();
		for (int idx = 0; idx < count; idx++) {
			boolean error = idx % 2 == 0 ? true : false;
			String sourceAbsolutePath = idx + "_" + path;
			mediaIssues.add(new MediaIssue(sourceAbsolutePath, MediaIssueCode.values()[idx % 3],
					sourceAbsolutePath, error));
		}
		return mediaIssues;
	}

	/**
	 * Tests methods {@link MediaIssuesCache#getInputMediaIssues()},
	 * {@link MediaIssuesCache#addInputMediaIssue(IMediaIssue)} and
	 * {@link MediaIssuesCache#addReferenceMediaIssue(IMediaIssue)}
	 * 
	 * @param inputMediaIssues
	 *            testing input media issues
	 * @param referenceMediaIssues
	 *            testing reference media issues
	 */
	@Test(dataProvider = DP_GENERATE_MEDIA_ISSUES)
	public void testGetInputMediaIssues(Collection<IMediaIssue> inputMediaIssues,
			Collection<IMediaIssue> referenceMediaIssues) {
		MediaIssuesCache testingObject = initTestingObject(inputMediaIssues, referenceMediaIssues);

		//call testing method
		Collection<IMediaIssue> actualMediaIssues = testingObject.getInputMediaIssues();

		Collection<IMediaIssue> expectedMediaIssues =
				inputMediaIssues == null ? new ArrayList<IMediaIssue>() : inputMediaIssues;

		Comparator<IMediaIssue> mediaIssueComparator = new Comparator<IMediaIssue>() {
			public int compare(IMediaIssue mediaIssue1, IMediaIssue mediaIssue2) {
				return mediaIssue1.getSourceAbsolutePath().compareTo(mediaIssue2.getSourceAbsolutePath());
			}
		};

		int compareResult =
				CollectionsComparator.compare(actualMediaIssues, expectedMediaIssues, mediaIssueComparator,
						true);
		Assert.assertEquals(compareResult, 0);
	}

	/**
	 * Creates and initializes testing object with given issues
	 * 
	 * @param inputMediaIssues
	 *            testing input media issues
	 * @param referenceMediaIssues
	 *            testing reference media issues
	 * @return testing object
	 */
	MediaIssuesCache initTestingObject(Collection<IMediaIssue> inputMediaIssues,
			Collection<IMediaIssue> referenceMediaIssues) {
		MediaIssuesCache testingObject = new MediaIssuesCache();
		if (inputMediaIssues != null) {
			for (IMediaIssue mediaIssue : inputMediaIssues) {
				testingObject.addInputMediaIssue(mediaIssue);
			}
		}
		if (referenceMediaIssues != null) {
			for (IMediaIssue mediaIssue : referenceMediaIssues) {
				testingObject.addReferenceMediaIssue(mediaIssue);
			}
		}
		return testingObject;
	}
}
