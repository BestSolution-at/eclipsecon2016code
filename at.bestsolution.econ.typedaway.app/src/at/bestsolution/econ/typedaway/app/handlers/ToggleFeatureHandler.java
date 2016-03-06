package at.bestsolution.econ.typedaway.app.handlers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.fx.code.editor.Constants;
import org.eclipse.fx.core.preferences.Preference;
import org.eclipse.fx.text.ui.Feature;

import javafx.beans.property.Property;

@SuppressWarnings("restriction")
public class ToggleFeatureHandler {

	@Execute
	public void execute(@Named("feature") String featureName, @Preference(key=Constants.PREFERENCE_KEY_EDITOR_FEATURE,nodePath=Constants.PREFERENCE_NODE_PATH) Property<Set<Feature>> featureSet) {
		Feature f = Feature.valueOf(featureName);

		Set<Feature> copy = new HashSet<>(featureSet.getValue() == null ?
				Collections.emptySet() : featureSet.getValue());
		if( copy.contains(f) ) {
			copy.remove(f);
		} else {
			copy.add(f);
		}
		featureSet.setValue(copy);
	}
}