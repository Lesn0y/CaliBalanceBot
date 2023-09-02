package org.lesnoy.dto;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class UserDTODeserializer extends StdDeserializer<UserDTO> {

    public UserDTODeserializer() {
        this(null);
    }
    public UserDTODeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public UserDTO deserialize(JsonParser parser, DeserializationContext context) throws IOException, JacksonException {
        ObjectCodec codec = parser.getCodec();
        JsonNode node = codec.readTree(parser);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(node.get("id").asInt());
        userDTO.setLogin(node.get("login").asText());
        userDTO.setAge(node.get("age").asInt());
        userDTO.setHeight(node.get("height").asLong());
        userDTO.setWeight(node.get("weight").asLong());
        userDTO.setSex(node.get("sex").asText());
        userDTO.setGoal(node.get("goal").asText());
        userDTO.setActivity(node.get("activity").asText());
        userDTO.setCal(node.get("cal").asLong());
        userDTO.setProt(node.get("prot").asLong());
        userDTO.setFats(node.get("fats").asLong());
        userDTO.setCarbc(node.get("carbs").asLong());

        return userDTO;
    }
}
