// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:31


package rs.ac.bg.etf.pp1.ast;

public class FunCallFactor extends Factor {

    private MethodCall MethodCall;

    public FunCallFactor (MethodCall MethodCall) {
        this.MethodCall=MethodCall;
        if(MethodCall!=null) MethodCall.setParent(this);
    }

    public MethodCall getMethodCall() {
        return MethodCall;
    }

    public void setMethodCall(MethodCall MethodCall) {
        this.MethodCall=MethodCall;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodCall!=null) MethodCall.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodCall!=null) MethodCall.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodCall!=null) MethodCall.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FunCallFactor(\n");

        if(MethodCall!=null)
            buffer.append(MethodCall.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FunCallFactor]");
        return buffer.toString();
    }
}
