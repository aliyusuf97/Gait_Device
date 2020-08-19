package list;

public class RecyclerViewItem {

    private String text1;
    private String text2;
    private int image1;

    private String footStatus = "NONE";

    public RecyclerViewItem(String text1, String text2, int image1){
        this.text1 = text1;
        this.text2 = text2;
        this.image1 = image1;
    }

    public void setFootStatus(String footStatus) {
        this.footStatus = footStatus;
    }

    public String getFootStatus() {
        return footStatus;
    }

    public void setImage1(int image1) {
        this.image1 = image1;
    }

    public int getImage1() {
        return image1;
    }

    public String getText1() {
        return text1;
    }

    public String getText2() {
        return text2;
    }

}
