package ph.sinonet.vg.live.validator.deserializer;

import com.google.gson.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ph.sinonet.vg.live.validator.annotation.JsonExemption;
import ph.sinonet.vg.live.validator.annotation.JsonRequired;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kierpagdato on 8/3/16.
 */
public class AnnotatedDeserializer<RequestMessage> implements JsonDeserializer<RequestMessage> {

    private final static Logger LOGGER = LoggerFactory.getLogger(AnnotatedDeserializer.class);

    @Override
    public RequestMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        ph.sinonet.vg.live.model.bean.websocket.RequestMessage modelClass = convert(json);
        checkRequiredFields(modelClass);
        RequestMessage rm = (RequestMessage) modelClass;

        return rm;
    }

    private void checkRequiredFields(Object modelClass){

        Class modelC = modelClass.getClass();
        Field[] fields = new Field[]{};
        List<String> fieldExemptions = new ArrayList<String>();

        fields = getAllFields(fields, modelC);
//        LOGGER.info("modelclass: " + modelClass + " fieldsize: " + fields.length);
//        LOGGER.info("fields: " + ArrayUtils.toString(fields));

//        LOGGER.info("exemption: " + modelC.getAnnotation(JsonExemption.class));


        if (modelC.isAnnotationPresent(JsonExemption.class)) {

            JsonExemption jsonExemption = (JsonExemption) modelC.getAnnotation(JsonExemption.class);
            fieldExemptions = Arrays.asList(jsonExemption.value());
//            LOGGER.info("fieldExemptions: " + ArrayUtils.toString(fieldExemptions));
        }


        for (Field f : fields) {
            try {

                if (!fieldExemptions.contains(f.getName())) {
                    if (f.getAnnotation(JsonRequired.class) != null) {

                        f.setAccessible(true);

                        if (f.get(modelClass) == null) {
                            throw new JsonParseException("Missing field in JSON: " + f.getName());
                        } else {
                            if(!StringUtils.isNotBlank(f.get(modelClass).toString())){
                                throw new JsonParseException("Missing field in JSON: " + f.getName());
                            }
                        }

                        //validate inner class
                        switch (f.getName()) {
                            case "data":
                                checkRequiredFields(f.get(modelClass));
                                break;
                            default:

                                break;
                        }

                    }
                }


            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
    }


    private Field[] getAllFields(Field[] fields, Class type){
        fields = ArrayUtils.addAll(fields, type.getDeclaredFields());

        if(type.getSuperclass() != null){
            fields = getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }


    private ph.sinonet.vg.live.model.bean.websocket.RequestMessage convert(JsonElement json) {
        try{
            Gson gson = new Gson();
            ph.sinonet.vg.live.model.bean.websocket.RequestMessage req = gson.fromJson(json, ph.sinonet.vg.live.model.bean.websocket.RequestMessage.class);
            req.data = gson.fromJson(getData(json.toString()), req.getTypeFunction());
            return req;
        } catch (NullPointerException ex ){
            throw new JsonParseException("Missing field in JSON: service/functionName/data");
        }
    }

    private String getData(String json) {
        JSONObject jsonObj = JSONObject.fromObject(json);
        return jsonObj.getString("data");
    }
}