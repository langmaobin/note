package com.ywvision.wyvisionhelper.generator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Schema;

public class GreendaoGenerator {

    private static void addSchema(Schema schema) {
        // TODO: setup table entities here
//        Entity carSuggestion = schema.addEntity("SrcCarSuggestion");
//        carSuggestion.addIdProperty();
//        carSuggestion.addStringProperty("name");
//        carSuggestion.addStringProperty("brand");
//        carSuggestion.addStringProperty("model");
    }

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.ywvision.wyvisionhelper.greendao");
        addSchema(schema);
        new DaoGenerator().generateAll(schema, "./wyvisionhelper/src/main/java");
    }

}
