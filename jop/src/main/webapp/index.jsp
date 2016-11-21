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
	<div 	jop_id=''
			xjop_rendered="java{
					helloWorld.IsRendering()
				}"
			xclass="java{helloWorld.CssClass}"
			>
		<div>	
		sasa<jbean>{return $helloWorld.Message}</jbean>ssass
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
	<input value="java{$helloWorld.Message}">
	<div jop_id='id1' onclick="java{}">
		<jbean>{return $helloWorld.Message}</jbean>
		<div jop_id='id2'>
			asasass<jbean>{return $helloWorld.Message}</jbean>
			<jbean>{return $helloWorld.Message}</jbean>
		</div>
		<div>
			<jbean>{return $helloWorld.Message}</jbean>
			<div jop_id='id3'>
				<jbean>{return $helloWorld.Message}</jbean>
				<div jop_id='id4'>
					<jbean>{return $helloWorld.Message}</jbean>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
