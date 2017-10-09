package demo.ui_jop.widget;

import java.util.Iterator;

import org.jsoup.nodes.Element;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.DomException;
import com.nandox.jop.widget.WidgetBlock;

public class FormInAccordion extends WidgetBlock {

	public FormInAccordion(WebAppContext Context, String PageId, Element DomElement) throws DomException {
		super(Context, PageId, DomElement);
	}

	/* (non-Javadoc)
	 * @see com.nandox.jop.widget.WidgetBlock#compileTemplate(org.jsoup.nodes.Element)
	 */
	@Override
	protected void compileTemplate(Element Tmpl) {
		Element tmp = Tmpl.clone();
		Iterator<Element> elems = this.domEl.getElementsByTag("wdg_item:not(wdg_item jwdg wdg_item)").iterator();
		int ix=0;
		while (elems.hasNext() ) {
			Element e = elems.next();
			Element item;
			if ( ix > 0 ) { // first item every substitute others append
				if ( e.hasAttr("newrow") ) {
					item = tmp.select(".row:has(.cell)").get(0);
					Tmpl.select(".row:has(.cell):last-of-type").get(0).after(item.outerHtml());
				} else {
					item = tmp.select(".cell").get(0);
					Tmpl.select(".cell:last-of-type").get(0).after(item.outerHtml());
				}
			}
			Tmpl.getElementsByTag(e.tagName()).get(0).tagName(e.tagName()+ix);
			e.tagName(e.tagName()+ix);
			ix++;
		}
		if ( this.domEl.hasAttr("jop_onaction") )
			Tmpl.child(0).attr("jop_onaction",this.domEl.attr("jop_onaction"));
		super.compileTemplate(Tmpl);
	}

}
