// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:30


package rs.ac.bg.etf.pp1.ast;

public class ConstTypeDeclaration extends DeclType {

    private ConstDeclExpression ConstDeclExpression;

    public ConstTypeDeclaration (ConstDeclExpression ConstDeclExpression) {
        this.ConstDeclExpression=ConstDeclExpression;
        if(ConstDeclExpression!=null) ConstDeclExpression.setParent(this);
    }

    public ConstDeclExpression getConstDeclExpression() {
        return ConstDeclExpression;
    }

    public void setConstDeclExpression(ConstDeclExpression ConstDeclExpression) {
        this.ConstDeclExpression=ConstDeclExpression;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstDeclExpression!=null) ConstDeclExpression.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclExpression!=null) ConstDeclExpression.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclExpression!=null) ConstDeclExpression.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstTypeDeclaration(\n");

        if(ConstDeclExpression!=null)
            buffer.append(ConstDeclExpression.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstTypeDeclaration]");
        return buffer.toString();
    }
}
