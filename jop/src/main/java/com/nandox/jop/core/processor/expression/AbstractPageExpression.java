package com.nandox.jop.core.processor.expression;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.DomException;
import com.nandox.jop.core.context.RequestContext;

/**
 * Abstract implementation for page expression.<p> 
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    AbstractPageExpression.java
 * 
 * @date      04 ott 2016 - 04 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public abstract class AbstractPageExpression<E extends Object> implements PageExpression {

	/** expression identifier */
	protected String Id;
	private String code;	// expression source code 
	private ExpressionInvoker invoker;	// expression invoker of runtime compiled class
	//private E value;	// cached value returned from expression invoker

	/**
	 * @param	  Context	Application context
	 * @param	  Code		expression code
	 * @param	  Vars 		list of block variables definitions [variable name, class]
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	public AbstractPageExpression(WebAppContext Context, String Code, Map<String,Class<?>> Vars) throws DomException {
		this.Id = computeId(Code);
		this.code = Code;
		@SuppressWarnings("unchecked")
		Class<E> retType = (Class<E>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		this.createInvokerClass(Context, retType, Vars);
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.PageExpression#getId
	 */
	public String getId() {
		return Id;
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.PageExpression#getCode()
	 */
	public String getCode() {
		return code;
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.PageExpression#Execute(com.nandox.jop.core.context.WebAppContext,Map<String,Object>)
	 */
	public abstract E execute(WebAppContext Context, Map<String,Object> Vars);
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.PageExpression#ResetValue(com.nandox.jop.core.context.WebAppContext)
	 */
	public void resetValue (WebAppContext Context) {
		ExpressionValue<E> ev = this.initExpValue(Context); 
		ev.setValue(null);
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.PageExpression#instanceValue()
	 */
	@Override
	public ExpressionValue<E> instanceValue() {
		return new ExpressionValue<E>(this.Id);
	}
	/**
	 * Invoke own ExpressionInvoker only if value is not reset.<br>
	 * @param	  Context	Application context
	 * @param	  Vars 	list of block variables instance [variable name, variable value instanced]
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  value on specific type
	 */
	@SuppressWarnings("unchecked")
	protected Object invoke(WebAppContext Context, Map<String,Object> Vars) {
		ExpressionValue<E> ev = this.initExpValue(Context); 
		if ( ev.getValue() == null )
			ev.setValue((E)this.invoker.invoke(Context, Vars));
		return ev.getValue();
	}
	@SuppressWarnings("unchecked")
	protected Object invoke(WebAppContext Context, Object Value, String NativeValue, Map<String,Object> Vars) {
		ExpressionValue<E> ev = this.initExpValue(Context); 
		ev.setValue((E)this.invoker.invoke(Context,Value,NativeValue,Vars));
		return ev.getValue();
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.PageExpression#GetBeansList()
	 */
	public String[] getBeansList() {
		return this.invoker.getBeans();
	}
	/**
	 * Compute expression identifier by hash code<br>
	 * @param	  Code expression code
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  identifier
	 */
	static public String computeId(String Code) {
		int i = Code.hashCode(); // get hash code
		return "Jbean_"+(i<0?i*-1:i); // avoid sign (char '-')
	}
	// Create and compile expression invoker class 
	//
	//
	void createInvokerClass(WebAppContext Context, Class<E> RetClass, Map<String,Class<?>> Vars) throws DomException {
		try {
			this.invoker = Context.getBeanCompiler().createInvoker(Context, this.Id, this.code, RetClass.getName(), Vars);
		} catch (Exception e) {
			throw new DomException(e.getMessage());
		}
	}
	//
	//
	//
	@SuppressWarnings("unchecked")
	private ExpressionValue<E> initExpValue(WebAppContext context) {
		RequestContext rc = WebAppContext.getCurrentRequestContext();
		ExpressionValue<E> ev = (ExpressionValue<E>)rc.getExpressionValue(this.Id); 
		if ( ev == null )
			rc.addExpressionValue(ev=this.instanceValue());
		return ev;
	}
}
