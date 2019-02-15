// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:30


package rs.ac.bg.etf.pp1.ast;

public class ALLDeclarationsList extends AllDeclList {

    private AllDeclList AllDeclList;
    private DeclType DeclType;

    public ALLDeclarationsList (AllDeclList AllDeclList, DeclType DeclType) {
        this.AllDeclList=AllDeclList;
        if(AllDeclList!=null) AllDeclList.setParent(this);
        this.DeclType=DeclType;
        if(DeclType!=null) DeclType.setParent(this);
    }

    public AllDeclList getAllDeclList() {
        return AllDeclList;
    }

    public void setAllDeclList(AllDeclList AllDeclList) {
        this.AllDeclList=AllDeclList;
    }

    public DeclType getDeclType() {
        return DeclType;
    }

    public void setDeclType(DeclType DeclType) {
        this.DeclType=DeclType;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AllDeclList!=null) AllDeclList.accept(visitor);
        if(DeclType!=null) DeclType.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AllDeclList!=null) AllDeclList.traverseTopDown(visitor);
        if(DeclType!=null) DeclType.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AllDeclList!=null) AllDeclList.traverseBottomUp(visitor);
        if(DeclType!=null) DeclType.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ALLDeclarationsList(\n");

        if(AllDeclList!=null)
            buffer.append(AllDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DeclType!=null)
            buffer.append(DeclType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ALLDeclarationsList]");
        return buffer.toString();
    }
}
