package at.bestsolution.econ.typedaway.core.marker;

import org.eclipse.jface.text.source.Annotation;

import at.bestsolution.typescript.service.api.model.Diagnostic;

public class TypescriptAnnotation extends Annotation {
	private final Diagnostic error;

	public TypescriptAnnotation(Diagnostic error) {
		super("dart.annotation."+error.category(), false, error.message().messageText() + "");
		this.error = error;
	}

	public Diagnostic getError() {
		return error;
	}

	@Override
	public String toString() {
		return "DartAnnotation("+getType()+")";
	}
}
