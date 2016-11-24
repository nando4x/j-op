package com.nandox.jop.core.context;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.net.URLClassLoader;
import java.net.URL;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.Arrays;
/**
 * Descrizione classe
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    BeanCompiler.java
 * 
 * @date      17 nov 2016 - 17 nov 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */

public class BeanCompiler {
	
	private JavaCompiler cmpl;
	DiagnosticCollector<JavaFileObject> dgn;
	/**
	 * @date      17 nov 2016 - 17 nov 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	public BeanCompiler () {
		this.cmpl = ToolProvider.getSystemJavaCompiler();
	    this.dgn = new DiagnosticCollector<JavaFileObject>();
	}

	public BeanInvoker CreateInvoker (WebAppContext Context, String BeanName, String BeanCode, String ReturnClass) throws Exception {
		String beans[] = new String[0];
    	// Composite java code
		String code = "package com.nandox.jop.core.context;";
		String path = "";
		// 		search bean reference $xxxx
		ArrayList<String> l = new ArrayList<String>();
		code = this.compositeCode(Context, BeanName, ReturnClass, BeanCode, l);
		path = this.computeBeansClasspath(Context, l);
	    JavaFileObject file = new JavaSourceFromString("com.xxxx."+BeanName, code);
	    Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
	    ArrayList<String> o = new ArrayList<String>();
	    o.add("-classpath");
	    o.add(System.getProperty("java.class.path")+";"+path);
	    CompilationTask task = this.cmpl.getTask(null, null, this.dgn, o, null, compilationUnits);
	    boolean success = task.call();
	    if ( success ) {
		    try {
		    	URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { new File("").toURI().toURL() });
		    	return new BeanInvoker(Class.forName("com.xxxx."+BeanName, true, classLoader),beans);
		    } catch (Exception e) {
		    	//TODO: gestire errore
		    	e = e;
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
	}
	
	//
	//
	//
	private String compositeCode (WebAppContext context, String classname, String returnclass, String source, List<String> beans) {
		String code = "package com.xxxx;";
		// 	search beans reference $xxxx and put them on argument list
		int inx_st = source.indexOf('$');
		int inx_end = source.indexOf('.',inx_st);
		while ( inx_st >= 0 && inx_end > inx_st) {
			beans.add(source.substring(inx_st+1, inx_end));
			code += "import "+context.GetBeanType(source.substring(inx_st+1, inx_end)).getName()+";";
			source = source.replace(source.substring(inx_st, inx_end), source.substring(inx_st+1, inx_end));
			inx_st = source.indexOf('$', inx_st+1);
			inx_end = source.indexOf('.',inx_st);
		}
		code += "public class "+classname + " {";
		code += "public "+returnclass+ " invoke (Object beans[]) {";
		if ( beans.size() > 0 ) {
			int ix=0;
			for ( String bean: beans ) {
				code += ""+context.GetBeanType(bean).getName()+" "+bean+"=("+context.GetBeanType(bean).getName()+")beans["+ix+"]; ";
				ix++;
			}
		}
		code += source;
		code += "}";
		code += "}";
		return code;
	}
	//
	//
	//
	private String computeBeansClasspath (WebAppContext context, List<String> beans) {
		String path = "";
		for ( String bean: beans ) {
			path += context.GetBeanType(bean).getClassLoader().getResource("").getPath();//context.GetBeanType(bean).getProtectionDomain().getCodeSource().getLocation();
			path += ";";
		}
		return path;
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