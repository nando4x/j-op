package com.nandox.jop.core.context;

import java.util.HashMap;
import org.springframework.context.ApplicationContext;
import com.nandox.jop.core.processor.PageApp;

/**
 * Application Context to resolve and invoke bean.<br>
 * The bean is search in spring environment
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    WebAppContext.java
 * 
 * @date      07 ott 2016 - 07 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class WebAppContext {

	private ApplicationContext springCtx;
	private HashMap<String,PageApp> pages;
	private ExpressionCompiler bcmpl;
	
	/**
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 */
	public WebAppContext() {
		this.pages = new HashMap<String,PageApp>();
		this.bcmpl = new ExpressionCompiler();
	}
	/**
	 * Search and return bean instance by name 
	 * @param 	  BeanName	bean name 
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  Bean instance
	 * @exception TODO:
	 */
	public Object GetBeanInstance(String BeanName) {
		return this.springCtx.getBean(BeanName);
	}
	/**
	 * Search and return bean Class by name 
	 * @param 	  BeanName	bean name 
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  Bean class
	 * @exception TODO:
	 */
	public Class<?> GetBeanType(String BeanName) {
		return this.springCtx.getType(BeanName);
	}
	/**
	 * Set spring application context where search bean
	 * @param 	  springCtx spring application context
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 */
	public void setSpringCtx(ApplicationContext springCtx) {
		this.springCtx = springCtx;
	}
	/**
	 * Return application page map: map with page id and page
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  pages map
	 */
	public HashMap<String,PageApp> GetPagesMap() {
		return this.pages;
	}
	/**
	 * @return the ExpressionCompiler
	 */
	public ExpressionCompiler getBeanCompiler() {
		return bcmpl;
	}
	/**
	 * Set compiler destination class path
	 * @param	  Path absolute path
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  pages map
	 */
	public void SetCompilerPath(String Path) {
		this.bcmpl.setClasspath(Path);
	}
}
