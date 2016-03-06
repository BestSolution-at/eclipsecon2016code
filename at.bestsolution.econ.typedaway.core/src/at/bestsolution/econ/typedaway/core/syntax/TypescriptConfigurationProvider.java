package at.bestsolution.econ.typedaway.core.syntax;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.eclipse.fx.code.editor.Input;
import org.eclipse.fx.code.editor.configuration.EditorGModel;
import org.eclipse.fx.code.editor.configuration.LanguageDef;
import org.eclipse.fx.code.editor.configuration.text.ConfigurationModelProvider;
import org.eclipse.fx.code.editor.services.URIProvider;
import org.osgi.service.component.annotations.Component;

@SuppressWarnings("restriction")
@Component
public class TypescriptConfigurationProvider implements ConfigurationModelProvider {

	@Override
	public boolean applies(Input<?> input) {
		return ((URIProvider)input).getURI().endsWith(".ts");
	}

	@Override
	public LanguageDef getModel(Input<?> input) {
		try(InputStream in = getClass().getResourceAsStream("ts.json");
				Reader r = new InputStreamReader(in)) {
			return EditorGModel.create().createObject(r);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
