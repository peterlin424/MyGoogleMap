package com.example.linweijie.mygooglemap;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by linweijie on 3/7/16.
 */
public class BasicMapMark {

    public LatLng getLatLng() {
        return latLng;
    }
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
    public BitmapDescriptor getIcon() {
        return icon;
    }
    public MarkerOptions getMarkerOpt() {
        return markerOpt;
    }

    private LatLng latLng;
    private String title;
    private String content;
    private BitmapDescriptor icon;
    private MarkerOptions markerOpt;

    public BasicMapMark(LatLng latLng, String title, String content, BitmapDescriptor icon) {
        this.latLng = latLng;
        this.title = title;
        this.content = content;
        this.icon = icon;

        this.markerOpt = new MarkerOptions();
        this.markerOpt.position(latLng);
        this.markerOpt.title(title);
        this.markerOpt.snippet(content);
        this.markerOpt.draggable(false);
        this.markerOpt.anchor(0.5f, 0.5f);//設為圖片中心
        if (icon != null) {
            this.markerOpt.icon(icon);
        }
    }


}
