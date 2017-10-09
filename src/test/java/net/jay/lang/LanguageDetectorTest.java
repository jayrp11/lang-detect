package net.jay.lang;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@PrepareForTest({LanguageDetector.class,Validator.class})
public class LanguageDetectorTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testDetectLanguage() throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		URL testFile = classLoader.getResource("TEST.txt");
		URL french1 = classLoader.getResource("FRENCH.1");
		URL french2 = classLoader.getResource("FRENCH.2");
		
		LanguageDetector ld = new LanguageDetector();
		List<String> langs = ld.detectLanguage(Arrays.asList(french1.getPath(), french2.getPath()), testFile.getPath());
		assertEquals(Arrays.asList(french2.getPath()), langs);
	}
	
	@Test
	public void testException1() {
		this.thrown.expect(ValidationException.class);
		this.thrown.expectMessage("File 'T' does not exists");
		LanguageDetector ld = new LanguageDetector();
		ld.detectLanguage(Arrays.asList("F1", "F2"), "T");
	}
	
	@Test
	public void testException2() {
		this.thrown.expect(ValidationException.class);
		this.thrown.expectMessage("File 'F1' does not exists");
		ClassLoader classLoader = getClass().getClassLoader();
		URL testFile = classLoader.getResource("TEST.txt");
		this.thrown.expect(ValidationException.class);
		LanguageDetector ld = new LanguageDetector();
		ld.detectLanguage(Arrays.asList("F1", "F2"), testFile.getPath());
	}
}
