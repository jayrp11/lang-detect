package net.jay.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx = SpringApplication.run(Main.class, args);
		
		Scanner in = new Scanner(System.in);
		System.out.print("Enter number of lang files (excluding file to be detected) : ");
		int noOfFiles = in.nextInt();
		ArrayList<String> langFiles = new ArrayList<String>();
		for(int i = 0; i < noOfFiles; i++) {
			System.out.print(String.format("Enter lang file name %d or %d : ", i+1, noOfFiles));
			langFiles.add(in.next());
		}
		System.out.print("Enter name of the file that will be detected : ");
		String mysteryFile = in.next();
		in.close();
		
		LanguageDetector ld = ctx.getBean(LanguageDetector.class);
		try {
			List<String> matched = ld.detectLanguage(langFiles, mysteryFile);
			System.out.println("Detected language : ");
			for (String string : matched) {
				System.out.println(string);
			}
		} catch(ValidationException e) {
			logger.error(String.format("Validation error '%s'", e.getMessage()));
		} catch (RuntimeException e) {
			logger.error(String.format("Runtime exception '%s'", e.getMessage()));
		}
	}
}
