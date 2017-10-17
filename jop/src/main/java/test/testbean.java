package test;

import com.nandox.jop.bean.JopMonitoring;
import com.nandox.jop.core.context.WebAppContext;

public class testbean {
	private String message;
	private list clist;

	@JopMonitoring
   public void setMessage(String message){
      this.message  = message;
      message = "x";
      
   }
	
   public String getMessage(){
      return  message;
   }
   
   public Boolean IsRendering() {
	   return true;
   }

   public String getCssClass() {
	   return "myCssClass";
   }
   
   public String[] getArray () {
	   String r[] = { "a", "b" };
	   return r;
   }
   public list[] getList () {
	   list r[] = new list[2];
	   r[0] = new list();
	   r[0].code = "1";
	   r[0].value = "111";
	   r[1] = new list();
	   r[1].code = "2";
	   r[1].value = "222";
	   return r;
   }
   
   public void setClist(list lst) {
	   this.clist = lst;
   }

   public list getClist() {
	   return this.clist;
   }

   public class list {
	   public String code;
	   public String value;
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.value;
	}
	   
	   
   }
	public static class Converter implements com.nandox.jop.core.Converter {

		//public Converter() {}
		@Override
		public Object asObject(WebAppContext Context, Object Data, String Value) {
			list[] arr = (list[])Data;
			for ( int ix=0; arr!=null&&ix<arr.length; ix++ ) {
				if ( arr[ix].code.equals(Value) )
					return arr[ix];
			}
			return null;
		}

		@Override
		public String asString(WebAppContext Context, Object Data, Object Value) {
			if ( Value != null )
				return ((list)Value).value;
			return null;
		}
		
	}
}
