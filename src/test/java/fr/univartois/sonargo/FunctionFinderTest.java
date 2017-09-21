/**
 * 
 */
package fr.univartois.sonargo;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import fr.univartois.sonargo.gotest.FunctionFinder;

import static org.junit.Assert.assertEquals;

/**
 * @author thibault
 *
 */
public class FunctionFinderTest extends AbstractSonarTest {
    @Before
    @Override
    public void init() {
	init(TestUtils.getDefaultFileSystem());
    }

    @Test
    public void testSearchInFile() {
	try {
	    FunctionFinder f = new FunctionFinder(testerContext);
	    assertEquals("TestAverage", f.searchIn("func TestAverage(t *testing.T)"));
	    assertEquals("TestGutterBalls", f.searchIn("func TestGutterBalls(t *testing.T)"));
	    assertEquals("TestOnePinOnEveryThrow", f.searchIn("func TestOnePinOnEveryThrow(t *testing.T) {"));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

}
