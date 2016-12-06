package cn.xiaocool.wxtteacher.bean;

/**
 * Created by xiaocool on 16/12/5.
 */

public class ChatInfoBean {

    /**
     * id : 3
     * chatid : 2
     * userid : 605
     * user_type : 1
     * inviter_uid : 605
     * create_time : 1480399789
     * my_face : newsgroup6171473498324434.jpg
     * my_nickname : 啾啾啾
     */

    private String id;
    private String chatid;
    private String userid;
    private String user_type;
    private String inviter_uid;
    private String create_time;
    private String my_face;
    private String my_nickname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getInviter_uid() {
        return inviter_uid;
    }

    public void setInviter_uid(String inviter_uid) {
        this.inviter_uid = inviter_uid;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getMy_face() {
        return my_face;
    }

    public void setMy_face(String my_face) {
        this.my_face = my_face;
    }

    public String getMy_nickname() {
        return my_nickname;
    }

    public void setMy_nickname(String my_nickname) {
        this.my_nickname = my_nickname;
    }
}
