<html>
 <link type="javascript" src="jquery.js">
<body>
<script type="javascript">
	var a, b, c;
	if ( (a && b) || c > 2 ) {
		a = "ssa";
			<jbean>aaaaa</jbean>
	}
</script>
	<div 	jop_id='id'
			jop_rendered={helloWorld.IsRendering()}
			class="{helloWorld.CssClass}"
			>
		<div>	
		jop_bean={helloWorld.Message}
		sasa<jbean>{helloWorld.Message}</jbean>ssass
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
	<div jop_id='id1'>
		jop_bean={helloWorld.Message}
		<div jop_id='id2'>
			asasassjop_bean={helloWorld.Message}
			<jbean>{helloWorld.Message}</jbean>
		</div>
		<div>
			jop_bean={helloWorld.Message}
			<div jop_id='id3'>
				jop_bean={helloWorld.Message}
				<div jop_id='id4'>
					jop_bean={helloWorld.Message}
				</div>
			</div>
		</div>
	</div>
</body>
</html>
