package spell;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import server.Facade;
import shared.communication.GetProjects_Params;
import shared.communication.GetProjects_Result;
import shared.model.Project;
import dataimporter.DataImporter;

public class SpellCheckTests {

	@Before
	public void setup() throws Exception {
		DataImporter importer = new DataImporter();
		importer.importData("/users/guest/y/youngj4/Desktop/Records/Records.xml");
	}
	
	@Test
	public void test_1() throws Exception {
		
		String dictionaryFileName = "http://localhost:8081/knowndata/ethnicities.txt";
		String inputWord = "alaskanative";
		
		System.out.println(dictionaryFileName + "  " + inputWord);
		
		MySpellCorrector corrector = new MySpellCorrector();
		
		corrector.useDictionary(dictionaryFileName);
		List<String> suggestions = corrector.suggestSimilarWords(inputWord);
		
		assertEquals(suggestions.size(), 1);
		
		assertEquals(suggestions.get(0), "alaska native");
	}
	
	@Test
	public void test_2() throws Exception {
		
		
		MySpellCorrector corrector = new MySpellCorrector();
		
		corrector.useDictionary("http://localhost:8081/knowndata/1890_first_names.txt");
		List<String> suggestions = corrector.suggestSimilarWords("au");
		
		assertEquals(suggestions.size(), 17);
		
		assertEquals(suggestions.get(0), "abe");
		
		assertEquals(suggestions.get(5), "bud");
		
	}

	public static void main(String[] args) {
		
		String[] testClasses = new String[] {
				"spell.SpellCheckTests"
		};

		org.junit.runner.JUnitCore.main(testClasses);
	}
}
