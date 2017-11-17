import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */

/**
 * @author nishantrathi
 *
 */
public class TestLogger {

	private static Logger logger = LoggerFactory.getLogger(TestLogger.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logger.info("This is an information message");
		logger.error("this is a error message");
		logger.warn("this is a warning message");



	}

}
