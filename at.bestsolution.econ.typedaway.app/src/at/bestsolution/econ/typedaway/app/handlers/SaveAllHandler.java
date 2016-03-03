package at.bestsolution.econ.typedaway.app.handlers;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

public class SaveAllHandler {
	@CanExecute
	public boolean canSaveAll(MWindow window, EModelService modelService) {
		return modelService.findElements(window, null, MPart.class, null, EModelService.ANYWHERE)
			.stream()
			.filter( e -> e.isDirty() )
			.findFirst().isPresent();
	}

	@Execute
	public void saveAll(MWindow window, EModelService modelService) {
		modelService.findElements(window, null, MPart.class, null, EModelService.ANYWHERE)
			.stream()
			.filter( e -> e.isDirty() )
			.forEach( e -> ContextInjectionFactory.invoke(e.getObject(), Persist.class, e.getContext()));
	}
}
