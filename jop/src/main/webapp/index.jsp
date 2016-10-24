<html>
<body>
<script type="javascript">
	var a, b, c;
	if ( (a && b) || c > 2 ) {
		a = "ssa";
	}
</script>
	<div 	jop_id='id'
			jop_rendered={helloWorld.IsRendering()}
			class="xxx {helloWorld.CssClass}"
			>
		jop_bean={helloWorld.Message}
	</div>
	<div jop_id='id1'>
		jop_bean={helloWorld.Message}
		<div jop_id='id2'>
			asasassjop_bean={helloWorld.Message}
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
