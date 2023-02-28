package demo2;

import com.apple.laf.ScreenMenuBar;
import javafx.scene.control.Slider;
import javafx.stage.Screen;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
import java.util.Vector;

public class screen extends JFrame implements ActionListener, MouseListener, MouseMotionListener, ChangeListener {

    //JToolBar toolBar = new JToolBar();
    private Vector<Point> vStart = new Vector<Point>();
    private Vector<Point> vEnd = new Vector<Point>();
    private Vector<String> mode = new Vector<String>();
    private Vector<Color> colors = new Vector<Color>();
    private Vector<Float> strokes = new Vector<Float>();


    //band
    Point BandStart = new Point();
    Point BandEnd = new Point();


    //그림을 그리는 panel과 버튼을 누르는 panel
    JPanel jpanel = new JPanel();
    JPanel jpanel2 = new JPanel();


    //color
    Color color = Color.BLACK;
    JButton colorButton;
    JColorChooser colorChooser = new JColorChooser(); //컬러 다이얼로그 생성

    //stroke
    Float stroke = (float) 5; //float로 설정하는 이유는 setStroke에서 받는 인자 자료형이 float이기 떄문
    JSlider slider = new JSlider(JSlider.HORIZONTAL,0,30,5);
    float dash1[] = {1,1f};
    float dash2[] = {2,2f};
    float dash3[] = {3,3f};
    float dash4[] = {4,4f};
    float dash5[] = {5,5f};
    float dash10[] = {10,10f};
    float dash20[] = {20,20f};



    //shape
    String shape = "선";
    ImageIcon shape1_Image = new ImageIcon("Button_Image/선.png");
    ImageIcon shape2_Image = new ImageIcon("Button_Image/사각형.png");
    ImageIcon shape3_Image = new ImageIcon("Button_Image/원.png");
    ImageIcon shape4_Image = new ImageIcon("Button_Image/펜.png");
    JButton shape1;
    JButton shape2;
    JButton shape3;
    JButton shape4;


    //지우개
    ImageIcon erase1_Image = new ImageIcon("Button_Image/지우개.png");
    JButton erase1;
    ImageIcon erase2_Image = new ImageIcon("Button_Image/삭제.png");
    JButton erase2;


    //Do
    ImageIcon Do1_Image = new ImageIcon("Button_Image/왼쪽_화살표.png");
    ImageIcon Do2_Image = new ImageIcon("Button_Image/오른쪽_화살표.png");

    JButton Do1;
    JButton Do2;


    int width;
    int height;
    int minPointx;
    int minPointy;
    int bwidth;
    int bheight;
    int bminPointx;
    int bminPointy;


    //undo, redo하기 위한 stack을 쌓기
    private final Stack<Undo_Redo> UDRD = new Stack<Undo_Redo>();


    //파일 저장, 불러오기
    BufferedImage bufferedImage = new BufferedImage(1000,500, BufferedImage.TYPE_INT_RGB);
    Graphics2D sav = bufferedImage.createGraphics();
    JFileChooser jFileChooser = new JFileChooser();

    ImageIcon save_Image = new ImageIcon("Button_Image/저장하기.png");
    ImageIcon load_Image = new ImageIcon("Button_Image/파일 불러오기.png");


    JButton save;
    JButton load;

    JLabel label = new JLabel();
    JLabel show_font = new JLabel();


    screen(){
        setTitle("Adoba");

        setLayout(null); //내 맘대로 화면 지정을 할 수 있는
        setSize(1000,600);
        //setBackground(Color.white);
        setLocationRelativeTo(null); //화면의 가운데 띄움
        setResizable(false); //사이즈 조절 불가능
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //창을 닫을 때 실행 중인 프로그램도 같이 종료되도록 함
        jpanel.setBounds(0, -100,1000,600);
        jpanel.setBackground(Color.white);

        jpanel2.setBounds(0,500,1000, 100);
        jpanel2.setBackground(Color.white);


        //모든 버튼을 설정해줌 -> 추후에 사진으로 바꿀 예정
        //colorComboBox = new JComboBox<Color>();
        colorButton = new JButton();
        colorButton.setPreferredSize(new Dimension(40, 40));
        colorButton.setBackground(color);
        colorButton.setOpaque(true);
        colorButton.setBorderPainted(false);

        //strokeComboBox = new JComboBox<Float>();
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);


