package test;

public class testbean {
	private String message;

   public void setMessage(String message){
      this.message  = message;
   }

   public String getMessage(){
      return  message;
   }
   
   public boolean IsRendering() {
	   return true;
   }

   public String getCssClass() {
	   return "myCssClass";
   }
}
