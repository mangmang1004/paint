package demo2;

import java.awt.*;

public class Undo_Redo {
    //내가 이걸 5개 다 가져와야 하니까 !!
    Point sp;
    Point ep;
    String md;
    Color mc;
    Float mw;

    public Undo_Redo(Point start_point, Point end_point, String draw_mode, Color color_mode, Float whight_mode){
        sp = start_point;
        ep = end_point;
        md = draw_mode;
        mc = color_mode;
        mw = whight_mode;
    }
}
