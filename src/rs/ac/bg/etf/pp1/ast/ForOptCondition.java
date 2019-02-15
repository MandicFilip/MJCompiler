// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:31


package rs.ac.bg.etf.pp1.ast;

public class ForOptCondition implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private OptCondition OptCondition;

    public ForOptCondition (OptCondition OptCondition) {
        this.OptCondition=OptCondition;
        if(OptCondition!=null) OptCondition.setParent(this);
    }

    public OptCondition getOptCondition() {
        return OptCondition;
    }

    public void setOptCondition(OptCondition OptCondition) {
        this.OptCondition=OptCondition;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OptCondition!=null) OptCondition.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptCondition!=null) OptCondition.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptCondition!=null) OptCondition.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ForOptCondition(\n");

        if(OptCondition!=null)
            buffer.append(OptCondition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ForOptCondition]");
        return buffer.toString();
    }
}
