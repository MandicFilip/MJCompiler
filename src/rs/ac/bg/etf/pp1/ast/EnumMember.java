// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:30


package rs.ac.bg.etf.pp1.ast;

public class EnumMember implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String enumMemberName;
    private EnumInit EnumInit;

    public EnumMember (String enumMemberName, EnumInit EnumInit) {
        this.enumMemberName=enumMemberName;
        this.EnumInit=EnumInit;
        if(EnumInit!=null) EnumInit.setParent(this);
    }

    public String getEnumMemberName() {
        return enumMemberName;
    }

    public void setEnumMemberName(String enumMemberName) {
        this.enumMemberName=enumMemberName;
    }

    public EnumInit getEnumInit() {
        return EnumInit;
    }

    public void setEnumInit(EnumInit EnumInit) {
        this.EnumInit=EnumInit;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(EnumInit!=null) EnumInit.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumInit!=null) EnumInit.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumInit!=null) EnumInit.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumMember(\n");

        buffer.append(" "+tab+enumMemberName);
        buffer.append("\n");

        if(EnumInit!=null)
            buffer.append(EnumInit.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumMember]");
        return buffer.toString();
    }
}
