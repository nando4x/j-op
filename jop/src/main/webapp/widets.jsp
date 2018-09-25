<html>
<head>
 <script jop_head="true"/>
 <script type="text/javascript" src="jquery-1.12.2.js"/>
</head>
<body>
	<div>
		<jwdg type="combobox"  
			  jop_repeater='java{return $helloWorld.getList();}'
			  jop_var='(test.testbean$list)var'
			  jop_status="status"
			  jop_converter='test.testbean$Converter' jop_support_data='java{return $helloWorld.getList();}'
			  value='java{return appContext.mngInput($helloWorld,"clist",value);}'
			>
		    <wdg_item data-value='java{return var.code;}'><jbean>{return var.value+"-"+(status.getSize()-status.getIndex());}</jbean></wdg_item>
		</jwdg>
	 </div>
 </body>
</html>
 