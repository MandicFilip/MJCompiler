// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:31


package rs.ac.bg.etf.pp1.ast;

public class SingleTermExpr extends Expr {

    private SignTerm SignTerm;

    public SingleTermExpr (SignTerm SignTerm) {
        this.SignTerm=SignTerm;
        if(SignTerm!=null) SignTerm.setParent(this);
    }

    public SignTerm getSignTerm() {
        return SignTerm;
    }

    public void setSignTerm(SignTerm SignTerm) {
        this.SignTerm=SignTerm;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(SignTerm!=null) SignTerm.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(SignTerm!=null) SignTerm.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(SignTerm!=null) SignTerm.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleTermExpr(\n");

        if(SignTerm!=null)
            buffer.append(SignTerm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleTermExpr]");
        return buffer.toString();
    }
}
