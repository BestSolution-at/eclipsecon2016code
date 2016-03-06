package at.bestsolution.econ.typedaway.app.themes;

import org.eclipse.fx.ui.services.theme.MultiURLStylesheet;
import org.eclipse.fx.ui.services.theme.Stylesheet;
import org.eclipse.fx.ui.services.theme.Theme;
import org.eclipse.fx.ui.theme.AbstractTheme;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicyOption;

@Component(service=Theme.class)
public class DarkTheme extends AbstractTheme {
	public DarkTheme() {
		super("theme.dark", "Dark theme", DefaultTheme.class.getClassLoader().getResource("css/dark.css"));
	}

	@Reference(cardinality=ReferenceCardinality.MULTIPLE,policyOption=ReferencePolicyOption.GREEDY)
	@Override
	public void registerStylesheet(Stylesheet stylesheet) {
		super.registerStylesheet(stylesheet);
	}

	@Override
	public void unregisterStylesheet(Stylesheet stylesheet) {
		super.unregisterStylesheet(stylesheet);
	}

	@Reference(cardinality=ReferenceCardinality.MULTIPLE,policyOption=ReferencePolicyOption.GREEDY)
	@Override
	public void registerMultiURLStylesheet(MultiURLStylesheet stylesheet) {
		super.registerMultiURLStylesheet(stylesheet);
	}

	@Override
	public void unregisterMultiURLStylesheet(MultiURLStylesheet stylesheet) {
		super.unregisterMultiURLStylesheet(stylesheet);
	}
}
