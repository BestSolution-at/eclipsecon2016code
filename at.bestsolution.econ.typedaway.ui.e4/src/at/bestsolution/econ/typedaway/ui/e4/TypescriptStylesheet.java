package at.bestsolution.econ.typedaway.ui.e4;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.fx.ui.services.theme.Stylesheet;
import org.eclipse.fx.ui.services.theme.Theme;
import org.osgi.service.component.annotations.Component;

@Component
public class TypescriptStylesheet implements Stylesheet {

	@Override
	public boolean appliesToTheme(Theme t) {
		return true;
	}

	@Override
	public URL getURL(Theme t) {
		try {
			return new URL("platform:/plugin/at.bestsolution.econ.typedaway.ui/at/bestsolution/econ/typedaway/ui/syntax/ts.css");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

}
