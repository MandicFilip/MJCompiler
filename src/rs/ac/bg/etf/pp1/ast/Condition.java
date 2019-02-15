// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:31


package rs.ac.bg.etf.pp1.ast;

public class Condition extends OptCondition {

    private ConditionDecl ConditionDecl;

    public Condition (ConditionDecl ConditionDecl) {
        this.ConditionDecl=ConditionDecl;
        if(ConditionDecl!=null) ConditionDecl.setParent(this);
    }

    public ConditionDecl getConditionDecl() {
        return ConditionDecl;
    }

    public void setConditionDecl(ConditionDecl ConditionDecl) {
        this.ConditionDecl=ConditionDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConditionDecl!=null) ConditionDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConditionDecl!=null) ConditionDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConditionDecl!=null) ConditionDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Condition(\n");

        if(ConditionDecl!=null)
            buffer.append(ConditionDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Condition]");
        return buffer.toString();
    }
}
