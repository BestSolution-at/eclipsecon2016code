package at.bestsolution.econ.typedaway.ui.marker;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.fx.core.URI;
import org.eclipse.fx.text.ui.source.ILineRulerAnnotationPresenter;
import org.eclipse.fx.ui.services.resources.GraphicsLoader;
import org.eclipse.jface.text.source.Annotation;

import at.bestsolution.econ.typedaway.core.marker.TypescriptAnnotation;
import at.bestsolution.typescript.service.api.model.Diagnostic;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

@SuppressWarnings("restriction")
public class TypescriptAnnotationLineRulerStatusIcon implements ILineRulerAnnotationPresenter {

	private GraphicsLoader loader;

	public TypescriptAnnotationLineRulerStatusIcon(GraphicsLoader loader) {
		this.loader = loader;
	}

	@Override
	public LayoutHint getLayoutHint() {
		return LayoutHint.ALIGN_CENTER;
	}

	@Override
	public int getOrder() {
		return 50;
	}

	@Override
	public boolean isApplicable(Annotation annotation) {
		return annotation instanceof TypescriptAnnotation;
	}

	@Override
	public Node createNode() {
		ImageView i = new ImageView();
		Tooltip tt = new Tooltip();
		Tooltip.install(i, tt);
		i.setUserData(tt);
		return i;

//		Label label = new Label();
//		label.setGraphic(new ImageView());
//		label.setTooltip(new Tooltip());
//		return label;
	}

	private Comparator<Diagnostic> importance = (Diagnostic o1, Diagnostic o2) -> {
		return Integer.compare(o2.category().ordinal(), o1.category().ordinal());
	};

	private Image taskImage;
	private Image errorImage;
	private Image warningImage;
	private Image infoImage;

	private Image getTaskImage() {
		if (taskImage == null) {
			taskImage = loader.getImage(URI.createPlatformPluginURI("at.bestsolution.econ.typedaway.ui", "css/icons/16/showtsk_tsk.png"));
		}
		return taskImage;
	}

	private Image getErrorImage() {
		if (errorImage == null) {
			errorImage = loader.getImage(URI.createPlatformPluginURI("at.bestsolution.econ.typedaway.ui", "css/icons/16/message_error.png"));
		}
		return errorImage;
	}

	private Image getWarningImage() {
		if (warningImage == null) {
			warningImage = loader.getImage(URI.createPlatformPluginURI("at.bestsolution.econ.typedaway.ui", "css/icons/16/message_warning.png"));
		}
		return warningImage;
	}

	private Image getInfoImage() {
		if (infoImage == null) {
			infoImage = loader.getImage(URI.createPlatformPluginURI("at.bestsolution.econ.typedaway.ui", "css/icons/16/message_info.png"));
		}
		return infoImage;
	}

	private Image getImage(Diagnostic error) {
		Image image = null;
		switch (error.category()) {
			case ERROR:
				image = getErrorImage();
				break;
			case MESSAGE:
				image = getInfoImage();
				break;
			case WARNING:
				image = getWarningImage();
				break;
			default:
				break;
			}
		return image;
	}

	@SuppressWarnings("unused")
	private void updateLabel(Label node, Set<Diagnostic> annotation) {
		Image image = null;

		List<Diagnostic> byImportance = annotation.stream().sorted(importance).collect(Collectors.toList());

		if (!byImportance.isEmpty()) {
			image = getImage(byImportance.get(0));
		}
		((ImageView)node.getGraphic()).setImage(image);

		String message = computeMessage(byImportance);
		if (message == null || message.isEmpty()) {
			node.setTooltip(null);
		}
		else {
			// attaching and detaching is expensive - but there is no api to hide empty tooltips -.-
			Tooltip tt = node.getTooltip();
			if (tt == null){
				tt = new Tooltip(message);
				node.setTooltip(tt);
			}
			else {
				tt.setText(message);
			}
		}

	}

	private void updateImageView(ImageView node, Set<TypescriptAnnotation> annotation) {
		Image image = null;

		List<Diagnostic> byImportance = annotation.stream().map(TypescriptAnnotation::getError).sorted(importance).collect(Collectors.toList());

		if (!byImportance.isEmpty()) {
			image = getImage(byImportance.get(0));
		}
		node.setImage(image);



		String message = computeMessage(byImportance);
		if (message == null || message.isEmpty()) {
			Tooltip tt = (Tooltip) node.getUserData();
			tt.setText("");
		}
		else {
			// attaching and detaching is expensive - but there is no api to hide empty tooltips -.-
			Tooltip tt = (Tooltip) node.getUserData();
			tt.setText(message);
		}

	}

	@Override
	public void updateNode(Node node, Set<Annotation> annotation) {
		updateImageView((ImageView) node, annotation.stream().map(a->(TypescriptAnnotation) a).collect(Collectors.toSet()));
//		updateLabel((Label) node, annotation.stream().map(a->(DartAnnotation) a).collect(Collectors.toSet()));
	}

	private String computeMessage(List<Diagnostic> byImportance) {
		return byImportance.stream().map(e-> e.category() + ": " + e.message().messageText()).collect(Collectors.joining("\n"));
	}
}
