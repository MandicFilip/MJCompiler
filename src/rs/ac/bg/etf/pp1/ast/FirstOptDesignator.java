// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:31


package rs.ac.bg.etf.pp1.ast;

public class FirstOptDesignator implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private OptDesignatorStatement OptDesignatorStatement;

    public FirstOptDesignator (OptDesignatorStatement OptDesignatorStatement) {
        this.OptDesignatorStatement=OptDesignatorStatement;
        if(OptDesignatorStatement!=null) OptDesignatorStatement.setParent(this);
    }

    public OptDesignatorStatement getOptDesignatorStatement() {
        return OptDesignatorStatement;
    }

    public void setOptDesignatorStatement(OptDesignatorStatement OptDesignatorStatement) {
        this.OptDesignatorStatement=OptDesignatorStatement;
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
        if(OptDesignatorStatement!=null) OptDesignatorStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptDesignatorStatement!=null) OptDesignatorStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptDesignatorStatement!=null) OptDesignatorStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FirstOptDesignator(\n");

        if(OptDesignatorStatement!=null)
            buffer.append(OptDesignatorStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FirstOptDesignator]");
        return buffer.toString();
    }
}
