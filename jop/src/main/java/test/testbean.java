package test;

import com.nandox.jop.bean.JopMonitoring;

public class testbean {
	private String message;

	//@JopMonitoring
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
   
}
