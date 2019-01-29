package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.ArrayList;
import java.util.List;

public class ActualParametersBuffer {

    private class MethodParameters {
        ArrayList<Struct> parameters;

        public MethodParameters() {
            parameters = new ArrayList<>();
        }
    }

    public ActualParametersBuffer() {
        this.parametersStack = new ArrayList<>();
        this.currentMethodParameters = null;
    }

    private ArrayList<MethodParameters> parametersStack;
    private MethodParameters currentMethodParameters;

    public boolean insertActualParameter(Struct parameter) {
        if (currentMethodParameters == null) {
            return false;
        }
        currentMethodParameters.parameters.add(parameter);
        return true;
    }

    public void createParametersList() {
        if (currentMethodParameters != null) {
            parametersStack.add(currentMethodParameters);
        }
        currentMethodParameters = new MethodParameters();
    }

    public List<Struct> getParameters() {
        if (currentMethodParameters == null) {
            return null;
        }

        List<Struct> tmp = currentMethodParameters.parameters;

        int stackSize = parametersStack.size();
        if (stackSize > 0) {
            currentMethodParameters = parametersStack.remove(stackSize - 1);
        }
        else {
            currentMethodParameters = null;
        }

        return tmp;
    }

}
