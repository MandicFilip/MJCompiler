// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:31


package rs.ac.bg.etf.pp1.ast;

public class NoActualParametarsList extends ActualPars {

    public NoActualParametarsList () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NoActualParametarsList(\n");

        buffer.append(tab);
        buffer.append(") [NoActualParametarsList]");
        return buffer.toString();
    }
}