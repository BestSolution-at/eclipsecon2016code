package at.bestsolution.econ.typedaway.core;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.fx.code.editor.Constants;
import org.eclipse.fx.code.editor.SourceFileChange;
import org.eclipse.fx.code.editor.SourceFileInput;
import org.eclipse.fx.code.editor.services.URIProvider;
import org.eclipse.fx.core.di.ContextValue;
import org.eclipse.fx.core.event.EventBus;
import org.eclipse.fx.core.preferences.Preference;

import at.bestsolution.typescript.service.api.TSServer;
import at.bestsolution.typescript.service.api.TSServerFactory;
import at.bestsolution.typescript.service.api.model.CompilerOptions.Builder;
import at.bestsolution.typescript.service.api.services.LanguageService;
import at.bestsolution.typescript.service.api.services.ModelBuilderService;
import javafx.beans.property.Property;

@SuppressWarnings("restriction")
public class TypescriptManager {
	private TSServer server;
	private String rootPath;
	private EventBus eventBus;
	private LanguageService languageService;

	@Inject
	public TypescriptManager(EventBus eventBus, TSServerFactory factory, @ContextValue("at.bestsolution.typescript.service.api.TSServer") Property<TSServer> property) {
		server = factory.getServer("server", this::initServer);
		property.setValue(server);
		this.languageService = server.getService(LanguageService.class);
		this.eventBus = eventBus;
		this.eventBus.subscribe(Constants.TOPIC_SOURCE_FILE_INPUT_CREATED, EventBus.data(this::handleInputCreated));
		this.eventBus.subscribe(Constants.TOPIC_SOURCE_FILE_INPUT_DISPOSED, EventBus.data(this::handleInputDisposed));
		this.eventBus.subscribe(Constants.TOPIC_SOURCE_FILE_INPUT_MODIFIED, EventBus.data(this::handleInputChanged));
		this.eventBus.subscribe(Constants.TOPIC_SOURCE_FILE_INPUT_SAVED, EventBus.data(this::handleSavedChanged));
	}

	public static String filename(URIProvider uriProvider) {
		return uriProvider.getURI().substring("file:".length());
	}

	private void handleInputCreated(SourceFileInput input) {
		languageService.open(filename(input));
	}

	private void handleInputDisposed(SourceFileInput input) {
		languageService.close(filename(input));
	}

	private void handleInputChanged(SourceFileChange change) {
		languageService.modifyContent(filename(change.input), change.offset, change.length, change.replacement);
	}

	private void handleSavedChanged(SourceFileInput input) {
		System.err.println(languageService.getSemanticDiagnostics(filename(input)));
		languageService.updateContent(filename(input), input.getData());
	}


	private void initServer(TSServer server) {
		Builder options = server.getService(ModelBuilderService.class).createCompilerOptions(false);
		server.getService(LanguageService.class).initProject(options.build(), collectFiles());
	}

	private List<String> collectFiles() {
		if( this.rootPath != null ) {
			List<String> files = new ArrayList<>();
			try {
				Files.walkFileTree(Paths.get(this.rootPath), new SimpleFileVisitor<Path>() {

					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						if( Files.isRegularFile(file) && ( file.getFileName().toString().endsWith(".ts")) ) {
							files.add(file.toAbsolutePath().toString());
						}
						return FileVisitResult.CONTINUE;
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
			return files;
		}
		return Collections.emptyList();
	}

	@Inject
	public void setRootPath(@Preference(nodePath="at.bestsolution.econ.typedaway.core", key="rootPath") String rootPath) {
		this.rootPath = rootPath;
		if( server != null ) {
			LanguageService service = server.getService(LanguageService.class);
			collectFiles().stream().forEach(service::addFile);
		}
	}
}
