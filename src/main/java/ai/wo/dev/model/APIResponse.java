package ai.wo.dev.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(Include.NON_NULL)
public class APIResponse {


    private Object data;

    private List<APIError> errors;
    private Map<String, String> meta;

    /**
     * Creates a new response wrapping a single result attribute (e.g., return
     * true from an [non-crud] RPC call)
     *
     * @param result
     * @return
     */
    public static APIResponse newSimpleResponse(Object result) {
        APIResponse res = new APIResponse();
        APIData data = new APIData();
        Map<String, Object> attr = new HashMap<>();
        attr.put("result", result);
        data.setAttributes(attr);
        res.setData(data);

        return res;
    }

    public APIResponse() {
        this.meta = new HashMap<>();
    }

    public APIResponse error(APIError error) {
        addError(error);
        return this;
    }
    public void addError(APIError error) {
        if (getErrors() == null) {
            this.errors = new ArrayList<APIError>();
        }
        getErrors().add(error);
    }

    public APIResponse data(Object data) {
        this.data = data;
        return this;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }

    public List<APIError> getErrors() {
        return errors;
    }
    public void setErrors(List<APIError> errors) {
        this.errors = errors;
    }

    public Map<String, String> getMeta() {
        return meta;
    }
    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }
}
