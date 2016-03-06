package at.bestsolution.econ.typedaway.ui.marker;

import org.eclipse.fx.text.ui.source.ITextAnnotationPresenter;
import org.eclipse.jface.text.source.Annotation;

import at.bestsolution.econ.typedaway.core.marker.TypescriptAnnotation;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

@SuppressWarnings("restriction")
public class TypescriptAnnotationTextMarker implements ITextAnnotationPresenter {

	@Override
	public boolean isApplicable(Annotation annotation) {
		return annotation instanceof TypescriptAnnotation;
	}

	@Override
	public Node createNode() {
		Region r = new Region();
		Tooltip t = new Tooltip();
		Tooltip.install(r, t);
		r.setUserData(t);
		return r;
	}

	@Override
	public void updateNode(Node node, Annotation annotation) {
		Region r = (Region) node;

		TypescriptAnnotation a = (TypescriptAnnotation) annotation;

		Color c;
		switch (a.getError().category()) {
		case ERROR: c = Color.RED; break;
		case WARNING: c = Color.DARKORANGE; break;
		default:
		case MESSAGE: c = Color.BLANCHEDALMOND; break;
		}
		r.setBorder(new Border(new BorderStroke(c, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 1.5, 0))));
		Tooltip t = (Tooltip) node.getUserData();
		t.setText(a.getError().message().messageText());
	}

}
