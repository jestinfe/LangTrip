package kr.co.sist.e_learning.community;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor

public class CommunityPostDTO {

    private Long postId;
    private String userId;
    private String title;
    private String content;
    private Timestamp createdAt;
    private int views;
    private char postState;
    private String nickname;

    
    
}
