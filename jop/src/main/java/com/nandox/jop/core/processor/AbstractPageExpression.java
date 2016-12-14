package com.nandox.jop.core.processor;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.context.ExpressionInvoker;
import com.nandox.jop.core.context.BeanException;
import com.nandox.jop.core.ErrorsDefine;

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

	private String beanId;
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
	public AbstractPageExpression(WebAppContext Context, String Code, Class<E> ReturnClass) throws DomException {
		this.beanId = ComputeId(Code);
		this.code = Code;
		//this.makeInvoker(Context, Clazz);
		this.createInvokerClass(Context, ReturnClass);
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.PageExpression#getId
	 */
	public String getId() {
		return beanId;
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
	public abstract E Execute(WebAppContext Context);
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.PageExpression#ResetValue
	 */
	public void ResetValue () {
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
	protected Object Invoke(WebAppContext Context) {
		if ( this.value == null )
			this.value = (E)this.invoker.Invoke(Context);
		return this.value;
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
	static public String ComputeId(String Code) {
		int i = Code.hashCode();
		return "Jbean_"+(i<0?i*-1:i);
	}
	// Create and compile invoker class 
	//
	//
	void createInvokerClass(WebAppContext Context, Class<E> RetClass) throws DomException {
		try {
			this.invoker = Context.getBeanCompiler().CreateInvoker(Context, this.beanId, this.code, RetClass.getName());
		} catch (Exception e) {
			throw new DomException(e.getMessage());
		}
	}
	// Get invoker from applicartion context by class and method
	// name take from bean identificator
	//
	/*private void makeInvoker(WebAppContext Context, Class<E> clazz) throws DomException {
		int inx_st = this.beanId.indexOf("{");
		int inx_end = this.beanId.indexOf("}");
		if ( inx_st >=0 && inx_end > inx_st ) {
			// get bean name
			int inx_dot = this.beanId.indexOf(".",inx_st+1);
			if ( inx_dot > 0 ) {
				String name = this.beanId.substring(inx_st+1, inx_dot).trim();
				String method = this.beanId.substring(inx_dot+1, inx_end).trim();
				int br;
				if ( (br = method.indexOf("(")) > 0 ) {
					if ( method.indexOf(")",br) > 0 )
						method = method.substring(0,br);
					else // error brachet
						throw new DomException(ErrorsDefine.JOP_BEAN_SYNTAX);
				} else
					method = "get"+method.substring(0, 1).toUpperCase()+method.substring(1);
				try {
					this.invoker = Context.GetBeanInvoker(name, method);
				} catch (BeanException e) { throw new DomException(ErrorsDefine.JOP_BEAN_NOTFOUND); }
				try {
					this.invoker.CheckCompliance(clazz);
				} catch (BeanException e) { throw new DomException(ErrorsDefine.JOP_BEAN_NOTFOUND); }
			} else // error dot
				throw new DomException(ErrorsDefine.JOP_BEAN_SYNTAX);
		} else // error delimiter
			throw new DomException(ErrorsDefine.JOP_BEAN_SYNTAX);
	}*/
}
