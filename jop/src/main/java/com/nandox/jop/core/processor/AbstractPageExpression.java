package com.nandox.jop.core.processor;

import com.nandox.jop.core.context.WebAppContext;

import java.lang.reflect.ParameterizedType;

import com.nandox.jop.core.context.ExpressionInvoker;

/**
 * Abstract implementation for page expression 
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

	protected String Id;
	private String code;
	private ExpressionInvoker invoker;
	private E value;

	/**
	 * @param	  Context	Application context
	 * @param	  Code		expression code
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	public AbstractPageExpression(WebAppContext Context, String Code) throws DomException {
		this.Id = computeId(Code);
		this.code = Code;
		@SuppressWarnings("unchecked")
		Class<E> retType = (Class<E>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		this.createInvokerClass(Context, retType);
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
	 * @see com.nandox.jop.core.processor.PageExpression#Execute(com.nandox.jop.core.context.WebAppContext)
	 */
	public abstract E execute(WebAppContext Context);
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.PageExpression#ResetValue
	 */
	public void resetValue () {
		this.value = null;
	}
	/**
	 * Invoke own ExpressionInvoker only if value is not reset.<br>
	 * @param	  Context	Application context
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  value on specific type
	 */
	@SuppressWarnings("unchecked")
	protected Object invoke(WebAppContext Context) {
		if ( this.value == null )
			this.value = (E)this.invoker.invoke(Context);
		return this.value;
	}
	@SuppressWarnings("unchecked")
	protected Object invoke(WebAppContext Context, E Value, String NativeValue) {
		this.value = (E)this.invoker.invoke(Context,Value,NativeValue);
		return this.value;
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
	// Create and compile invoker class 
	//
	//
	void createInvokerClass(WebAppContext Context, Class<E> RetClass) throws DomException {
		try {
			this.invoker = Context.getBeanCompiler().createInvoker(Context, this.Id, this.code, RetClass.getName());
		} catch (Exception e) {
			throw new DomException(e.getMessage());
		}
	}
}
