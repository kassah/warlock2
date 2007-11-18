package cc.warlock.core.test;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		
		suite.addTest(new JUnit4TestAdapter(StreamTest.class));
		suite.addTest(new JUnit4TestAdapter(SGETest.class));
		suite.addTest(new JUnit4TestAdapter(ServerScriptTest.class));
		return suite;
	}
}
