package spartaspringnewspeed.spartafacespeed.common.entity;

import jakarta.persistence.*;

@Entity
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;
}
