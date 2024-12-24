package spartaspringnewspeed.spartafacespeed.post.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class PostPageDto {
    private List<PostDto> content;
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;

    public PostPageDto(List<PostDto> content, int page, int size, int totalPages, long totalElements) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public static PostPageDto fromPage(org.springframework.data.domain.Page<PostDto> postPage) {
        return new PostPageDto(
                postPage.getContent(),
                postPage.getNumber(),
                postPage.getSize(),
                postPage.getTotalPages(),
                postPage.getTotalElements()
        );
    }
}