package at.bestsolution.econ.typedaway.core.complete;

import org.eclipse.fx.code.editor.Input;
import org.eclipse.fx.code.editor.services.ProposalComputer;
import org.eclipse.fx.code.editor.services.ProposalComputerTypeProvider;
import org.eclipse.fx.code.editor.services.URIProvider;

@SuppressWarnings("restriction")
//TODO 3: Register this provide as an OSGi-Service
public class TypescriptProposalComputerProvider implements ProposalComputerTypeProvider {

	@Override
	public Class<? extends ProposalComputer> getType(Input<?> s) {
		return TypescriptProposalComputer.class;
	}

	@Override
	public boolean test(Input<?> input) {
		return ((URIProvider)input).getURI().endsWith(".ts");
	}

}
