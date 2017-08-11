package com.nandox.jop.core.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.net.URLClassLoader;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import com.nandox.jop.core.ErrorsDefine;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.Arrays;
/**
 * Compile a java expression.<br>
 * Compile and create the invoker class of an expression, if already exist an invoker for the same expression<br>
 * return it 
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    ExpressionCompiler.java
 * 
 * @date      17 nov 2016 - 17 nov 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */

public class ExpressionCompiler {
	
	/** package of expression class */
	public static final String PACKAGE = "";
	
	private static final String package_pfx = (PACKAGE.isEmpty()?"":PACKAGE+"."); 
	private JavaCompiler cmpl;		// 			
	private DiagnosticCollector<JavaFileObject> dgn;
	private String classpath;
	private ClassLoader classLoader;
	private HashMap<String,ExpressionInvoker> invokers; // list of created invoker
	/**
	 * @date      17 nov 2016 - 17 nov 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	public ExpressionCompiler () {
		this.cmpl = ToolProvider.getSystemJavaCompiler();
	    this.dgn = new DiagnosticCollector<JavaFileObject>();
	    this.classpath = ((URLClassLoader)this.getClass().getClassLoader()).getURLs()[0].getPath();
    	this.classLoader = this.getClass().getClassLoader();
    	this.invokers = new HashMap<String,ExpressionInvoker>();
	}

	/**
	 * @param classpath the classpath to set
	 */
	public void setClasspath(String classpath) {
		if ( classpath != null )
			this.classpath = classpath;
	}

