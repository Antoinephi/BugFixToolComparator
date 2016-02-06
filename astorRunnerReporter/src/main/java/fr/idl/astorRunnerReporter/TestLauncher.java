package fr.idl.astorRunnerReporter;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.internal.runners.statements.Fail;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestLauncher {
	
	
	public ClassLoader getClassLoader(String repertoireTest, String repertoireClasse) throws MalformedURLException {
		final URL[] urls = {
				new File(repertoireTest).toURI().toURL(),new File(repertoireClasse).toURI().toURL()};
		return URLClassLoader.newInstance(urls/*, getClass().getClassLoader()*/);
	}
	
	public int runTests(String classeName, String repertoireClasse, String repertoireClasseTest){
		
		try{
			ClassLoader classLoader = getClassLoader(repertoireClasse,repertoireClasseTest);
			JUnitCore junit = new JUnitCore();
			Result results = junit.run(classLoader.loadClass(classeName));
//			for(Failure fail : results.getFailures()){
//				System.out.println(fail.getTrace());
//			}
			return results.getFailures() != null ? results.getFailures().size() : 0;
		}catch(Exception e){
			return -1;
		}
		

	}

}
