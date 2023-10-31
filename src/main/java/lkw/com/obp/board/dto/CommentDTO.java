package lkw.com.obp.board.dto;

import java.util.Date;

public class CommentDTO {
    private int commentNum;         // 댓글 글번호
    private int commentBoard;       // 게시글 번호
    private String commentId;       // 댓글 작성자
    private Date commentDate;       // 댓글 작성일
    private int commentParent;      // 부모글
    private String commentContent;  // 댓글 내용
    private int commentLevel;       // 댓글- 답변글 깊이



    public int getCommentLevel() {
        return commentLevel;
    }

    public CommentDTO setCommentLevel(int commentLevel) {
        this.commentLevel = commentLevel;
        return this;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public CommentDTO setCommentNum(int commentNum) {
        this.commentNum = commentNum;
        return this;
    }

    public int getCommentBoard() {
        return commentBoard;
    }

    public CommentDTO setCommentBoard(int commentBoard) {
        this.commentBoard = commentBoard;
        return this;
    }

    public String getCommentId() {
        return commentId;
    }

    public CommentDTO setCommentId(String commentId) {
        this.commentId = commentId;
        return this;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public CommentDTO setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
        return this;
    }

    public int getCommentParent() {
        return commentParent;
    }

    public CommentDTO setCommentParent(int commentParent) {
        this.commentParent = commentParent;
        return this;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public CommentDTO setCommentContent(String commentContent) {
        this.commentContent = commentContent;
        return this;
    }
}
