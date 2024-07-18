package com.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.sql.Date;
import java.util.GregorianCalendar;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    private int id;
    private String contents;
    private String publicationDate;
    private String name;
    private String author;
    private String recordingDate;

    @JsonIgnore
    public Date getPublicationDateSQL() {
        String[] s = publicationDate.split("-");
        try {
            GregorianCalendar c = new GregorianCalendar();
            c.set(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]));
            return new Date(c.getTimeInMillis());
        } catch (NumberFormatException e) {
        }
        return null;
    }

    @JsonIgnore
    public boolean isEmpty(){
        return contents == null && publicationDate == null && name == null && author == null;
    }

    @JsonIgnore
    public boolean isNotFull(){
        return contents == null || publicationDate == null || name == null || author == null;
    }
}
