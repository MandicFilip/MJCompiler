// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:31


package rs.ac.bg.etf.pp1.ast;

public class PrintStatement extends Statement {

    private ExpressionToPrint ExpressionToPrint;

    public PrintStatement (ExpressionToPrint ExpressionToPrint) {
        this.ExpressionToPrint=ExpressionToPrint;
        if(ExpressionToPrint!=null) ExpressionToPrint.setParent(this);
    }

    public ExpressionToPrint getExpressionToPrint() {
        return ExpressionToPrint;
    }

    public void setExpressionToPrint(ExpressionToPrint ExpressionToPrint) {
        this.ExpressionToPrint=ExpressionToPrint;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExpressionToPrint!=null) ExpressionToPrint.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExpressionToPrint!=null) ExpressionToPrint.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExpressionToPrint!=null) ExpressionToPrint.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("PrintStatement(\n");

        if(ExpressionToPrint!=null)
            buffer.append(ExpressionToPrint.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [PrintStatement]");
        return buffer.toString();
    }
}
