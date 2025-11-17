package nnpda.nnpda.model.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nnpda.nnpda.model.enums.TicketPriority;
import nnpda.nnpda.model.enums.TicketState;
import nnpda.nnpda.model.enums.TicketType;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "tickets")
public class TicketDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Keyword)
    private TicketType type;

    @Field(type = FieldType.Keyword)
    private TicketPriority priority;

    @Field(type = FieldType.Keyword)
    private TicketState state;

    @Field(type = FieldType.Long)
    private Long projectId;

    @Field(type = FieldType.Long)
    private Long ownerId;

    @Field(type = FieldType.Keyword)
    private String resolver;
}