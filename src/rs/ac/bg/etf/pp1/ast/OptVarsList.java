// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:30


package rs.ac.bg.etf.pp1.ast;

public class OptVarsList extends OptVars {

    private VarDeclExpressionList VarDeclExpressionList;

    public OptVarsList (VarDeclExpressionList VarDeclExpressionList) {
        this.VarDeclExpressionList=VarDeclExpressionList;
        if(VarDeclExpressionList!=null) VarDeclExpressionList.setParent(this);
    }

    public VarDeclExpressionList getVarDeclExpressionList() {
        return VarDeclExpressionList;
    }

    public void setVarDeclExpressionList(VarDeclExpressionList VarDeclExpressionList) {
        this.VarDeclExpressionList=VarDeclExpressionList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclExpressionList!=null) VarDeclExpressionList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclExpressionList!=null) VarDeclExpressionList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclExpressionList!=null) VarDeclExpressionList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OptVarsList(\n");

        if(VarDeclExpressionList!=null)
            buffer.append(VarDeclExpressionList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OptVarsList]");
        return buffer.toString();
    }
}
