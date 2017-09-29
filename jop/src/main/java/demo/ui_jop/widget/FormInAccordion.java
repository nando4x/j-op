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
		Iterator<Element> elems = this.domEl.getElementsByTag("wdg_item").iterator();
		while (elems.hasNext() ) {
			Element e = elems.next();
			Tmpl.select(cssQuery)
		}
		Element tmp = Tmpl.clone();
		Iterator<Element> elems = Tmpl.getAllElements().iterator();
		while (elems.hasNext() ) {
			Element e = elems.next();
			// if found replace template clone widget with same tag of the element 
			if ( e.tagName().toLowerCase().startsWith("wdg_") ) {
				if ( !this.domEl.getElementsByTag(e.tagName()).isEmpty() ) {
					e.html(this.domEl.getElementsByTag(e.tagName()).get(0).html());
				}
				e.unwrap();
			}
		}
	}

}
