package com.nandox.jop.core.processor;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.context.BeanInvoker;
import com.nandox.jop.core.context.BeanException;

import com.nandox.jop.core.ErrorsDefine;
/**
 * Abstract implementation for page bean 
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    AbstractPageBean.java
 * 
 * @date      04 ott 2016 - 04 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public abstract class AbstractPageBean<E extends Object> implements PageBean {

	private String beanId;
	private String code;
	private BeanInvoker invoker;
	private String beanName;
	private E value;

	/**
	 * @param	  Context	Application context
	 * @param	  BeanId	bean identificator name
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	public AbstractPageBean(WebAppContext Context, String BeanId, Class<E> ReturnClass) throws DomException {
		this.beanId = "Jbean_"+BeanId.hashCode();
		this.code = BeanId;
		this.beanName = "testb";
		//this.makeInvoker(Context, Clazz);
		this.createInvokerClass(Context, ReturnClass);
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.PageBean#getBeanId
	 */
	public String getBeanId() {
		return beanId;
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.PageBean#Fire(com.nandox.jop.core.context.WebAppContext)
	 */
	public abstract E Fire(WebAppContext Context);
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.PageBean#ResetValue
	 */
	public void ResetValue () {
		this.value = null;
	}
	/**
	 * Invoke bean method by own BeanInvoker only if value is not reset.<br>
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

	static public String ComputeId(String Code) {
		return ""+Code.hashCode();
	}
	// Get invoker from applicartion context by class and method
	// name take from bean identificator
	//
	private void makeInvoker(WebAppContext Context, Class<E> clazz) throws DomException {
		int inx_st = this.beanId.indexOf("{");
		int inx_end = this.beanId.indexOf("}");
		if ( inx_st >=0 && inx_end > inx_st ) {
			// get bean name
			int inx_dot = this.beanId.indexOf(".",inx_st+1);
			if ( inx_dot > 0 ) {
				String name = this.beanName = this.beanId.substring(inx_st+1, inx_dot).trim();
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
	}
	//
	//
	//
	void createInvokerClass(WebAppContext Context, Class<E> RetClass) throws DomException {
		try {
			this.invoker = Context.getBeanCompiler().CreateInvoker(Context, this.beanId, this.code, RetClass.getName());
		} catch (Exception e) {
			throw new DomException(e.getMessage());
		}
	}
	
}
