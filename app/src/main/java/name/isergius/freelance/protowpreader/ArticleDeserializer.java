package name.isergius.freelance.protowpreader;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * @author Sergey Kondratyev
 */

public class ArticleDeserializer extends StdDeserializer<Article> {

    public ArticleDeserializer() {
        super(Article.class);
    }

    @Override
    public Article deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);
        long id = node.get("id").asLong();
        String title = node.get("title").get("rendered").asText();
        String content = node.get("content").get("rendered").asText();
        return new Article(id, title, content);
    }
}
