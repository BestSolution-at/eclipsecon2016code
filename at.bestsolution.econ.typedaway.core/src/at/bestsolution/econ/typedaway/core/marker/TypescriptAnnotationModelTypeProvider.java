package at.bestsolution.econ.typedaway.core.marker;

import org.eclipse.fx.code.editor.Input;
import org.eclipse.fx.code.editor.SourceFileInput;
import org.eclipse.fx.code.editor.services.AnnotationModelTypeProvider;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.osgi.service.component.annotations.Component;


@SuppressWarnings("restriction")
@Component
public class TypescriptAnnotationModelTypeProvider implements AnnotationModelTypeProvider {

	@Override
	public boolean test(Input<?> input) {
		return input instanceof SourceFileInput && ((SourceFileInput)input).getURI().endsWith(".ts");
	}

	@Override
	public Class<? extends IAnnotationModel> getType(Input<?> input) {
		return TypescriptAnnotationModel.class;
	}

}
