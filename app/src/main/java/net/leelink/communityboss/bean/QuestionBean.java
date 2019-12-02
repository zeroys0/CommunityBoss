package net.leelink.communityboss.bean;

public class QuestionBean {

    /**
     * Id : 1
     * ProblemName : 找回密码
     * Answer : 可以通过短信验证码重置密码
     */

    private int Id;
    private String ProblemName;
    private String Answer;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getProblemName() {
        return ProblemName;
    }

    public void setProblemName(String ProblemName) {
        this.ProblemName = ProblemName;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String Answer) {
        this.Answer = Answer;
    }
}
