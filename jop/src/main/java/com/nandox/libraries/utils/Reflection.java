package com.nandox.libraries.utils;

import java.util.Enumeration;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import java.io.IOException;
import java.net.URL;
import java.net.URISyntaxException;

/**
 * Class with utility to use java reflection
 * 
 * @project   General Java Library
 * 
 * @module    Reflection.java
 * 
 * @date      25 ott 2016 - 25 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class Reflection {

	/**
	 * Return list with classes for a specific package
	 * @param	  Pkg	package to scan
	 * @param	  Interface	if not null return only classes that implement it (directly or not) 
	 * @param	  HaveTobeClass	if true return only class otherwise classes and interfaces
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 * @return	  Classes list 
	 */	
	public static List<Class<?>> getClassesForPackage(Package Pkg, Class<?> Interface, boolean HaveTobeClass) {
	    String pkgname = Pkg.getName();
	    ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
	    // Get a File object for the package
	    File directory = null;
	    String fullPath;
	    String relPath = pkgname.replace('.', '/');
	    
	    
	    URL resource = Reflection.class.getClassLoader().getResource(relPath);//ClassLoader.getSystemClassLoader().getResource(relPath);
	    if (resource == null) {
	        throw new RuntimeException("No resource for " + relPath);
	    }
	    fullPath = resource.getFile();

	    try {
	        directory = new File(resource.toURI());
	    } catch (URISyntaxException e) {
	        throw new RuntimeException(pkgname + " (" + resource + ") does not appear to be a valid URL / URI.  Strange, since we got it from the system...", e);
	    } catch (IllegalArgumentException e) {
	        directory = null;
	    }

	    if (directory != null && directory.exists()) {
	        // Get the list of the files contained in the package
	        String[] files = directory.list();
	        for (int i = 0; i < files.length; i++) {
	            // we are only interested in .class files
	            if (files[i].endsWith(".class")) {
	                // removes the .class extension
	                String className = pkgname + '.' + files[i].substring(0, files[i].length() - 6);
	                try {
	                	Class<?> cl = Class.forName(className);
	                	if ( !HaveTobeClass || !cl.isInterface() ) {
		                	if ( Interface != null ) {
		                		if ( Interface.isAssignableFrom(cl) )
			                		classes.add(cl);
		                	} else
		                		classes.add(cl);
	                	}
	                } 
	                catch (ClassNotFoundException e) {
	                    throw new RuntimeException("ClassNotFoundException loading " + className);
	                }
	            }
	        }
	    }
	    else {
	        try {
	            String jarPath = fullPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
	            @SuppressWarnings("resource")
				JarFile jarFile = new JarFile(jarPath);         
	            Enumeration<JarEntry> entries = jarFile.entries();
	            while(entries.hasMoreElements()) {
	                JarEntry entry = entries.nextElement();
	                String entryName = entry.getName();
	                if(entryName.startsWith(relPath) && entryName.length() > (relPath.length() + "/".length())) {
	                    String className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
	                    try {
		                	Class<?> cl = Class.forName(className);
		                	if ( !HaveTobeClass || !cl.isInterface() ) {
			                	if ( Interface != null ) {
			                		if ( Interface.isAssignableFrom(cl) )
				                		classes.add(cl);
			                	} else
			                		classes.add(cl);
		                	}
	                    } 
	                    catch (ClassNotFoundException e) {
	                        throw new RuntimeException("ClassNotFoundException loading " + className);
	                    }
	                }
	            }
	            jarFile.close();
	        } catch (IOException e) {
	            throw new RuntimeException(pkgname + " (" + directory + ") does not appear to be a valid package", e);
	        }
	    }
	    return classes;
	}
}
