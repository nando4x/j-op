package com.nandox.jop.core.processor;

import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.jsoup.nodes.Element;

import com.nandox.jop.core.ErrorsDefine;
import com.nandox.jop.core.context.BeanMonitoring;
import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.expression.AbstractPageExpression;
import com.nandox.jop.core.processor.expression.PageExpression;
import com.nandox.jop.core.processor.expression.SimplePageExpression;

/**
 * Standard parser.<p> 
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    Parser.java
 * 
 * @date      04 ott 2016 - 04 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class Parser {
	public static final String JOP_EXPR_INI = "{";
	public static final String JOP_EXPR_END = "}";

	private Map<String,PageExpression> exprs;		// list of all expressions [id, expression created]
	private Map<String,Class<?>> vars_definition;	// list of block variables [variable name, variable java class]
	
	/**
	 * Constructor
	 * @param	  Exprs	expression map: [id, expression created]
	 * @param	  VarsDefinition	variables map: [variable name, variable java class]
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */	
	public Parser(Map<String,PageExpression> Exprs, Map<String,Class<?>> VarsDefinition) {
		this.exprs = Exprs;
		this.vars_definition = VarsDefinition;
	}
	
	/**
	 * Expression parser to interpreter and compile an expression.
	 * @param	  Context	Application context
	 * @param	  Code		expression code
	 * @param	  BlockJd	identifier of block that contains this expression
	 * @param	  ExpType	Class of expression type
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 * @return	  
	 */	
	public PageExpression expressionParser(WebAppContext Context, String Code, JopId BlockId, Class<?> ExpType ) throws DomException {
		//TODO: gestire macanza di exprs,vars e BlockId per skippare monitor
		BeanMonitoring mon = Context.getBeanMonitor(); // get bean monitor
		PageExpression exp = null;
		String code = this.parseJavaExpression(Code);
		if ( this.exprs == null || !this.exprs.containsKey(AbstractPageExpression.computeId(code)) ) {
			// for a new expression add it and register this block to those to refresh 
			try {
				exp = (PageExpression)ExpType.getDeclaredConstructor(WebAppContext.class,String.class,Map.class).newInstance(Context,code,this.vars_definition);
				//exp = new SimplePageExpression(Context,code,this.vars_definition);
				if ( this.exprs != null ) 
					this.exprs.put(exp.getId(), exp);
				if ( BlockId != null )
					mon.registerRefreshable(exp.getBeansList(), BlockId);
			} catch (Exception e) {
				throw (DomException)e.getCause();
			}
		} else
			exp = this.exprs.get(AbstractPageExpression.computeId(code));
		return exp;
	}
	/**
	 * Expression parser to interpreter and compile an expression of type SimplePageExpression
	 * @param	  Context	Application context
	 * @param	  Code		expression code
	 * @param	  BlockJd	identifier of block that contains this expression
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 * @return	  
	 */	
	public PageExpression expressionParser(WebAppContext Context, String Code, JopId BlockId ) throws DomException {
		return this.expressionParser(Context, Code, BlockId, SimplePageExpression.class );
	}

	/**
	 * Parse code of java expression to make a syntax check and clean it 
	 * @param	  Code		expression code
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 * @return		pure java code  	  
	 */	
	public String parseJavaExpression (String code) throws DomException {
		if ( !code.isEmpty() ) {
			if ( code.trim().indexOf("java"+JOP_EXPR_INI) >= 0 ) {
				if ( code.lastIndexOf(JOP_EXPR_END) > 0 ) {
					String bid = code.substring(code.indexOf(JOP_EXPR_INI),code.trim().lastIndexOf(JOP_EXPR_END)+1);
					return bid;
				} else
					throw new DomException(ErrorsDefine.JOP_EXPR_SYNTAX);
			}
		}
		return null;
	}
	/**
	 * Parse variables to make a syntax check and clean it 
	 * @param	  Code		expression code
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 * @return		pure java code  	  
	 */	
	public Map<String,Class<?>> parseVariables(Element Node) {
		Map<String,Class<?>> ret = new HashMap<String,Class<?>>();
		StringTokenizer st = new StringTokenizer(Node.attr("jop_var").trim(),";");
		while ( st.hasMoreTokens() ) {
			Class<?> cl = null;
			String vname = st.nextToken();
			if ( vname.trim().startsWith("(") && vname.indexOf(")") > 0 ) {
				try {
					cl = this.getClass().getClassLoader().loadClass(vname.substring(vname.indexOf("(")+1, vname.indexOf(")")).trim());
					vname = vname.substring(vname.indexOf(")")+1).trim();
					ret.put(vname, cl);
				} catch (Exception e) {
					throw new RuntimeException(e.toString() + ": " + e.getMessage());
				}
			} else {
				ret.put(vname, null);
			}
		}
		return ret;
	}
	
}
