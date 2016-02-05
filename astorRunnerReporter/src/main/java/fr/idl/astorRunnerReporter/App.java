package fr.idl.astorRunnerReporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import fr.inria.main.evolution.AstorMain;

/**
 * Hello world!
 *
 */
public class App {

	private static final String WHITEBOX_TEST = "WhiteboxTest";
	private static final String BLACKBOX_TEST = "BlackboxTest";
	private static final String FILE_RESULT_NAME = "result-astor.txt";
	private static final String FAIL_FILE_RESULT_NAME = "fail-result-astor.txt";
	private static final String PACKAGE = "introclassJava";
	private static final String MAVEN_CLASS_REPERTOIRE = "target/classes";
	private static final String MAVEN_CLASS_TEST_REPERTOIRE = "target/test-classes";
	private static final String OUTPUT_REPERTOIRE = "outputMutation";

	private static final String INPUT_DATASET = File.separator + "home" + File.separator + "once" + File.separator + "Documents" + File.separator + "wk-spoon" + File.separator + "IntroClassJava" + File.separator + "dataset";
	private static final String INPUT_DATASET_CHECKSUM = INPUT_DATASET + File.separator + "checksum";
	private static final String INPUT_DATASET_DIGITS = INPUT_DATASET + File.separator + "digits";
	private static final String INPUT_DATASET_GRADE = INPUT_DATASET + File.separator + "grade";
	private static final String INPUT_DATASET_MEDIAN = INPUT_DATASET + File.separator + "median";
	private static final String INPUT_DATASET_SMALLEST = INPUT_DATASET + File.separator + "smallest";
	private static final String INPUT_DATASET_SYLLABLES = INPUT_DATASET + File.separator + "syllables";

	private static List<String> findJavaFiles(String path) {
		File[] files = new File(path).listFiles();
		List<String> pathToSources = new ArrayList<String>();
		for (File f : files) {
			if (f.isDirectory())
				pathToSources.addAll(findJavaFiles(f.getAbsolutePath()));
			else if (f.getName().endsWith(".java")) {
				pathToSources.add(f.getAbsolutePath());
			}
		}

		return pathToSources;
	}

	private static List<String> findProjectFolder(String path) {
		File[] files = new File(path).listFiles();
		List<String> pathToSources = new ArrayList<String>();
		for (File f : files) {
			if (f.isDirectory() && !f.getName().equals("src"))
				pathToSources.addAll(findProjectFolder(f.getAbsolutePath()));
			else if (f.isDirectory())
				pathToSources.add(f.getParentFile().getAbsolutePath());
		}

		return pathToSources;
	}

	private static boolean checkFixFound(String path) {
		File[] files = new File(path).listFiles();
		for (File f : files) {
			if (f.isDirectory() && f.getName().endsWith("src")) {
				File[] filesrc = f.listFiles();
				for (File fsrc : filesrc) {
					if (!(fsrc.getName().endsWith("default"))) {
						return true;
					}
				}
				return false;
			} else if (f.isDirectory() && f.getName().endsWith("bin")) {
				continue;
			} else {
				return checkFixFound(f.getAbsolutePath());
			}
		}
		return false;
	}

	public static String getTestClassNameFromProject(String sourcePath, String typeTest) {
		List<String> tests = findJavaFiles(sourcePath);
		for (String test : tests) {
			if (test.contains(typeTest)) {
				return convertToClassName(test);
			}
		}

		return null;
	}

	public static String getWhiteTestClassNameFromProject(String sourcePath) {
		return getTestClassNameFromProject(sourcePath, WHITEBOX_TEST);
	}

	public static String getBlackTestClassNameFromProject(String sourcePath) {
		return getTestClassNameFromProject(sourcePath, BLACKBOX_TEST);
	}

	private static void deleteOutPutFolder() {
		deleteClassFiles(OUTPUT_REPERTOIRE);

	}

	private static void deleteClassFiles(String path) {
		File folder = new File(path);

		File[] files = folder.listFiles();
		if (files != null) {
			for (File f : files) {
				if (!f.isDirectory())
					f.delete();
				else if (f.isDirectory())
					deleteClassFiles(f.getAbsolutePath());
			}
		}
		if (folder != null) {
			folder.delete();
		}
	}

