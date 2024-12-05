package rw.wetech.HospitalManagementSystem.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import rw.wetech.HospitalManagementSystem.model.User;

import java.io.IOException;

public class UserSerializer extends JsonSerializer<User> {

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", user.getId());
        jsonGenerator.writeStringField("username", user.getUsername());
        jsonGenerator.writeStringField("email", user.getEmail());
        jsonGenerator.writeStringField("role", user.getRole().name());
        jsonGenerator.writeStringField("firstName", user.getFirstName());
        jsonGenerator.writeStringField("lastName",user.getLastName());
        jsonGenerator.writeStringField("phoneNumber", user.getPhoneNumber());
        jsonGenerator.writeStringField("profilePicture", user.getProfilePicture());

        // Add other fields you want to serialize
        jsonGenerator.writeEndObject();
    }
}
