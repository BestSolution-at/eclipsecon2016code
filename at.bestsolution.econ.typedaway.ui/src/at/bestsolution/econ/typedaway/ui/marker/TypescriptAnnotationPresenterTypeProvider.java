package at.bestsolution.econ.typedaway.ui.marker;

import org.eclipse.fx.code.editor.Input;
import org.eclipse.fx.code.editor.SourceFileInput;
import org.eclipse.fx.code.editor.fx.services.AnnotationPresenterTypeProvider;
import org.eclipse.fx.text.ui.source.AnnotationPresenter;
import org.osgi.service.component.annotations.Component;

@SuppressWarnings("restriction")
@Component
public class TypescriptAnnotationPresenterTypeProvider implements AnnotationPresenterTypeProvider {

	@Override
	public Class<? extends AnnotationPresenter> getType(Input<?> s) {
		return TypescriptAnnotationPresenter.class;
	}

	@Override
	public boolean test(Input<?> input) {
		return input instanceof SourceFileInput && ((SourceFileInput)input).getURI().endsWith(".ts");
	}

}
