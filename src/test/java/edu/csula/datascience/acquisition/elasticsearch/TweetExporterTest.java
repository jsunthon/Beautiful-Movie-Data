package edu.csula.datascience.acquisition.elasticsearch;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class TweetExporterTest {
	@Test
	public void vaidateSentimentTxtFile() {
		List<String> positiveWords = new ArrayList<>();
		List<String> negativeWords = new ArrayList<>();
		try {
			positiveWords = Files.readAllLines(Paths.get(ClassLoader.getSystemResource("positive-words.txt").toURI()));
			negativeWords = Files.readAllLines(Paths.get(ClassLoader.getSystemResource("negative-words.txt").toURI()));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		assertTrue(positiveWords.size() > 0 && negativeWords.size() > 0);
		assertEquals("a+", positiveWords.get(0));
		assertEquals("2-faced", negativeWords.get(0));
	}
}
