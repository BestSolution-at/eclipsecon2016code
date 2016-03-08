package at.bestsolution.econ.typedaway.core.complete;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.eclipse.fx.code.editor.services.CompletionProposal;
import org.eclipse.fx.code.editor.services.ProposalComputer;
import org.eclipse.fx.code.editor.services.URIProvider;
import org.eclipse.jface.text.BadLocationException;

import at.bestsolution.typescript.service.api.TSServer;
import at.bestsolution.typescript.service.api.model.CompletionEntry;
import at.bestsolution.typescript.service.api.model.CompletionEntryDetails;
import at.bestsolution.typescript.service.api.model.CompletionInfo;
import at.bestsolution.typescript.service.api.model.ScriptElementKind;
import at.bestsolution.typescript.service.api.services.LanguageService;

@SuppressWarnings("restriction")
public class TypescriptProposalComputer implements ProposalComputer {
	private LanguageService languageService;

	@Inject
	public TypescriptProposalComputer(TSServer server) {
		this.languageService = server.getService(LanguageService.class);
	}

	@Override
	public CompletableFuture<List<CompletionProposal>> compute(ProposalContext context) {
		String uri = ((URIProvider)context.input).getURI().substring("file:".length());
		CompletionInfo completionInfo = this.languageService.getCompletionsAtPosition(uri, context.location);

		Stream<? extends CompletionEntry> entryStream;
		int location;
		String prefix;
		if( completionInfo.isMemberCompletion() ) {
			location = context.location - 1;
			try {
				while( location >= 0 && context.document.getChar(location) != '.' ) {
					location--;
				}

				location = location < 0 ? context.location : (location + 1);
				prefix = context.document.get(location, context.location - location);
			} catch (BadLocationException e1) {
				location = context.location;
				prefix = "";
			}
			entryStream = completionInfo.entries().stream();
		} else {
			location = context.location;
			try {
				while( location >= 0 && ! Character.isWhitespace(context.document.getChar(location)) ) {
					location--;
				}
				location = location < 0 ? context.location : location;
				prefix = context.document.get(location, context.location - location);
			} catch (BadLocationException e1) {
				location = context.location;
				prefix = "";
			}
			entryStream = completionInfo.entries().stream();
		}

		int fLocation = location;
		int replacementLength = context.location - location;
		String fPrefix = prefix;

		List<CompletionProposal> proposalList = entryStream
				.filter( e -> e.kind() != ScriptElementKind.WARNING )
				.filter( e -> replacementLength == 0 || e.name().startsWith(fPrefix))
				.map( e -> this.languageService.getCompletionEntryDetails(uri, context.location, e.name()))
				.filter( d -> d != null)
				.map( d -> toProposal(d, context, fLocation, replacementLength))
				.filter( p -> p != null)
				.collect(Collectors.toList());

		return CompletableFuture.completedFuture(proposalList);
	}

	private CompletionProposal toProposal(CompletionEntryDetails d, ProposalContext context, int location, int replacementLength) {
		if( ScriptElementKind.MEMBER_FUNCTION_ELEMENT == d.kind() ) {
			return new CompletionProposal.BaseCompletetionProposal(d.name(), location, replacementLength, d.name(), null);
		} else if( ScriptElementKind.MEMBER_VARIABLE_ELEMENT == d.kind() ) {
			return new CompletionProposal.BaseCompletetionProposal(d.name(), location, replacementLength, d.name(), null);
		} else if( ScriptElementKind.FUNCTION_ELEMENT == d.kind() ) {
			return new CompletionProposal.BaseCompletetionProposal(d.name(), location, replacementLength, d.name(), null);
		} else if( ScriptElementKind.CLASS_ELEMENT == d.kind() ) {
			return new CompletionProposal.BaseCompletetionProposal(d.name(), location, replacementLength, d.name(), null);
		} else if( ScriptElementKind.KEYWORD == d.kind() ) {
			return new CompletionProposal.BaseCompletetionProposal(d.name(), location, replacementLength, d.name(), null);
		}

		return null;
	}
}
