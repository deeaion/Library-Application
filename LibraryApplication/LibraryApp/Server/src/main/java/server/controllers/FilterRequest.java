package server.controllers;

import java.util.List;

public class FilterRequest {
    private List<String> criterias;
    private List<String> values;

    public List<String> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<String> criterias) {
        this.criterias = criterias;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
