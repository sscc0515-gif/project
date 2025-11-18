package com.springboot.domain;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardFormDto {

    private Long id;
    private String writerid;
    private String writer;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public Board toEntity() {
        return Board.builder()
                .id(id)
                .writerid(writerid)
                .writer(writer)
                .title(title)
                .content(content)
                .build();
    }

    @Builder
    public BoardFormDto(Long id, String writerid, String writer, String title,
                        String content, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.writerid = writerid;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
