<html>
<head>
 <script type="text/javascript" src="jquery-1.12.2.js"/>
 <script type="text/javascript" src="jopscript/baselibs.js"/>
 <script type="text/javascript" src="jopscript/core/services.js"/>
</head>
<body>
<script type="javascript">
	var a, b, c;
	if ( (a && b) || c > 2 ) {
		a = "ssa";
	}
</script>
	<div jop_id=''>
	</div>
	<div 	jop_id='xxw'
			jop_rendered="java{
					return (1==1);
				}"
			class="java{return $helloWorld.getCssClass();} xxxx"
			>
		<div>	
		sasas<span class="java{return $helloWorld.getCssClass();} xxxx">aa</span><jbean>{return $helloWorld.getMessage();}</jbean>ssass
		</div>
		<table jop_id="tab1">
			<thead>
				<th>col1</th>
				<th>col2</th>
				<th>col3</th>
			</thead>
			<tbody>
				<tr  jop_repeater='java{return $helloWorld.getArray();}' jop_var='(java.lang.String)var'>
					<td><xxjbean>{return var;}</xxjbean></td>
				</tr>
			</tbody>
		</table>
	</div>
	<script>
		function xxx(_this) {
			alert('xxx');
		}
	</script>
	<form jop_id='' onsubmit="xxx(this);"  action="index.jsp" method="post">
		<input jop_submiton="change" name="male" value="java{if(value!=null) { $helloWorld.setMessage(value); } return $helloWorld.getMessage();}">
			ssasssasa
		</input>
		<input  value='xjava{return JopHelper.manageInput($helloWorld,"Message",value);}'>
		<textarea rows="4" cols="50" name="ddd">
			At w3schools.com you will learn how to make a website. We offer free tutorials in all web development technologies. 
		</textarea>
		<select>
			<!-- <option rjop_repeater='java{return $helloWorld.getArray();}' 
				rjop_var='var' 
				value='java{return var;}'>java{return var;}</option> -->
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