	/** Create or get (if exist) the invoker
	 * @param	  Context	Application context
	 * @param	  BeanName name of bean
	 * @param	  BeanCode java code of bean
	 * @param	  ReturnClass name of the returned type
	 * @param	  Vars 	list of block variables definitions [variable name, class]
	 * @date      17 nov 2016 - 17 nov 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	public ExpressionInvoker createInvoker (WebAppContext Context, String BeanName, String BeanCode, String ReturnClass, Map<String,Class<?>> Vars) throws Exception {
		// Check if invoker already exist
		if ( !this.invokers.containsKey(BeanName) ) {
	    	// Composite java code and class path 
			String code = "";
			String path = "";
			ArrayList<String> l = new ArrayList<String>(); // create list of argumented beans
			code = this.compositeCode(Context, BeanName, ReturnClass, BeanCode, l, Vars);
			String beans[] = l.toArray(new String[0]);
			path = this.computeBeansClasspath(Context, l);
		    JavaFileObject file = new JavaSourceFromString(package_pfx+BeanName, code);
		    Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
		    ArrayList<String> o = new ArrayList<String>();
		    // Add path with compiler options
	    	o.add("-classpath");
		    if ( path.isEmpty() )
		    	path = this.classpath+";";
		    path += this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + ";";
	    	o.add(System.getProperty("java.class.path")+";"+path);
		    o.add("-d");
		    o.add(this.classpath);
		    // Compile
		    CompilationTask task = this.cmpl.getTask(null, null, this.dgn, o, null, compilationUnits);
		    boolean success = task.call();
		    if ( success ) {
		    	// Create invoker with the class just compiled
			    try {
			    	@SuppressWarnings({ "rawtypes", "unchecked" })
					Class<ExpressionExecutor> c = (Class<ExpressionExecutor>)classLoader.loadClass(package_pfx+BeanName);
			    	ExpressionInvoker i = new ExpressionInvoker(c,beans);
			    	this.invokers.put(BeanName, i);
			    	return i;
			    } catch (Exception e) {
			    	//TODO: gestire errore
			    }
		    } else {
			    // TODO: manage error compile
			    StringWriter writer = new StringWriter();
			    PrintWriter out = new PrintWriter(writer);
			    for (Diagnostic<?> diagnostic : this.dgn.getDiagnostics()) {
			    	out.println(diagnostic.getCode());
			    	out.println(diagnostic.getKind());
			    	out.println(diagnostic.getPosition());
				    out.println(diagnostic.getStartPosition());
				    out.println(diagnostic.getEndPosition());
				    out.println(diagnostic.getSource());
				    out.println(diagnostic.getMessage(null));
			    }
				throw new Exception(writer.toString());
		    }
		    return null;
		} else
			return this.invokers.get(BeanName);
	}
	
	// Composite code of expression class
	//
	//
	private String compositeCode (WebAppContext context, String classname, String returnclass, String source, List<String> beans, Map<String,Class<?>> vars) throws Exception {
		String code = (PACKAGE.isEmpty()?"":"package "+PACKAGE);
		// 	search beans reference $xxxx and put them on argument list
		int inx_st = source.indexOf('$');
		int inx_end = source.indexOf('.',inx_st);
		while ( inx_st >= 0 && inx_end > inx_st) {
			try {
				beans.add(source.substring(inx_st+1, inx_end));
				code += "import "+this.epurateClassName(context.getBeanType(source.substring(inx_st+1, inx_end)).getName())+";";
				source = source.replace(source.substring(inx_st, inx_end), source.substring(inx_st+1, inx_end));
				inx_st = source.indexOf('$', inx_st+1);
				inx_end = source.indexOf('.',inx_st);
			} catch ( org.springframework.beans.factory.NoSuchBeanDefinitionException e ) {
				throw new Exception(ErrorsDefine.JOP_BEAN_NOTFOUND);
			}
		}
		// wrap code to class that implements ExpressionExecutor interface
		code += "public class "+classname + " implements "+ExpressionExecutor.class.getName()+"<"+returnclass+"> {";
		code += "public "+returnclass+ " invoke (com.nandox.jop.bean.BeanAppContext appContext, Object beans[],Object Value, String nativeValue, java.util.Map<String,Object> vars) { ";
		code += returnclass+" value = ("+returnclass+")Value;";
		// add variables declaration if present and have a class definition
		if ( vars != null && vars.size() > 0 ) {
			Iterator<String> v = vars.keySet().iterator();
			while (v.hasNext()) {
				String k = v.next();
				if ( vars.get(k) != null )
					code += " "+vars.get(k).getName()+" "+k+" = (vars!=null&&vars.size()>0?("+vars.get(k).getName()+")vars.get(\""+k+"\"):null);";
			}
		}
		if ( beans.size() > 0 ) {
			int ix=0;
			for ( String bean: beans ) {
				String bcls = this.epurateClassName(context.getBeanType(bean).getName());
				code += ""+bcls+" "+bean+"=("+bcls+")beans["+ix+"]; ";
				ix++;
			}
		}
		code += source;
		code += "}";
		code += "}";
		return code;
	}
	// Compute path getting classe path of bean used as argument
	//
	//
	private String computeBeansClasspath (WebAppContext context, List<String> beans) {
		String path = "";
		for ( String bean: beans ) {
			path += context.getBeanType(bean).getClassLoader().getResource("").getPath();//context.GetBeanType(bean).getProtectionDomain().getCodeSource().getLocation();
			path += ";";
		}
		/*if ( path.isEmpty() ) {
			path += this.getClass().getClassLoader().getResource("").getPath();//context.GetBeanType(bean).getProtectionDomain().getCodeSource().getLocation();
			path += ";";
		}*/
		return path;
	}
	//
	//
	//
	private String epurateClassName (String ClassName) {
		if ( ClassName.indexOf("$$") > 0 )
			ClassName = ClassName.substring(0,ClassName.indexOf("$$"));
		return ClassName;
	}
	/*
	public static void main(String args[]) throws IOException {
	    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

	    StringWriter writer = new StringWriter();
	    PrintWriter out = new PrintWriter(writer);
	    out.println("public class HelloWorld {");
	    out.println("  public static void main(String args[]) {");
	    out.println("    System.out.println(\"This is in another java file\");");    
	    out.println("  }");
	    out.println("}");
	    out.close();
	    JavaFileObject file = new JavaSourceFromString("HelloWorld", writer.toString());

	    Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
	    CompilationTask task = cmpl.getTask(null, null, diagnostics, null, null, compilationUnits);

	    boolean success = task.call();
	    for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
	      System.out.println(diagnostic.getCode());
	      System.out.println(diagnostic.getKind());
	      System.out.println(diagnostic.getPosition());
	      System.out.println(diagnostic.getStartPosition());
	      System.out.println(diagnostic.getEndPosition());
	      System.out.println(diagnostic.getSource());
	      System.out.println(diagnostic.getMessage(null));

	    }
	    System.out.println("Success: " + success);

	    if (success) {
	      try {

	          URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { new File("").toURI().toURL() });
	          Class.forName("HelloWorld", true, classLoader).getDeclaredMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { null });

	      } catch (ClassNotFoundException e) {
	        System.err.println("Class not found: " + e);
	      } catch (NoSuchMethodException e) {
	        System.err.println("No such method: " + e);
	      } catch (IllegalAccessException e) {
	        System.err.println("Illegal access: " + e);
	      } catch (InvocationTargetException e) {
	        System.err.println("Invocation target: " + e);
	      }
	    }
	  }
	}*/

	class JavaSourceFromString extends SimpleJavaFileObject {
	  final String code;

	  JavaSourceFromString(String name, String code) {
	    super(URI.create("string:///" + name.replace('.','/') + Kind.SOURCE.extension),Kind.SOURCE);
	    this.code = code;
	  }

	  @Override
	  public CharSequence getCharContent(boolean ignoreEncodingErrors) {
	    return code;
	  }
	}
}