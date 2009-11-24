package dk.bot.bettingengine.statemachine.executor;

import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;
import org.junit.Before;
import org.junit.Test;

/** Testing JEXL expressions
 * 
 * @author daniel
 *
 */
public class JexlTest {

	private JexlContext ctx;
	
	@Before
	public void setUp() {
	ctx = JexlHelper.createContext();	
	}
	
	@Test
	public void test1() throws Exception {
		Expression expr = ExpressionFactory.createExpression("a>0");
		
		ctx.getVars().put("a", 4);	
		System.out.println(expr.evaluate(ctx));
	}
}
