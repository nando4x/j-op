<html>
<head>
 <script type="text/javascript" src="jquery-1.12.2.js"/>
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
		sasa<span class="java{return $helloWorld.getCssClass();} xxxx">aa</span><jbean>{return $helloWorld.getMessage();}</jbean>ssass
		</div>
		<table jop_id="tab1">
			<thead>
				<th>col1</th>
				<th>col2</th>
				<th>col3</th>
			</thead>
			<tbody>
				<!-- <tr rjop_repeater='java{return $helloWorld.getArray();}' jop_var='var'>
					<td>java{return var;}</td>
				</tr> -->
			</tbody>
		</table>
	</div>
	<form jop_id='' action="index.jsp" method="post">
		<input name="male" value="java{if(value!=null) { $helloWorld.setMessage(value) } else return $helloWorld.getMessage();}">
		<input  value='java{return JopHelper.manageInput($helloWorld,"Message",value);}">
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
			<jbean>{return    $helloWorld.getMessage();}</jbean>
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