	private static String convertToClassName(String file) {
		String pattern = Pattern.quote(File.separator);
		int ind = file.split(pattern).length;
		return file.split(pattern)[ind - 1].replace(".java", "");
	}

	private static void addLigneToResult(String ligne, String filename) {
		FileWriter dw = null;
		try {
			dw = new FileWriter(filename, true);
			dw.write(ligne);
			dw.flush();
			dw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				dw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {

		int nbrProjectOnlyBlackFix = 0;
		int nbrProjectOnlyWhiteFix = 0;
		int nbrProjectWithBlackAndWhiteFix = 0;
		int nbrProject = 0;
		int nbrProjetWithoutFail =0;
		for (String folder : findProjectFolder(INPUT_DATASET_CHECKSUM)) {
			if (!folder.endsWith("/")) {
				folder += "/";
			}
//    	String folder =args[0];
//    	if(!folder.endsWith("/")){
//    		folder+="/";
//    	}
			nbrProject += 1;
			TestLauncher testLauncher = new TestLauncher();

			String repertoireClasseTestName = folder + MAVEN_CLASS_TEST_REPERTOIRE;
			String repertoireClasseName = folder + MAVEN_CLASS_REPERTOIRE;

			String whiteTestCurrent = PACKAGE + "." + getWhiteTestClassNameFromProject(folder);
			String blackTestCurrent = PACKAGE + "." + getBlackTestClassNameFromProject(folder);

			int nbrBlackFailInit = testLauncher.runTests(whiteTestCurrent, repertoireClasseName,
					repertoireClasseTestName);
			int nbrWhiteFailInit = testLauncher.runTests(blackTestCurrent, repertoireClasseName,
					repertoireClasseTestName);

			String ClasseTestFail="";
			if(nbrBlackFailInit < 1 && nbrBlackFailInit < 1){
				nbrProjetWithoutFail+=1;
				continue;
			}
			
			if(nbrWhiteFailInit > 0 ){
				ClasseTestFail+=whiteTestCurrent;
			}
			if(nbrBlackFailInit > 0){
				if(!ClasseTestFail.equals("")){
					ClasseTestFail+=":";
				}
				ClasseTestFail+=blackTestCurrent;
			}
			
			String[] AstorArgs = { "-location", folder, "-package", "introclassJava", "-dependencies",
					"/home/once/Documents/wk-spoon/astor_exec/junit-4.11.jar", "-failing",
					ClasseTestFail };

			AstorMain.main(AstorArgs);

			if (nbrBlackFailInit > 0 || nbrWhiteFailInit > 0) {
				if (checkFixFound(OUTPUT_REPERTOIRE)) {
					if (nbrWhiteFailInit == 0 && nbrBlackFailInit > 0) {
						nbrProjectOnlyBlackFix += 1;
					} else if (nbrWhiteFailInit > 0 && nbrBlackFailInit == 0) {
						nbrProjectOnlyWhiteFix += 1;
					} else if (nbrWhiteFailInit > 0 && nbrBlackFailInit > 0) {
						nbrProjectWithBlackAndWhiteFix += 1;
					}
					addLigneToResult(
							"FIXED B[" + nbrBlackFailInit + "] W[" + nbrWhiteFailInit + "] " + folder + "\n",
							FILE_RESULT_NAME);
				} else {
					addLigneToResult(
							"fail to fix B[" + nbrBlackFailInit + "] W[" + nbrWhiteFailInit + "] " + folder+ "\n",
							FAIL_FILE_RESULT_NAME);
				}
			}
			deleteOutPutFolder();
		}
		
		addLigneToResult(
				"project[" + nbrProject + "]project without fail["+nbrProjetWithoutFail+ "] Fixed OnlyBlack[" + nbrProjectOnlyBlackFix + "] OnlyWhite["
						+ nbrProjectOnlyWhiteFix + "] whiteAndBlack[" + nbrProjectWithBlackAndWhiteFix + "]\n",
				FILE_RESULT_NAME);

	}

}
