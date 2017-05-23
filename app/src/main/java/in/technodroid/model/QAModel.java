package in.technodroid.model;

import in.technodroid.util.AppConstants;

/**
 * Created by IBM_ADMIN on 9/10/2015.
 */
public class QAModel {



    /* Mandatory Details for QA*/
    private String _Question;
    private String _Answer;
    private String _imgURL;
    private String _By;
    private float _rating;
    /* Additional Details for QA*/
    private String _Topic; //Android ,JAVA ,Puzzle
    private String _SubTopic; //Thread ,Activity
    private AppConstants.LEVEL _Level;//Easy Medium Tough
    private String _AskedIn;//IBM

    public boolean isFav() {
        return isFav;
    }

    public void setIsFav(boolean isFav) {
        this.isFav = isFav;
    }

    private boolean isFav=false;


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    private int _id;


    public QAModel(String _Question, String _Answer, String _imgURL, String _By) {
        this._Question = _Question;
        this._Answer = _Answer;
        this._imgURL = _imgURL;
        this._By = _By;
    }



    public String get_Question() {
        return _Question;
    }

    public void set_Question(String _Question) {
        this._Question = _Question;
    }

    public String get_Answer() {
        return _Answer;
    }

    public void set_Answer(String _Answer) {
        this._Answer = _Answer;
    }

    public String get_imgURL() {
        return _imgURL;
    }

    public void set_imgURL(String _imgURL) {
        this._imgURL = _imgURL;
    }

    public String get_By() {
        return _By;
    }

    public void set_By(String _By) {
        this._By = _By;
    }

    public float get_rating() {
        return _rating;
    }

    public void set_rating(float _rating) {
        this._rating = _rating;
    }

    public String get_Topic() {
        return _Topic;
    }

    public void set_Topic(String _Topic) {
        this._Topic = _Topic;
    }

    public String get_SubTopic() {
        return _SubTopic;
    }

    public void set_SubTopic(String _SubTopic) {
        this._SubTopic = _SubTopic;
    }

    public AppConstants.LEVEL get_Level() {
        return _Level;
    }

    public void set_Level(AppConstants.LEVEL _Level) {
        this._Level = _Level;
    }

    public String get_AskedIn() {
        return _AskedIn;
    }

    public void set_AskedIn(String _AskedIn) {
        this._AskedIn = _AskedIn;
    }




    QAModel(){

    }


}
