// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:30


package rs.ac.bg.etf.pp1.ast;

public class VarDeclaration extends VarDecl {

    private String varName;
    private ArrOpt ArrOpt;

    public VarDeclaration (String varName, ArrOpt ArrOpt) {
        this.varName=varName;
        this.ArrOpt=ArrOpt;
        if(ArrOpt!=null) ArrOpt.setParent(this);
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName=varName;
    }

    public ArrOpt getArrOpt() {
        return ArrOpt;
    }

    public void setArrOpt(ArrOpt ArrOpt) {
        this.ArrOpt=ArrOpt;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ArrOpt!=null) ArrOpt.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ArrOpt!=null) ArrOpt.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ArrOpt!=null) ArrOpt.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclaration(\n");

        buffer.append(" "+tab+varName);
        buffer.append("\n");

        if(ArrOpt!=null)
            buffer.append(ArrOpt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclaration]");
        return buffer.toString();
    }
}
