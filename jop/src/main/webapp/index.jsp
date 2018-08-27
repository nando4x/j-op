<html>
<head>
 <script jop_head="true"/>
 <script type="text/javascript" src="jquery-1.12.2.js"/>
</head>
<body>
<script type="javascript">
	var a, b, c;
	if ( (a && b) || c > 2 ) {
		a = "ssa";
	}
</script>
		<!--<table jop_id="xxtab1">
			<thead>
				<th>col1</th>
				<th>col2</th>
				<th>col3</th>
			</thead>
			<tbody jop_repeater='java{return $helloWorld.getArray();}' jop_var='(java.lang.String)var'>
				<tr  >
					<td><jbean>{return var;}</jbean></td>
					<td><jbean>{return var+"-1";}</jbean></td>
				</tr>
			</tbody>
		</table>-->
		<div jop_id>
			<select>
				<option value='java{return $helloWorld.getArray()[0];}'>
					<jbean>{return $helloWorld.getArray()[0];}</jbean>
				</option>
				<option value='java{return $helloWorld.getArray()[1];}'>
					<jbean>{return $helloWorld.getArray()[1];}</jbean>
				</option>
			</select>
		</div>		
		<form jop_id="">
		<div jop_repeater='java{return $helloWorld.getArray();}' jop_var='varP'>
		<select jop_repeater='java{return $helloWorld2.getArray();}' jop_var='var2' value='java{return "";}'
				class="java{return $helloWorld2.getCssClass();} xxxx">
			<option class="java{return var2;}" value="java{return var2;}"><jbean>{return varP + "choice "+var2;}</jbean></option>
		</select>
		</div>
		</form>
		<div  jop_include="page.html" jop_rendered="java{
					return (1==1);
				}">
			<param name="aaa">ccc</param>
			<param name="bbb">ccc1</param>
			<param name="ccc"></param>
		</div>
 	<div jop_id=''>
	</div>
	<div 	jop_id='xxw'
			jop_rendered="java{
					return (1==1);
				}"
			class="java{return $helloWorld.getCssClass();} xxxx"
			>
		<div>	
		sasas<span class="java{/*appContext = null; appContext.getParameter(null);*/ return $helloWorld.getCssClass();} xxxx">aa</span><jbean>{return $helloWorld.getMessage();}</jbean>ssass
		</div>
		<table jop_id="tab1">
			<thead>
				<th>col1</th>
				<th>col2</th>
				<th>col3</th>
			</thead>
			<tbody>
				<tr  jop_repeater='java{return $helloWorld.getArray();}' jop_var='(java.lang.String)var'>
					<td><jbean>{return var;}</jbean></td>
				</tr>
			</tbody>
		</table>
	</div>
	<script>
		function xxx(_this) {
			//alert('xxx');
		}
	</script>
	<form jop_id='' xonsubmit="xxx(this);"  xaction="index.jsp" method="post">
		<input jop_submiton="change" name="male" value="java{if(value!=null) { $helloWorld2.setMessage((String)value); } return $helloWorld2.getMessage();}">
			ssasssasa
		</input>
		<input  value='xjava{return JopHelper.manageInput($helloWorld,"Message",value);}'>
		<textarea rows="4" cols="50" name="ddd">
			At w3schools.com you will learn how to make a website. We offer free tutorials in all web development technologies. 
		</textarea>
		<select name="xx" jop_repeater='java{return $helloWorld.getArray();}' jop_var='var2' value='java{return "";}'
				class="java{return $helloWorld.getCssClass();} xxxx">
			<option class="java{return var2;}" value="java{return var2;}"><jbean>{return "choice "+var2;}</jbean></option>
		</select>
	<fieldset>
    <legend>Personalia:</legend>
    Name: <input name="cccc" type="text"><br>
    Email: <input type="text"><br>
    Date of birth: <input type="text">
  	</fieldset>
  	<label for="male">Male</label>	
		 <button type="button">Click Me!</button>
		<input  type="submit">
	<script jop_before="true">
		//alert('before');
	</script>
	<script>
		//alert('after');
	</script>
	</form>
	<div jop_id='id1' onclick="{}">
		<jbean>{return 
		$helloWorld.getMessage();}</jbean>
		<div jop_id='id2'>
			asasass<jbean>{return $helloWorld.getMessage();}</jbean>
			<jbean>{return           $helloWorld.getMessage();}</jbean>
		</div>
		<div>
			dd<jbean>                {return    $helloWorld.getMessage();}</jbean>
			<div jop_id='id3'>
				<jbean>{return         $helloWorld.getMessage();}</jbean>
				<div jop_id='id4'>
					<jbean>{return 
					
					$helloWorld.getMessage();}</jbean>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
