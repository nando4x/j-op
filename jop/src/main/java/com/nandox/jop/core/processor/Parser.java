package com.nandox.jop.core.processor;

import java.util.Map;

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
	
	public Parser(Map<String,PageExpression> Exprs, Map<String,Class<?>> VarsDefinition) {
		this.exprs = Exprs;
		this.vars_definition = VarsDefinition;
	}
	
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
	public PageExpression expressionParser(WebAppContext Context, String Code, JopId BlockId ) throws DomException {
		return this.expressionParser(Context, Code, BlockId, SimplePageExpression.class );
	}

	// Parse expression check syntax and extract string of expression
	//
	//
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
}
