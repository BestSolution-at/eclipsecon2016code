package at.bestsolution.econ.typedaway.app.handlers;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

public class SaveHandler {

	@CanExecute
	public boolean canSave(MPart part) {
		return part.isDirty();
	}

	@Execute
	public void save(MPart activePart) {
		ContextInjectionFactory.invoke(activePart.getObject(),Persist.class,activePart.getContext());
	}
}