        //shapeComboBox = new JComboBox<String>(shapes);
        shape1 = new JButton(shape1_Image);
        shape1.setPreferredSize(new Dimension(50, 50));
        shape2 = new JButton(shape2_Image);
        shape2.setPreferredSize(new Dimension(50, 50));
        shape3 = new JButton(shape3_Image);
        shape3.setPreferredSize(new Dimension(50, 50));
        shape4 = new JButton(shape4_Image);
        shape4.setPreferredSize(new Dimension(50, 50));

        //지우개
        erase1 = new JButton(erase1_Image);
        erase1.setPreferredSize(new Dimension(50,50));
        erase2 = new JButton(erase2_Image);
        erase2.setPreferredSize(new Dimension(50,50));



        //doingComboBox = new JComboBox<String>(doings);
        Do1 = new JButton(Do1_Image);
        Do1.setPreferredSize(new Dimension(50, 50));
        Do2 = new JButton(Do2_Image);
        Do2.setPreferredSize(new Dimension(50,50));
        save = new JButton(save_Image);
        save.setPreferredSize(new Dimension(50,50));
        load = new JButton(load_Image);
        load.setPreferredSize(new Dimension(50,50));


        //Jpanel에 그 값들을 넣어줌
        //jpanel.add(colorComboBox);
        jpanel2.add(colorButton,BorderLayout.SOUTH);
        //jpanel.add(strokeComboBox);
        jpanel2.add(slider,BorderLayout.SOUTH);
        //jpanel.add(shapeComboBox);
        jpanel2.add(shape1,BorderLayout.SOUTH);
        jpanel2.add(shape2,BorderLayout.SOUTH);
        jpanel2.add(shape3,BorderLayout.SOUTH);
        jpanel2.add(shape4,BorderLayout.SOUTH);

        jpanel2.add(erase1,BorderLayout.SOUTH);
        jpanel2.add(erase2,BorderLayout.SOUTH);

        jpanel2.add(Do1,BorderLayout.SOUTH);
        jpanel2.add(Do2,BorderLayout.SOUTH);
        jpanel2.add(save,BorderLayout.SOUTH);
        jpanel2.add(load,BorderLayout.SOUTH);

//        toolBar.add(colorButton);
//        toolBar.add(slider);
//        toolBar.add(shape1);
//        toolBar.add(shape2);
//        toolBar.add(shape3);
//        toolBar.add(shape4);
//        toolBar.add(Do1);
//        toolBar.add(Do2);
//        toolBar.add(save);
//        toolBar.add(load);

        add(jpanel);
        add(jpanel2);




        //모든 것에 addActionListener을 넣어줌
        //colorComboBox.addActionListener(this);
        colorButton.addActionListener(this);
        //strokeComboBox.addActionListener(this);
        slider.addChangeListener(this);
        //shapeComboBox.addActionListener(this);
        //doingComboBox.addActionListener(this);
        shape1.addActionListener(this);
        shape2.addActionListener(this);
        shape3.addActionListener(this);
        shape4.addActionListener(this);
        erase1.addActionListener(this);
        erase2.addActionListener(this);

        Do1.addActionListener(this);
        Do2.addActionListener(this);
        save.addActionListener(this);
        load.addActionListener(this);


