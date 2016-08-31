package example.naimish.com.alarm.Pojo;

/**
 * Created by naimish on 6/8/16.
 */
public class FeedsObject {
    String Sender,Label;


    public FeedsObject(String Sender,String Label) {
        this.Sender=Sender;
        this.Label=Label;

    }

    public String getSender() {
        return Sender;
    }


    public String getLabel() {
        return Label;
    }

}
