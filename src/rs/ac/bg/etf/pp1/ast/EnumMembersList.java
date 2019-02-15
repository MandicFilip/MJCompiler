// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:30


package rs.ac.bg.etf.pp1.ast;

public class EnumMembersList extends EnumList {

    private EnumList EnumList;
    private EnumMember EnumMember;

    public EnumMembersList (EnumList EnumList, EnumMember EnumMember) {
        this.EnumList=EnumList;
        if(EnumList!=null) EnumList.setParent(this);
        this.EnumMember=EnumMember;
        if(EnumMember!=null) EnumMember.setParent(this);
    }

    public EnumList getEnumList() {
        return EnumList;
    }

    public void setEnumList(EnumList EnumList) {
        this.EnumList=EnumList;
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
        if(EnumList!=null) EnumList.accept(visitor);
        if(EnumMember!=null) EnumMember.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumList!=null) EnumList.traverseTopDown(visitor);
        if(EnumMember!=null) EnumMember.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumList!=null) EnumList.traverseBottomUp(visitor);
        if(EnumMember!=null) EnumMember.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumMembersList(\n");

        if(EnumList!=null)
            buffer.append(EnumList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(EnumMember!=null)
            buffer.append(EnumMember.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumMembersList]");
        return buffer.toString();
    }
}
