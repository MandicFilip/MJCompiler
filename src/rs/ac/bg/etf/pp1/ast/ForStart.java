// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:31


package rs.ac.bg.etf.pp1.ast;

public class ForStart implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private FirstOptDesignator FirstOptDesignator;
    private ForOptCondition ForOptCondition;
    private SecondOptDesignator SecondOptDesignator;

    public ForStart (FirstOptDesignator FirstOptDesignator, ForOptCondition ForOptCondition, SecondOptDesignator SecondOptDesignator) {
        this.FirstOptDesignator=FirstOptDesignator;
        if(FirstOptDesignator!=null) FirstOptDesignator.setParent(this);
        this.ForOptCondition=ForOptCondition;
        if(ForOptCondition!=null) ForOptCondition.setParent(this);
        this.SecondOptDesignator=SecondOptDesignator;
        if(SecondOptDesignator!=null) SecondOptDesignator.setParent(this);
    }

    public FirstOptDesignator getFirstOptDesignator() {
        return FirstOptDesignator;
    }

    public void setFirstOptDesignator(FirstOptDesignator FirstOptDesignator) {
        this.FirstOptDesignator=FirstOptDesignator;
    }

    public ForOptCondition getForOptCondition() {
        return ForOptCondition;
    }

    public void setForOptCondition(ForOptCondition ForOptCondition) {
        this.ForOptCondition=ForOptCondition;
    }

    public SecondOptDesignator getSecondOptDesignator() {
        return SecondOptDesignator;
    }

    public void setSecondOptDesignator(SecondOptDesignator SecondOptDesignator) {
        this.SecondOptDesignator=SecondOptDesignator;
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
        if(FirstOptDesignator!=null) FirstOptDesignator.accept(visitor);
        if(ForOptCondition!=null) ForOptCondition.accept(visitor);
        if(SecondOptDesignator!=null) SecondOptDesignator.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FirstOptDesignator!=null) FirstOptDesignator.traverseTopDown(visitor);
        if(ForOptCondition!=null) ForOptCondition.traverseTopDown(visitor);
        if(SecondOptDesignator!=null) SecondOptDesignator.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FirstOptDesignator!=null) FirstOptDesignator.traverseBottomUp(visitor);
        if(ForOptCondition!=null) ForOptCondition.traverseBottomUp(visitor);
        if(SecondOptDesignator!=null) SecondOptDesignator.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ForStart(\n");

        if(FirstOptDesignator!=null)
            buffer.append(FirstOptDesignator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForOptCondition!=null)
            buffer.append(ForOptCondition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SecondOptDesignator!=null)
            buffer.append(SecondOptDesignator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ForStart]");
        return buffer.toString();
    }
}
