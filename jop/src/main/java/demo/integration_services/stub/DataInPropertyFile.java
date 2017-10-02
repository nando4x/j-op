package demo.integration_services.stub;

import java.util.ResourceBundle;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;

public class DataInPropertyFile<E> {
	private ResourceBundle resourceBundle; 			// proprieta'
		
	public DataInPropertyFile(String Name) {
		this.resourceBundle = ResourceBundle.getBundle("demo."+Name);
	}

	public void readEntity(String Key, E Entity) {
		Field[] f = Entity.getClass().getDeclaredFields();
		for ( int ix=0; ix<f.length; ix++ ) {
			if ( Modifier.isPrivate(f[ix].getModifiers()) || Modifier.isProtected(f[ix].getModifiers()) ) {
				String fname = f[ix].getName().substring(0, 1).toUpperCase()+f[ix].getName().substring(1);
				try {
					if ( Entity.getClass().getDeclaredMethod("set"+fname,f[ix].getType()) != null ) {
						if ( this.resourceBundle.containsKey(Key+"."+fname.toLowerCase()) ) {
							String v = this.resourceBundle.getString(Key+"."+fname.toLowerCase());
							Entity.getClass().getDeclaredMethod("set"+fname,f[ix].getType()).invoke(Entity, v);
						}
					}
				} catch (Exception e) {
					
				}
			}
		}
	}
	public void writeEntity(String Key, E Entity) {
		Field[] f = Entity.getClass().getDeclaredFields();
		for ( int ix=0; ix<f.length; ix++ ) {
			if ( Modifier.isPrivate(f[ix].getModifiers()) || Modifier.isProtected(f[ix].getModifiers()) ) {
				String fname = f[ix].getName().substring(0, 1).toUpperCase()+f[ix].getName().substring(1);
				try {
					if ( Entity.getClass().getDeclaredMethod("get"+fname) != null ) {
						String v = (String)Entity.getClass().getDeclaredMethod("get"+fname).invoke(Entity);
						v = null;
					}
				} catch (Exception e) {
					
				}
			}
		}
	}

}
