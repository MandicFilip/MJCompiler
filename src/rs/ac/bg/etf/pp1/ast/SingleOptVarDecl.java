// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:30


package rs.ac.bg.etf.pp1.ast;

public class SingleOptVarDecl extends VarDeclExpressionList {

    private VarDeclExpression VarDeclExpression;

    public SingleOptVarDecl (VarDeclExpression VarDeclExpression) {
        this.VarDeclExpression=VarDeclExpression;
        if(VarDeclExpression!=null) VarDeclExpression.setParent(this);
    }

    public VarDeclExpression getVarDeclExpression() {
        return VarDeclExpression;
    }

    public void setVarDeclExpression(VarDeclExpression VarDeclExpression) {
        this.VarDeclExpression=VarDeclExpression;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclExpression!=null) VarDeclExpression.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclExpression!=null) VarDeclExpression.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclExpression!=null) VarDeclExpression.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleOptVarDecl(\n");

        if(VarDeclExpression!=null)
            buffer.append(VarDeclExpression.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleOptVarDecl]");
        return buffer.toString();
    }
}
