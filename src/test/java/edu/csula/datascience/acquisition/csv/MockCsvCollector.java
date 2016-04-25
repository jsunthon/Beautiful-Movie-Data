package edu.csula.datascience.acquisition.csv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import edu.csula.datascience.acquisition.Movie;
import edu.csula.datascience.acquisition.csv.Collector;

/**
 * A mock implementation of collector for testing
 */
public class MockCsvCollector implements Collector<MovieModel, MockCsvData> {
	String title = "";

	@Override
	public Collection<MovieModel> mungee(Collection<MockCsvData> src) {
		
		//unclean
		List<MovieModel> newSrc = src.stream().map(MovieModel::build).collect(Collectors.toList());
		System.out.println("size: " + newSrc.size());
		Collection<MovieModel> finalMovieEntry = new ArrayList();
		double averageRating = 0.0;
		title = newSrc.get(0).getTitle();
		int year = 0;

		// regex found on stackoverflow
		Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(title);
		title = replaceLast(title, "\\(.*?\\) ?", "");
		while (m.find()) {
			try {
				year = Integer.parseInt(m.group(1));
			} catch (Exception e) {
				// if there is a parenthesis in the title
			}
		}
		if (isValidTitle(title)) {
			MovieModel newMovie = new MovieModel(newSrc.get(0).getId(), title);
			for (MovieModel movie : newSrc) {
				averageRating += movie.getRating();
			}
			averageRating = averageRating / (double) src.size();
			newMovie.setRating(averageRating);
			newMovie.setYear(year);
			finalMovieEntry.add(newMovie);
		}
		return finalMovieEntry;
	}

	@Override
	public void save(Collection<MovieModel> data) {
	}

	// regex helper methods from StackOverflow
	private static boolean isValidTitle(String title) {
		boolean valid = (title.matches("^[a-zA-Z0-9_() ]*$") && (title != null)
				&& (!title.replaceAll("\\s+", "").isEmpty()));
		return valid;
	}

	private static String replaceLast(String text, String regex, String replacement) {
		return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
	}
}
