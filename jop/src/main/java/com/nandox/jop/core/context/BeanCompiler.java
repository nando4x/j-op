package com.nandox.jop.core.context;

import java.util.ArrayList;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
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
	 * @param	  Context	Application context
	 * @date      17 nov 2016 - 17 nov 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	public BeanCompiler (WebAppContext Context) {
		JavaCompiler cmpl = ToolProvider.getSystemJavaCompiler();
	    DiagnosticCollector<JavaFileObject> dgn = new DiagnosticCollector<JavaFileObject>();
	}

	public BeanDescriptor CreateClass (WebAppContext Context, String BeanName, String BeanCode, String ReturnClass) {
    	BeanDescriptor b = new BeanDescriptor();
    	// Composite java code
		String code = "public class "+this.getClass().getPackage().getName()+"."+BeanName + " {";
		code += "	public "+ReturnClass+ " invoke (Object beans[]) {";
		// search bean reference $xxxx
		ArrayList<String> l = new ArrayList<String>();
		int inx_st = BeanCode.indexOf('$');
		int inx_end = BeanCode.indexOf('.',inx_st);
		while ( inx_st >= 0 && inx_end > inx_st) {
			l.add(BeanCode.substring(inx_st+1, inx_end-1));
			inx_st = BeanCode.indexOf('$');
			inx_end = BeanCode.indexOf('.',inx_st);
		}
		if ( l.size() > 0 ) {
			b.beans = l.toArray(new String[0]);
			for ( int ix=0; ix<l.size(); ix++ )
				code += "	"+Context.GetBeanType(b.beans[ix]).getName()+" "+b.beans[ix]+"=beans["+ix+"]; ";
		}
		code += BeanCode;
		code += "	}";
		code += "}";
	    JavaFileObject file = new JavaSourceFromString(BeanName, code);
	    Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
	    CompilationTask task = this.cmpl.getTask(null, null, this.dgn, null, null, compilationUnits);
	    boolean success = task.call();
	    // TODO: manage error compile
	    /*for (Diagnostic diagnostic : this.dgn.getDiagnostics()) {
	      System.out.println(diagnostic.getCode());
	      System.out.println(diagnostic.getKind());
	      System.out.println(diagnostic.getPosition());
	      System.out.println(diagnostic.getStartPosition());
	      System.out.println(diagnostic.getEndPosition());
	      System.out.println(diagnostic.getSource());
	      System.out.println(diagnostic.getMessage(null));
	    }*/
	    try {
	    	b.clazz = Class.forName(BeanName);
	    } catch (ClassNotFoundException e) {}
	    return null;
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
	
	class BeanDescriptor {
		protected Class<?> clazz;
		protected String[] beans;
	}
}
