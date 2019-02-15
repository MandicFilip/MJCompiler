// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:31


package rs.ac.bg.etf.pp1.ast;

public class IfStart implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private IfConditionForm IfConditionForm;

    public IfStart (IfConditionForm IfConditionForm) {
        this.IfConditionForm=IfConditionForm;
        if(IfConditionForm!=null) IfConditionForm.setParent(this);
    }

    public IfConditionForm getIfConditionForm() {
        return IfConditionForm;
    }

    public void setIfConditionForm(IfConditionForm IfConditionForm) {
        this.IfConditionForm=IfConditionForm;
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
        if(IfConditionForm!=null) IfConditionForm.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IfConditionForm!=null) IfConditionForm.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IfConditionForm!=null) IfConditionForm.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IfStart(\n");

        if(IfConditionForm!=null)
            buffer.append(IfConditionForm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [IfStart]");
        return buffer.toString();
    }
}
