// generated with ast extension for cup
// version 0.8
// 6/1/2019 17:53:30


package rs.ac.bg.etf.pp1.ast;

public class SingleFormParametarDeclaration extends FormParams {

    private FormPara FormPara;

    public SingleFormParametarDeclaration (FormPara FormPara) {
        this.FormPara=FormPara;
        if(FormPara!=null) FormPara.setParent(this);
    }

    public FormPara getFormPara() {
        return FormPara;
    }

    public void setFormPara(FormPara FormPara) {
        this.FormPara=FormPara;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormPara!=null) FormPara.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormPara!=null) FormPara.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormPara!=null) FormPara.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleFormParametarDeclaration(\n");

        if(FormPara!=null)
            buffer.append(FormPara.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleFormParametarDeclaration]");
        return buffer.toString();
    }
}
