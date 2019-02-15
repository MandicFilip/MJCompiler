// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:31


package rs.ac.bg.etf.pp1.ast;

public class TermListCondition extends ConditionDecl {

    private ConditionDecl ConditionDecl;
    private CondTerm CondTerm;

    public TermListCondition (ConditionDecl ConditionDecl, CondTerm CondTerm) {
        this.ConditionDecl=ConditionDecl;
        if(ConditionDecl!=null) ConditionDecl.setParent(this);
        this.CondTerm=CondTerm;
        if(CondTerm!=null) CondTerm.setParent(this);
    }

    public ConditionDecl getConditionDecl() {
        return ConditionDecl;
    }

    public void setConditionDecl(ConditionDecl ConditionDecl) {
        this.ConditionDecl=ConditionDecl;
    }

    public CondTerm getCondTerm() {
        return CondTerm;
    }

    public void setCondTerm(CondTerm CondTerm) {
        this.CondTerm=CondTerm;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConditionDecl!=null) ConditionDecl.accept(visitor);
        if(CondTerm!=null) CondTerm.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConditionDecl!=null) ConditionDecl.traverseTopDown(visitor);
        if(CondTerm!=null) CondTerm.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConditionDecl!=null) ConditionDecl.traverseBottomUp(visitor);
        if(CondTerm!=null) CondTerm.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("TermListCondition(\n");

        if(ConditionDecl!=null)
            buffer.append(ConditionDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondTerm!=null)
            buffer.append(CondTerm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [TermListCondition]");
        return buffer.toString();
    }
}
