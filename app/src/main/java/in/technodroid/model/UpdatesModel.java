package in.technodroid.model;

/**
 * Created by IBM_ADMIN on 8/17/2015.
 */
public class UpdatesModel {

    private String category;

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setIsFav(boolean isFav) {
        this.isFav = isFav;
    }

    private int id;
    private boolean isFav;

    public UpdatesModel(String category, String title, String desc, String imgPath, String tag) {
        this.category = category;
        this.title = title;
        this.desc = desc;
        this.imgPath = imgPath;
        this.tag = tag;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private String desc;

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    private String imgPath;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private String tag;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }




}
