package at.bestsolution.econ.typedaway.ui;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.fx.core.preferences.Preference;
import org.eclipse.fx.ui.controls.dialog.TitleAreaDialog;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

@SuppressWarnings("restriction")
public class NewFile {
	@Execute
	public void createNewFile() {
		//TODO 1 Open the new file dialog
	}

	static class NewFileDialog extends TitleAreaDialog {
		private final String path;
		private TextField textField;

		@Inject
		public NewFileDialog(@Preference(nodePath="at.bestsolution.econ.typedaway.core", key="rootPath") String path) {
			super("New Tile", "New File", "Enter the name of the new typescript file");
			this.path = path;
			setClientArea(createClientArea());
			addDefaultButtons();
		}

		private Node createClientArea() {
			HBox box = new HBox(5);
			box.setAlignment(Pos.CENTER_LEFT);
			box.getChildren().add(new Label("Name"));
			box.getChildren().add(textField = new TextField());
			HBox.setHgrow(textField, Priority.ALWAYS);
			return box;
		}

		@Override
		protected void handleOk() {
			File f = new File(path, textField.getText() + ".ts");
			try {
				f.createNewFile();
				super.handleOk();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