        addMouseListener(this);
        addMouseMotionListener(this);


    }



    public static void main(String[] args){
        new screen().setVisible(true);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

        if (e.getSource() == colorButton) {
            //JColorChooser colorChooser = new JColorChooser(); //컬러 다이얼로그 생성
            color = colorChooser.showDialog(null, "Color", Color.BLACK); // 컬러 다이얼로그를 출력하고 사용자가 선택한 색을 알아옴.
            colorButton.setBackground(color);
        }

         else if (e.getSource() == shape1) {
            shape = "선";
        } else if (e.getSource() == shape2) {
            shape = "사각형";
        } else if (e.getSource() == shape3) {
            shape = "원";
        } else if (e.getSource() == shape4) {
            shape = "펜";
        } else if (e.getSource() == erase1){
            shape = "지우개";
        } else if (e.getSource() == erase2){
            System.out.println("하하하핳");
            vStart.removeAllElements();
            vEnd.removeAllElements();
            mode.removeAllElements();
            colors.removeAllElements();
            strokes.removeAllElements();
            repaint();
        } else if (e.getSource() == Do1) {

            for (int i = vStart.size() - 1; vStart.get(i) != null; i--) {
                UDRD.push(new Undo_Redo(vStart.get(i), vEnd.get(i), mode.get(i), colors.get(i), strokes.get(i)));
                vStart.remove(i);
                vEnd.remove(i);
                mode.remove(i);
                colors.remove(i);
                strokes.remove(i);
            }
            UDRD.push(new Undo_Redo(null, null, null, null, null));
            vStart.remove(vStart.size() - 1);
            vEnd.remove(vEnd.size() - 1);
            mode.remove(mode.size() - 1);
            colors.remove(colors.size() - 1);
            strokes.remove(strokes.size() - 1);
            repaint();
        }
        else if(e.getSource() == Do2){

                UDRD.pop();
                vStart.add(null);
                vEnd.add(null);
                mode.add(null);
                colors.add(null);
                strokes.add(null);
                for(int i = UDRD.size()-1; UDRD.peek().sp!= null ; i--){
                    Undo_Redo redo = UDRD.peek();
                    vStart.add(redo.sp);
                    vEnd.add(redo.ep);
                    mode.add(redo.md);
                    colors.add(redo.mc);
                    strokes.add(redo.mw);
                    UDRD.pop();
                    if(UDRD.empty())
                        break;
                }
                repaint();


        }
        else if (e.getSource() == save) {
            jFileChooser.setFileFilter(new FileNameExtensionFilter("*.png", "png"));
            int rVal = jFileChooser.showSaveDialog(null);
            //bufferedImage = new BufferedImage(jpanel.getWidth(),jpanel.getHeight(),BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = bufferedImage.createGraphics();
            paint(g2);

            if(rVal == JFileChooser.APPROVE_OPTION){
                File file = jFileChooser.getSelectedFile();
                try {
                    ImageIO.write(bufferedImage, "png", new File(file.getAbsolutePath()));
                    System.out.println("saved Correctly " + file.getAbsolutePath());
                } catch (IOException ex) {
                    System.out.println("Failed to save image");
                }
            }
                if (rVal == JFileChooser.CANCEL_OPTION) {
                    System.out.println("No file choosen");
                }
            }


        else if (e.getSource() == load) {
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JPEG", "jpeg", "jpg", "png", "bmp", "gif");
            jFileChooser.addChoosableFileFilter(filter);
            System.out.println("야야야");
            Graphics g = null;

            //Image background=new ImageIcon(Main.class.getResource("../image/background1.png")).getImage();
            //public void paint(Graphics g) {//그리는 함수
                //g.drawImage(background, 0, 0, null);//background를 그려줌

            int rVal = jFileChooser.showOpenDialog(null);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jFileChooser.getSelectedFile();

                Image background = new ImageIcon(selectedFile.getAbsolutePath()).getImage();
                
                g.drawImage(background, 0, 0, 1000, 500, this);
                System.out.println("야야야 보고싶었웅");

            }
            if (rVal == JFileChooser.CANCEL_OPTION) {

            }

        }
    }



    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        //graphic이 그려질 순서
        //drawBox를 먼저 실행해야지 checkbox가
        //도형들과 동시에 그려지는 것을 방지 가능
        System.out.println(e.getX() + " " + e.getY());

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        //다시 클릭됐을 경우 좌표 초기화
        //firstPointer.setLocation(0,0);
        //secondPointer.setLocation(0,0);
        //firstPointer.setLocation(e.getX(), e.getY());

        //쓰레기 값 'null' 넣어주기
        vStart.add(null);
        vEnd.add(null);
        mode.add(null);
        colors.add(null);
        strokes.add(null);

        Point startP = e.getPoint();
        vStart.add(startP);
        mode.add(shape); //버튼을 누를 때도 계속해서 mode에 넣어줌
        colors.add(color);
        strokes.add(stroke);

        BandStart = e.getPoint();




    }

    @Override
    public void mouseReleased(MouseEvent e) {

            Point endP = e.getPoint();
            vEnd.add((endP));
            //mode.add(shape);
            repaint(); //패널의 다시 그리기를 요청한다.


            //쓰레기 값을 넣어줌
            BandStart = new Point();
            BandEnd = new Point();

    }

    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g; //이게 왜 일까 ????
        bwidth = Math.abs((int) BandEnd.getX() - (int) BandStart.getX());
        bheight = Math.abs((int)BandEnd.getY() - (int) BandStart.getY());

        bminPointx = Math.min((int)BandStart.getX(), (int)BandEnd.getX());
        bminPointy = Math.min((int)BandStart.getY(), (int)BandEnd.getY());


        for(int i=0; i<vEnd.size(); i++){
            Point s = vStart.get(i);
            Point e = vEnd.get(i);

            if(s != null){
                width = Math.abs((int)e.getX() - (int) s.getX());
                height = Math.abs((int)e.getY() - (int) s.getY());

                minPointx = Math.min((int)s.getX(), (int)e.getX());
                minPointy = Math.min((int)s.getY(), (int)e.getY());


                if(mode.get(i) == "선"){
                    g2.setColor(colors.get(i));
                    g2.setStroke(new BasicStroke(strokes.get(i)));
                    g2.drawLine((int)s.getX(), (int)s.getY(), (int)e.getX(), (int)e.getY());
                    sav.setColor(colors.get(i));
                    sav.setStroke(new BasicStroke(strokes.get(i)));
                    sav.drawLine((int)s.getX(), (int)s.getY(), (int)e.getX(), (int)e.getY());

                }
                else if(mode.get(i) == "사각형"){
                    g2.setColor(colors.get(i));
                    g2.setStroke(new BasicStroke(strokes.get(i)));
                    g2.drawRect(minPointx, minPointy, width, height);
                    sav.setColor(colors.get(i));
                    sav.setStroke(new BasicStroke(strokes.get(i)));
                    sav.drawRect(minPointx, minPointy, width, height);
                }
                else if (mode.get(i)== "원") {
                    g2.setColor(colors.get(i));
                    g2.setStroke(new BasicStroke(strokes.get(i)));
                    g2.drawOval(minPointx, minPointy, width, height);
                    sav.setColor(colors.get(i));
                    sav.setStroke(new BasicStroke(strokes.get(i)));
                    sav.drawOval(minPointx, minPointy, width, height);
                }
                else if (mode.get(i)== "펜"){
                    g2.setColor(colors.get(i));
                    g2.setStroke(new BasicStroke(strokes.get(i)));
                    g2.drawLine((int)s.getX(), (int)s.getY(), (int)e.getX(), (int)e.getY());
                    sav.setColor(colors.get(i));
                    sav.setStroke(new BasicStroke(strokes.get(i)));
                    sav.drawLine((int)s.getX(), (int)s.getY(), (int)e.getX(), (int)e.getY());
                }else if(mode.get(i) == "지우개"){
                    g2.setColor(Color.white);
                    g2.setStroke(new BasicStroke(strokes.get(i)));
                    g2.drawLine((int)s.getX(), (int)s.getY(), (int)e.getX(), (int)e.getY());

                }
            }
        }

        if(shape.equals("선")){
            System.out.println("hhhhhhhh");
            g2.setColor(new Color(0xC0C0C0));
            g2.setStroke(new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1,dash3,0));
            g2.drawLine(BandStart.x, BandStart.y, BandEnd.x, BandEnd.y);
        }
        else if(shape.equals("사각형")){
            g2.setColor(new Color(0xC0C0C0));
            g2.setStroke(new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1,dash3,0));
            g2.drawRect(bminPointx, bminPointy, bwidth, bheight);
        }
        else if (shape.equals("원")) {
            g2.setColor(new Color(0xC0C0C0));

            g2.setStroke(new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1,dash3,0));
            g2.drawOval(bminPointx, bminPointy, bwidth, bheight);
        }
        }



    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(shape == "펜"){
            vEnd.add(e.getPoint());
            repaint();

            vStart.add(e.getPoint());
            mode.add(shape);
            colors.add(color);
            strokes.add(stroke);
        }else{
            BandEnd = e.getPoint();
            repaint();
        }


    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider value = (JSlider)e.getSource();
        show_font.setText(String.valueOf(value.getValue()));
        stroke=Float.valueOf(show_font.getText());
    }
}

class OpenFile implements ActionListener{


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

