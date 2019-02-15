// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:30


package rs.ac.bg.etf.pp1.ast;

public class SingleEnumMember extends EnumList {

    private EnumMember EnumMember;

    public SingleEnumMember (EnumMember EnumMember) {
        this.EnumMember=EnumMember;
        if(EnumMember!=null) EnumMember.setParent(this);
    }

    public EnumMember getEnumMember() {
        return EnumMember;
    }

    public void setEnumMember(EnumMember EnumMember) {
        this.EnumMember=EnumMember;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(EnumMember!=null) EnumMember.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumMember!=null) EnumMember.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumMember!=null) EnumMember.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleEnumMember(\n");

        if(EnumMember!=null)
            buffer.append(EnumMember.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleEnumMember]");
        return buffer.toString();
    }
}
