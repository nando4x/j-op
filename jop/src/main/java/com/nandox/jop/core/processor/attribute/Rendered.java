package com.nandox.jop.core.processor.attribute;

import org.jsoup.nodes.Element;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.BooleanPageExpression;

@JopCoreAttribute(name="rendered")
public class Rendered extends AbstractJopAttribute<BooleanPageExpression> implements JopAttribute {

	public Rendered(WebAppContext Context ,String Name, String Value) {
		super(Context,Name,Value);
	}

	public RETURN_ACTION preRender(Element Dom) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BooleanPageExpression computeExpression(WebAppContext Context, String Code) {
		return new BooleanPageExpression(Context,Code);
	}

}
