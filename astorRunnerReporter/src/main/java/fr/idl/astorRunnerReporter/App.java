package fr.idl.astorRunnerReporter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import fr.inria.main.evolution.AstorMain;

/**
 * Hello world!
 *
 */
public class App 
{
	
	private static final String WHITEBOX_TEST = "WhiteboxTest";
	private static final String BLACKBOX_TEST = "BlackboxTest";
	private static final String FILE_RESULT_NAME ="result-astor.txt";
	private static final String PACKAGE ="introclassJava";
	private static final String MAVEN_CLASS_REPERTOIRE = "target/classes";
	private static final String MAVEN_CLASS_TEST_REPERTOIRE = "target/test-classes";

	
	private static List<String> findJavaFiles(String path){
		File[] files = new File(path).listFiles();
		List<String> pathToSources = new ArrayList<String>();
		for(File f : files){
			if(f.isDirectory())
				pathToSources.addAll(findJavaFiles(f.getAbsolutePath()));
			else if(f.getName().endsWith(".java")){
				pathToSources.add(f.getAbsolutePath());
			}
		}
		
		return pathToSources;
	}
	public static String getTestClassNameFromProject(String sourcePath, String typeTest){
		List<String> tests = findJavaFiles(sourcePath);
		for(String test : tests){
			if(test.contains(typeTest)){
				return convertToClassName(test);
			}
		}
		
		return null;
	}
	
	public static String getWhiteTestClassNameFromProject(String sourcePath){
		return getTestClassNameFromProject(sourcePath, WHITEBOX_TEST);
	}
	
	public static String getBlackTestClassNameFromProject(String sourcePath){
		return getTestClassNameFromProject(sourcePath, BLACKBOX_TEST);
	}
	
	private static String convertToClassName(String file) {
		String pattern = Pattern.quote(File.separator);
		int ind = file.split(pattern).length;
		return file.split(pattern)[ind -1].replace(".java", "");
	}
	
    public static void main( String[] args ) throws Exception
    {
    	if(args.length < 1){
    		System.out.println("Vous devez indiquer le répertoire du projet");
    		return;
    	}
    	String projectPath =args[0];
    	if(!projectPath.endsWith("/")){
    		projectPath+="/";
    	}
		TestLauncher testLauncher = new TestLauncher();

		String repertoireClasseTestName = projectPath+MAVEN_CLASS_TEST_REPERTOIRE;
		String repertoireClasseName = projectPath+MAVEN_CLASS_REPERTOIRE;

		String whiteTestCurrent = PACKAGE+"."+getWhiteTestClassNameFromProject(projectPath);
		String blackTestCurrent = PACKAGE+"."+getBlackTestClassNameFromProject(projectPath);

		int nbrBlackFailInit = testLauncher.runTests(whiteTestCurrent,repertoireClasseName,repertoireClasseTestName);
		int nbrWhiteFailInit = testLauncher.runTests(blackTestCurrent,repertoireClasseName,repertoireClasseTestName);
		System.out.println("B["+nbrBlackFailInit+"] W["+nbrWhiteFailInit+"]");
		
		String[] AstorArgs = { "-location", projectPath, "-dependencies", "/home/once/Documents/wk-spoon/astor_exec/junit-4.11.jar", "-failing", whiteTestCurrent+":"+blackTestCurrent};

		AstorMain.main(AstorArgs);
    }
}
