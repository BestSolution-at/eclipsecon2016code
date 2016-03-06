package at.bestsolution.econ.typedaway.core.marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.fx.code.editor.Constants;
import org.eclipse.fx.code.editor.Input;
import org.eclipse.fx.code.editor.SourceFileChange;
import org.eclipse.fx.code.editor.services.URIProvider;
import org.eclipse.fx.core.Subscription;
import org.eclipse.fx.core.ThreadSynchronize;
import org.eclipse.fx.core.event.EventBus;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationModel;

import at.bestsolution.econ.typedaway.core.TypescriptManager;
import at.bestsolution.typescript.service.api.TSServer;
import at.bestsolution.typescript.service.api.model.Diagnostic;
import at.bestsolution.typescript.service.api.services.LanguageService;

@SuppressWarnings("restriction")
public class TypescriptAnnotationModel extends AnnotationModel {
	private final ThreadSynchronize synchronize;
	private Subscription delayedRequest;
	private final LanguageService languageService;
	private String filename;

	@Inject
	public TypescriptAnnotationModel(TSServer server, Input<?> input, EventBus eventBus, ThreadSynchronize synchronize) {
		this.synchronize = synchronize;
		this.filename = TypescriptManager.filename((URIProvider) input);
		this.languageService = server.getService(LanguageService.class);
		eventBus.subscribe(Constants.TOPIC_SOURCE_FILE_INPUT_MODIFIED, EventBus.data(this::handleFileChanged));

		updateErrorMarkers();
//		// Subscribe to errors
//		ServiceAnalysis service = server.getService(ServiceAnalysis.class);
//		subscription = service.errors(this::accept);
//
//		CompletableFuture.supplyAsync(
//				() -> service.getErrors(file.toString())).thenAccept(this::accept);

	}

	private void handleFileChanged(SourceFileChange change) {
		if( delayedRequest != null) {
			delayedRequest.dispose();
		}
		delayedRequest = synchronize.scheduleExecution(500, this::updateErrorMarkers );
	}

	private void updateErrorMarkers() {
		Iterator<?> annotationIterator = getAnnotationIterator();
		List<Annotation> removed = new ArrayList<>();
		while( annotationIterator.hasNext() ) {
			Object next = annotationIterator.next();
			if( next instanceof TypescriptAnnotation) {
				removed.add((Annotation) next);
			}
		}

		List<Diagnostic> list = languageService.getSyntacticDiagnostics(filename);

		Map<Annotation,Position> added = new HashMap<>();
		for( Diagnostic e : list ) {
			added.put(new TypescriptAnnotation(e),new Position(e.start(), e.length()));
		}
		replaceAnnotations(removed.toArray(new Annotation[0]), added);
	}

	@PreDestroy
	void cleanup() {
		if( delayedRequest != null ) {
			delayedRequest.dispose();
			delayedRequest = null;
		}
	}
}
