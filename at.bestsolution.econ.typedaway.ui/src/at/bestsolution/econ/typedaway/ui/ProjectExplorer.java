package at.bestsolution.econ.typedaway.ui;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.fx.code.editor.services.EditorOpener;
import org.eclipse.fx.core.preferences.Preference;
import org.eclipse.fx.ui.controls.filesystem.DirItem;
import org.eclipse.fx.ui.controls.filesystem.FileItem;
import org.eclipse.fx.ui.controls.filesystem.ResourceEvent;
import org.eclipse.fx.ui.controls.filesystem.ResourceItem;
import org.eclipse.fx.ui.controls.filesystem.ResourceTreeView;
import org.eclipse.fx.ui.controls.filesystem.RootDirItem;

import at.bestsolution.typescript.service.api.TSServer;
import at.bestsolution.typescript.service.api.services.LanguageService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;

@SuppressWarnings("restriction")
public class ProjectExplorer {
	private final TSServer server;
	private ResourceTreeView viewer;
	private ObservableList<DirItem> rootList;

	@Inject
	public ProjectExplorer(TSServer server) {
		this.server = server;
		this.rootList = FXCollections.observableArrayList();
	}

	@PostConstruct
	void init(BorderPane parent, EditorOpener editorOpener) {
		viewer = new ResourceTreeView();
		viewer.setRootDirectories(rootList);
		viewer.addEventHandler(ResourceEvent.openResourceEvent(), e -> {
			e.getResourceItems()
				.stream()
				.filter( r -> r instanceof FileItem )
				.map( ResourceItem::getUri )
				.forEach( editorOpener::openEditor );
		});
		parent.setCenter(viewer);
	}

	@Inject
	public void setRootPath( @Preference(key="rootPath") String path ) {
		if( path != null ) {
			RootDirItem item = ResourceItem.createObservedPath(Paths.get(path));
			item.getChildren().addListener( this::handleDirContentChanged );
			rootList.add(item);
		}
	}

	private void handleDirContentChanged(javafx.collections.ListChangeListener.Change<? extends ResourceItem> c) {
		while( c.next() ) {
			LanguageService service = server.getService(LanguageService.class);
			if( c.wasAdded() ) {
				filterFileStream(c.getAddedSubList()).forEach( service::addFile );
			} else if( c.wasRemoved() ) {
				filterFileStream(c.getRemoved()).forEach( service::removeFile );

			}
		}
	}

	private Stream<String> filterFileStream(List<? extends ResourceItem> list) {
		return list.stream()
				.filter( r -> r instanceof FileItem )
				.map( ResourceItem::getNativeResourceObject )
				.map( r -> (Path)r )
				.map( Path::toAbsolutePath )
				.map( Object::toString )
				.filter( s -> s.endsWith(".ts"));
	}
}