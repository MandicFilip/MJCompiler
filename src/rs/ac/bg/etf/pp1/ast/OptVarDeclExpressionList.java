// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:30


package rs.ac.bg.etf.pp1.ast;

public class OptVarDeclExpressionList extends VarDeclExpressionList {

    private VarDeclExpressionList VarDeclExpressionList;
    private VarDeclExpression VarDeclExpression;

    public OptVarDeclExpressionList (VarDeclExpressionList VarDeclExpressionList, VarDeclExpression VarDeclExpression) {
        this.VarDeclExpressionList=VarDeclExpressionList;
        if(VarDeclExpressionList!=null) VarDeclExpressionList.setParent(this);
        this.VarDeclExpression=VarDeclExpression;
        if(VarDeclExpression!=null) VarDeclExpression.setParent(this);
    }

    public VarDeclExpressionList getVarDeclExpressionList() {
        return VarDeclExpressionList;
    }

    public void setVarDeclExpressionList(VarDeclExpressionList VarDeclExpressionList) {
        this.VarDeclExpressionList=VarDeclExpressionList;
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
        if(VarDeclExpressionList!=null) VarDeclExpressionList.accept(visitor);
        if(VarDeclExpression!=null) VarDeclExpression.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclExpressionList!=null) VarDeclExpressionList.traverseTopDown(visitor);
        if(VarDeclExpression!=null) VarDeclExpression.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclExpressionList!=null) VarDeclExpressionList.traverseBottomUp(visitor);
        if(VarDeclExpression!=null) VarDeclExpression.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OptVarDeclExpressionList(\n");

        if(VarDeclExpressionList!=null)
            buffer.append(VarDeclExpressionList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclExpression!=null)
            buffer.append(VarDeclExpression.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OptVarDeclExpressionList]");
        return buffer.toString();
    }
}
