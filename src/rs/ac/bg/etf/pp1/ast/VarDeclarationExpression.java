// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:30


package rs.ac.bg.etf.pp1.ast;

public class VarDeclarationExpression extends VarDeclExpression {

    private Type Type;
    private VarsList VarsList;

    public VarDeclarationExpression (Type Type, VarsList VarsList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.VarsList=VarsList;
        if(VarsList!=null) VarsList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public VarsList getVarsList() {
        return VarsList;
    }

    public void setVarsList(VarsList VarsList) {
        this.VarsList=VarsList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(VarsList!=null) VarsList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(VarsList!=null) VarsList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(VarsList!=null) VarsList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclarationExpression(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarsList!=null)
            buffer.append(VarsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclarationExpression]");
        return buffer.toString();
    }
}
