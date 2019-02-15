// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:31


package rs.ac.bg.etf.pp1.ast;

public class ParametarsList extends ActualParamList {

    private ActualParamList ActualParamList;
    private ActualParametar ActualParametar;

    public ParametarsList (ActualParamList ActualParamList, ActualParametar ActualParametar) {
        this.ActualParamList=ActualParamList;
        if(ActualParamList!=null) ActualParamList.setParent(this);
        this.ActualParametar=ActualParametar;
        if(ActualParametar!=null) ActualParametar.setParent(this);
    }

    public ActualParamList getActualParamList() {
        return ActualParamList;
    }

    public void setActualParamList(ActualParamList ActualParamList) {
        this.ActualParamList=ActualParamList;
    }

    public ActualParametar getActualParametar() {
        return ActualParametar;
    }

    public void setActualParametar(ActualParametar ActualParametar) {
        this.ActualParametar=ActualParametar;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ActualParamList!=null) ActualParamList.accept(visitor);
        if(ActualParametar!=null) ActualParametar.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ActualParamList!=null) ActualParamList.traverseTopDown(visitor);
        if(ActualParametar!=null) ActualParametar.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ActualParamList!=null) ActualParamList.traverseBottomUp(visitor);
        if(ActualParametar!=null) ActualParametar.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ParametarsList(\n");

        if(ActualParamList!=null)
            buffer.append(ActualParamList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActualParametar!=null)
            buffer.append(ActualParametar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ParametarsList]");
        return buffer.toString();
    }
}
