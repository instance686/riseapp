package example.naimish.com.alarm.Pojo;

import android.graphics.drawable.Drawable;
import android.opengl.EGLDisplay;


/**
 * Created by naimish on 2/8/16.
 */
public class ListItem {
    String Heading;
    String subHeading;
    String label;

    public ListItem() {

    }
    public ListItem(String Heading,String subHeading) {
        super();
        this.Heading=Heading;
        this.subHeading=subHeading;
    }


    public ListItem(String label) {
        super();
        this.label=label;
    }

    public String getHeading() {
        return Heading;
    }

    public void setHeading(String Heading) {
        this.Heading=Heading;
    }

    public String getsubHeading() {
        return subHeading;
    }

    public void setsubHeading(String subHeading) {
        this.subHeading=subHeading;
    }


}
