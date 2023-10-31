package lkw.com.obp.board.dto;

public class BoardDTO {
    private int num;
    private String name;
    private String pwd;
    private String email;
    private String subject;
    private String content;
    private String ipAddr;
    private int hitCount;
    private String created;
    private int virtualNum;

    public int getNum() {
        return num;
    }

    public BoardDTO setNum(int num) {
        this.num = num;
        return this;
    }

    public String getName() {
        return name;
    }

    public BoardDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getPwd() {
        return pwd;
    }

    public BoardDTO setPwd(String pwd) {
        this.pwd = pwd;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public BoardDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public BoardDTO setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getContent() {
        return content;
    }

    public BoardDTO setContent(String content) {
        this.content = content;
        return this;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public BoardDTO setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
        return this;
    }

    public int getHitCount() {
        return hitCount;
    }

    public BoardDTO setHitCount(int hitCount) {
        this.hitCount = hitCount;
        return this;
    }

    public String getCreated() {
        return created;
    }

    public BoardDTO setCreated(String created) {
        this.created = created;
        return this;
    }

    public int getVirtualNum() {
        return virtualNum;
    }

    public BoardDTO setVirtualNum(int virtualNum) {
        this.virtualNum = virtualNum;
        return this;
    }
}
