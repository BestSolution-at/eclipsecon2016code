package at.bestsolution.econ.typedaway.ui;

import java.io.File;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.fx.core.preferences.Preference;

import javafx.beans.property.Property;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class SelectProjectDirectory {

	@Execute
	public void select(Stage stage, @Preference(key="rootPath") Property<String> rootPath) {
		DirectoryChooser chooser = new DirectoryChooser();
		File f = chooser.showDialog(stage);
		if( f != null ) {
			rootPath.setValue(f.getAbsolutePath().toString());
		}
	}
}
