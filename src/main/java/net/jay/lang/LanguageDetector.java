package net.jay.lang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
public class LanguageDetector {
	
	public List<String> detectLanguage(List<String> langFiles, String mysteryFile) {
		validate(langFiles, mysteryFile);
		File mf = new File(mysteryFile);
		List<String> readFile = readFile(mf);
		Map<String, Integer> similarWordCounts = getSimilarWordCounts(readFile, langFiles);
		return findMostMatched(similarWordCounts);
	}
	
	private void validate(List<String> langFiles, String mysteryFile) {
		if(!Validator.validateFileExists(mysteryFile)) {
			throw new ValidationException(String.format("File '%s' does not exists", mysteryFile));
		}
		for (String fileName : langFiles) {
			if(!Validator.validateFileExists(fileName)) {
				throw new ValidationException(String.format("File '%s' does not exists", fileName));
			}
		}
	}
	
	private List<String> findMostMatched(Map<String, Integer> counts) {
		List<String> matched = new ArrayList<String>();
		Integer max = Collections.max(counts.values());
		for (Entry<String, Integer> e : counts.entrySet()) {
			if(e.getValue().equals(max))
				matched.add(e.getKey());
		}
		return matched;
	}
	
	private Map<String, Integer> getSimilarWordCounts(List<String> tokens, List<String> lang_files) {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		for (String string : lang_files) {
			int similarWordCount = getSimilarWordCount(tokens, new File(string));
			counts.put(string, similarWordCount);
		}
		return counts;
	}
	
	private int getSimilarWordCount(List<String> tokens, File file) {
		List<String> readFile2 = readFile(file);
		Collection<String> intersection = CollectionUtils.intersection(tokens, readFile2);
		return intersection.size();
	}
	
	private List<String> readFile(File file) {
		List<String> tokens = new ArrayList<String>();
		
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = null;
			while((line = br.readLine()) != null) {
				tokens.addAll(tokanizeString(line));
			}
		} catch (IOException e) {
			throw new RuntimeException(String.format("Error reading file '%s'", file.getName()));
		}
			
		return tokens;
	}
	
	private List<String> tokanizeString(String line) {
		List<String> lineList = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(line, " .;:,");
		while (st.hasMoreElements()) {
			lineList.add(st.nextToken().toLowerCase());
		}
		return lineList;
	}
}
