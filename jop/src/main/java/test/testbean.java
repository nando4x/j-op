package test;

public class testbean {
	private String message;

   public void setMessage(String message){
      this.message  = message;
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
