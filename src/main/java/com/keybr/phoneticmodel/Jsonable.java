package com.keybr.phoneticmodel;

import javax.json.Json;
import javax.json.JsonValue;
import java.io.Writer;

interface Jsonable {

    JsonValue toJson();

    static void write(Writer writer, JsonValue json) {
        var generator = Json.createGenerator(writer);
        generator.write(json);
        generator.flush();
    }
}
