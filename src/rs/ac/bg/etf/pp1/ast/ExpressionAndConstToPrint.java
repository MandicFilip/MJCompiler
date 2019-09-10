// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:31


package rs.ac.bg.etf.pp1.ast;

public class ExpressionAndConstToPrint extends ExpressionToPrint {

    private Expr Expr;
    private Integer value;

    public ExpressionAndConstToPrint (Expr Expr, Integer value) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.value=value;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value=value;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExpressionAndConstToPrint(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+value);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExpressionAndConstToPrint]");
        return buffer.toString();
    }
}