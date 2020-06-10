/**
 *
 */
package com.coolapps.logomaker.utilities;

/**
 * @author Waqar MK
 */
public class Item {

    private int image;
    private String engurduText;

    public Item(String _text){
        this.engurduText = _text;
    }

    public Item(int _image) {
        this.image = _image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getEngUrduText() {
        return engurduText;
    }

    public void setEngUrduText(String text) {
        this.engurduText = text;
    }

}
