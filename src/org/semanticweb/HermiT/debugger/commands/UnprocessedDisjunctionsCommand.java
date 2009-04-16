package org.semanticweb.HermiT.debugger.commands;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import org.semanticweb.HermiT.model.DLPredicate;
import org.semanticweb.HermiT.model.Equality;
import org.semanticweb.HermiT.tableau.GroundDisjunction;
import org.semanticweb.HermiT.debugger.Debugger;

public class UnprocessedDisjunctionsCommand extends AbstractCommand {

    public UnprocessedDisjunctionsCommand(Debugger debugger) {
        super(debugger);
    }
    public String getCommandName() {
        return "uDisjunctions";
    }
    public String[] getDescription() {
        return new String[] { "","prints unprocessed ground disjunctions" };
    }
    public void printHelp(PrintWriter writer) {
        writer.println("usage: uDisjunctions");
        writer.println("Prints a list of unprocessed ground disjunctions. ");
    }
    public void execute(String[] args) {
        CharArrayWriter buffer=new CharArrayWriter();
        PrintWriter writer=new PrintWriter(buffer);
        writer.println("Unprocessed ground disjunctions");
        writer.println("===========================================");
        GroundDisjunction groundDisjunction=m_debugger.getTableau().getFirstUnprocessedGroundDisjunction();
        while (groundDisjunction!=null) {
            for (int disjunctIndex=0;disjunctIndex<groundDisjunction.getNumberOfDisjuncts();disjunctIndex++) {
                if (disjunctIndex!=0)
                    writer.print(" v ");
                DLPredicate dlPredicate=groundDisjunction.getDLPredicate(disjunctIndex);
                if (Equality.INSTANCE.equals(dlPredicate)) {
                    writer.print(groundDisjunction.getArgument(disjunctIndex,0).getNodeID());
                    writer.print(" == ");
                    writer.print(groundDisjunction.getArgument(disjunctIndex,1).getNodeID());
                }
                else {
                    writer.print(dlPredicate.toString(m_debugger.getPrefixes()));
                    writer.print('(');
                    for (int argumentIndex=0;argumentIndex<dlPredicate.getArity();argumentIndex++) {
                        if (argumentIndex!=0)
                            buffer.append(',');
                        writer.print(groundDisjunction.getArgument(disjunctIndex,argumentIndex).getNodeID());
                    }
                    writer.print(')');
                }
            }
            writer.println();
            groundDisjunction=groundDisjunction.getPreviousGroundDisjunction();
        }
        writer.flush();
        showTextInWindow(buffer.toString(),"Unprocessed ground disjunctions");
        selectConsoleWindow();
    }
}
