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
	<div jop_id=''>
	</div>
	<div 	jop_id='xxw'
			jop_rendered="java{
					return (1==0);
				}"
			class="java{return $helloWorld.getCssClass();} xxxx"
			>
		<div>	
		sasa<span>aa</span><jbean>{return $helloWorld.getMessage();}</jbean>ssass
		</div>
		<table jop_id="tab1">
			<thead>
				<th>col1</th>
				<th>col2</th>
				<th>col3</th>
			</thead>
			<tbody>
				<tr>
					<td>dt col1</td>
				</tr>
			</tbody>
		</table>
	</div>
	<input value="java{if(value!=null) $helloWorld.setMessage(value) else return $helloWorld.getMessage();}">
	<input value="java{return JopHelper.manageInput($helloWorld,"Message",value);}">
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
