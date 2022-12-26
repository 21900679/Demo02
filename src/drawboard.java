import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Vector;
import java.awt.image.*;
import java.io.File;
import java.io.*;

public class drawboard extends JFrame {

    JPanel bottonpanel = new JPanel();
    MyPanel panel = new MyPanel();
    public drawboard(){
        setSize(1300, 700);     //프레임 크기 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프레임 윈도우를 닫으면 프로그램 종료
        setTitle("지민이의 그림판");
        setLocationRelativeTo(null);        //프로그램 시작시 화면 중앙에 출력됨
        //getContentPane().setLayout(null); //레이아웃 설정

        add(panel);        //frame에 panel추가
        add(bottonpanel, BorderLayout.NORTH);
        //BufferedImage img = new BufferedImage(1300, 700, BufferedImage.TYPE_INT_RGB);

        setVisible(true);           //프레임이 보이도록 설정
    }

    class MyPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener, ChangeListener {
        JFileChooser chooser = new JFileChooser();

        JComboBox<Color> colorComboBox;
        JComboBox<Float> strokeComboBox;
        Vector<Point> vStart = new Vector<Point>();
        Vector<Point> vEnd = new Vector<Point>();
        Vector<Integer> mode = new Vector<Integer>();
        Vector<Color> color = new Vector<Color>();
        Vector<Float> stroke = new Vector<Float>();
        Vector<undo> vUndo = new Vector<undo>();
        Point rubStart = new Point();
        Point rubEnd = new Point();
        public static final int LINE = 1;
        public static final int RECT = 2;
        public static final int CIRCLE = 3;
        public static final int SKETCH = 4;
        public static final int UNDO = 5;
        public static final int REDO = 6;
        public static final int ERASER = 7;
        boolean read = false;
        int CurrentMode = 4;
        int BeforeMode = 4;
        Color CurrentColor = Color.black;
        Float CurrentStroke = (float)5;
        int width, height, Pointx, Pointy, rubwidth, rubheight, rubPointx, rubPointy;
        float dash3[] = {3,3f};
        BufferedImage img = new BufferedImage(1300, 700, BufferedImage.TYPE_INT_RGB);
        Graphics2D sav = img.createGraphics();
        Image readimage = null;
        JLabel label = new JLabel();
        JLabel fontsize = new JLabel();

        public MyPanel(){

            colorComboBox = new JComboBox<Color>();
            strokeComboBox = new JComboBox<Float>();
            JButton[] btn = new JButton[13];
            btn[0] = new JButton("선(line)");
            btn[1] = new JButton("sketch(pen)");
            btn[2] = new JButton("사각형");
            btn[3] = new JButton("원");
            btn[4] = new JButton("<-");     // undo
            btn[5] = new JButton("->");     // redo
            btn[6] = new JButton("지우개");
            btn[7] = new JButton("저장");
            btn[8] = new JButton("불러오기");
            btn[9] = new JButton("색상");
            label.setText("          ");

            /*JSlider slider = new JSlider(JSlider.HORIZONTAL,0,25,5);
            slider.setMinorTickSpacing(1);
            slider.setMajorTickSpacing(5);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.addChangeListener(this);
            add(slider);*/

            //colorComboBox.setModel(new DefaultComboBoxModel<Color>(new Color[]{Color.black, Color.red, Color.blue, Color.green, Color.yellow, Color.pink, Color.magenta}));
            btn[10] = new JButton("+");
            btn[11] = new JButton("-");
            btn[12] = new JButton("Clear");
            fontsize.setText("Font size: " + CurrentStroke);
            //strokeComboBox.setModel(new DefaultComboBoxModel<Float>(new Float[]{(float)5, (float)10, (float)15, (float)20, (float)25}));

            bottonpanel.add(btn[0]);
            bottonpanel.add(btn[1]);
            bottonpanel.add(btn[2]);
            bottonpanel.add(btn[3]);
            bottonpanel.add(btn[4]);
            bottonpanel.add(btn[5]);
            bottonpanel.add(btn[6]);
            bottonpanel.add(btn[12]);
            bottonpanel.add(btn[7]);
            bottonpanel.add(btn[8]);
            bottonpanel.add(btn[9]);
            bottonpanel.add(label);
            bottonpanel.add(btn[10]);
            bottonpanel.add(btn[11]);
            bottonpanel.add(fontsize);
            //add(colorComboBox);
            //add(strokeComboBox);

            btn[0].setBackground(Color.PINK);
            btn[1].setBackground(Color.PINK);
            btn[2].setBackground(Color.PINK);
            btn[3].setBackground(Color.PINK);
            btn[4].setBackground(Color.PINK);
            btn[5].setBackground(Color.PINK);
            btn[6].setBackground(Color.PINK);
            btn[7].setBackground(Color.PINK);
            btn[8].setBackground(Color.PINK);
            btn[9].setBackground(Color.PINK);
            btn[10].setBackground(Color.PINK);
            btn[11].setBackground(Color.PINK);
            btn[12].setBackground(Color.PINK);
            label.setBackground(CurrentColor);
            //label.setSize(10, 50);
            label.setOpaque(true);
            //colorComboBox.setBackground(Color.PINK);
            //strokeComboBox.setBackground(Color.PINK);

            btn[0].addActionListener(this);
            btn[1].addActionListener(this);
            btn[2].addActionListener(this);
            btn[3].addActionListener(this);
            btn[4].addActionListener(this);
            btn[5].addActionListener(this);
            btn[6].addActionListener(this);
            btn[7].addActionListener(this);
            btn[8].addActionListener(this);
            btn[9].addActionListener(this);
            btn[10].addActionListener(this);
            btn[11].addActionListener(this);
            btn[12].addActionListener(this);
            //colorComboBox.addActionListener(this);
            //strokeComboBox.addActionListener(this);

            addMouseListener(this);
            addMouseMotionListener(this);

            setBackground(Color.white);
        }

        public void mouseDragged(MouseEvent e) {

            if(CurrentMode == SKETCH || CurrentMode == ERASER){
                vEnd.add(e.getPoint());
                repaint();

                vStart.add(e.getPoint());
                mode.add(CurrentMode);
                color.add(CurrentColor);
                stroke.add(CurrentStroke);
            }
            else{
                rubEnd = e.getPoint();

                rubwidth = Math.abs(rubEnd.x - rubStart.x);
                rubheight = Math.abs(rubEnd.y - rubStart.y);
                rubPointx = Math.min(rubStart.x, rubEnd.x);
                rubPointy = Math.min(rubStart.y, rubEnd.y);

                Graphics g = getGraphics();
                Graphics2D g2 = (Graphics2D)g;

                if(CurrentMode == LINE){
                    g2.setColor(Color.lightGray);
                    g2.setStroke(new BasicStroke(CurrentStroke, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1, dash3,0));
                    g2.drawLine(rubStart.x, rubStart.y, rubEnd.x, rubEnd.y);
                }
                else if(CurrentMode == RECT){
                    g2.setColor(Color.lightGray);
                    g2.setStroke(new BasicStroke(CurrentStroke, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1, dash3,0));
                    g2.drawRect(rubPointx, rubPointy, rubwidth, rubheight);
                }
                else if(CurrentMode == CIRCLE){
                    g2.setColor(Color.lightGray);
                    g2.setStroke(new BasicStroke(CurrentStroke, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1, dash3,0));
                    g2.drawOval(rubPointx, rubPointy, rubwidth, rubheight);
                }
                repaint();
            }

        }
        @Override
        public void mouseMoved(MouseEvent e) {

        }

        @Override
        public void mouseClicked(MouseEvent e) {        //마우스 버튼 클릭

        }

        @Override
        public void mousePressed(MouseEvent e) {        //마우스 버튼 누름

            vStart.add(e.getPoint());
            rubStart = e.getPoint();

            mode.add(CurrentMode);
            color.add(CurrentColor);
            stroke.add(CurrentStroke);

        }

        @Override
        public void mouseReleased(MouseEvent e) {       //마우스 버튼을 놓음

            vEnd.add(e.getPoint());
            repaint();

            vStart.add(new Point(-1, -1));
            vEnd.add(new Point(-1, -1));
            mode.add(-1);
            color.add(Color.black);
            stroke.add(-1.0f);

        }

        @Override
        public void mouseEntered(MouseEvent e) {        //마우스가 윈도우 안에 들어옴

        }

        @Override
        public void mouseExited(MouseEvent e) {     //마우스가 윈도우 밖으로 나감

        }

        public void paint(Graphics g){

            super.paint(g);
            Graphics2D g2 = (Graphics2D)g;
            sav.setColor(Color.white);
            sav.fillRect(0,0,1300,700);
            if(read){
                g.drawImage(readimage, 0, 0, 1300, 700, this);
                sav.drawImage(readimage, 0, 0, 1300, 700, this);
            }

            for(int i = 0; i < vEnd.size(); i++){

                width = Math.abs(vEnd.get(i).x - vStart.get(i).x);
                height = Math.abs(vEnd.get(i).y - vStart.get(i).y);
                Pointx = Math.min(vStart.get(i).x, vEnd.get(i).x);
                Pointy = Math.min(vStart.get(i).y, vEnd.get(i).y);

                if(mode.get(i) == LINE){
                    System.out.println("선(line) 그리기");
                    g2.setColor(color.get(i));
                    g2.setStroke(new BasicStroke(stroke.get(i)));
                    g2.drawLine(vStart.get(i).x, vStart.get(i).y, vEnd.get(i).x, vEnd.get(i).y);
                    sav.setColor(color.get(i));
                    sav.setStroke(new BasicStroke(stroke.get(i)));
                    sav.drawLine(vStart.get(i).x, vStart.get(i).y, vEnd.get(i).x, vEnd.get(i).y);
                }
                else if(mode.get(i) == RECT){
                    System.out.println("사각형 그리기");
                    g2.setColor(color.get(i));
                    g2.setStroke(new BasicStroke(stroke.get(i)));
                    g2.drawRect(Pointx, Pointy, width, height);
                    sav.setColor(color.get(i));
                    sav.setStroke(new BasicStroke(stroke.get(i)));
                    sav.drawRect(Pointx, Pointy, width, height);
                }
                else if(mode.get(i) == CIRCLE){
                    System.out.println("원 그리기");
                    g2.setColor(color.get(i));
                    g2.setStroke(new BasicStroke(stroke.get(i)));
                    g2.drawOval(Pointx, Pointy, width, height);
                    sav.setColor(color.get(i));
                    sav.setStroke(new BasicStroke(stroke.get(i)));
                    sav.drawOval(Pointx, Pointy, width, height);
                }
                else if(mode.get(i) == SKETCH){
                    System.out.println("펜 그리기");
                    g2.setColor(color.get(i));
                    g2.setStroke(new BasicStroke(stroke.get(i)));
                    g2.drawLine(vStart.get(i).x, vStart.get(i).y, vEnd.get(i).x, vEnd.get(i).y);
                    sav.setColor(color.get(i));
                    sav.setStroke(new BasicStroke(stroke.get(i)));
                    sav.drawLine(vStart.get(i).x, vStart.get(i).y, vEnd.get(i).x, vEnd.get(i).y);
                }
                else if(mode.get(i) == ERASER){
                    System.out.println("지우개");
                    g2.setColor(Color.white);
                    g2.setStroke(new BasicStroke(stroke.get(i)));
                    g2.drawLine(vStart.get(i).x, vStart.get(i).y, vEnd.get(i).x, vEnd.get(i).y);
                    sav.setColor(Color.white);
                    sav.setStroke(new BasicStroke(stroke.get(i)));
                    sav.drawLine(vStart.get(i).x, vStart.get(i).y, vEnd.get(i).x, vEnd.get(i).y);
                }
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("선(line)")){
                CurrentMode = LINE;
            }
            else if(e.getActionCommand().equals("사각형")){
                CurrentMode = RECT;
            }
            else if(e.getActionCommand().equals("원")){
                CurrentMode = CIRCLE;
            }
            else if(e.getActionCommand().equals("sketch(pen)")){
                CurrentMode = SKETCH;
            }
            else if(e.getSource().equals(colorComboBox)){
                if(CurrentMode == ERASER)
                    CurrentMode = BeforeMode;
                CurrentColor = (Color)colorComboBox.getSelectedItem();
            }
            else if(e.getSource().equals(strokeComboBox)){
                CurrentStroke = (Float)strokeComboBox.getSelectedItem();
            }
            else if(e.getActionCommand().equals("<-")){
                for(int i = vEnd.size()-1; i >= 0; i--){
                    vUndo.add(new undo(vStart.get(i), vEnd.get(i), mode.get(i), color.get(i), stroke.get(i)));
                    vStart.remove(i);
                    vEnd.remove(i);
                    color.remove(i);
                    stroke.remove(i);
                    mode.remove(i);
                    if(i-1 > 0 && mode.get(i-1) == -1)
                        break;
                }
                Graphics g = getGraphics();
                paint(g);
            }
            else if(e.getActionCommand().equals("->")){
                for(int i = vUndo.size()-1; i >= 0; i--){
                    vStart.add(vUndo.get(i).uStart);
                    vEnd.add(vUndo.get(i).uEnd);
                    color.add(vUndo.get(i).uColor);
                    stroke.add(vUndo.get(i).uStroke);
                    mode.add(vUndo.get(i).uMode);
                    vUndo.remove(i);
                    if(i-1 > 0 && vUndo.get(i-1).uMode == -1)
                        break;
                }
                Graphics g = getGraphics();
                paint(g);
            }
            else if(e.getActionCommand().equals("지우개")){
                if(CurrentMode != ERASER)
                    BeforeMode = CurrentMode;
                CurrentMode = ERASER;
            }
            else if(e.getActionCommand().equals("저장")){
                System.out.println("저장");
                chooser.setFileFilter(new FileNameExtensionFilter("jpg","jpg"));        // 파일 필터
                chooser.setMultiSelectionEnabled(false);        //다중 선택 불가
                if(chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){    //디렉토리를 선택했으면
                    try{
                        File file = new File(chooser.getSelectedFile().getAbsolutePath() + ".jpg");  //선택된 디렉토리 저장
                        ImageIO.write(img, "jpg", file);
                    }catch(Exception a){
                        a.printStackTrace();
                    }
                }
                Graphics g = getGraphics();
                paint(g);
            }
            else if(e.getActionCommand().equals("불러오기")){
                System.out.println("불러오기");
                read = true;
                FileNameExtensionFilter filter = (new FileNameExtensionFilter("jpg","jpg"));
                chooser.setMultiSelectionEnabled(false);
                chooser.setFileFilter(filter);
                if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
                    readimage = new ImageIcon(chooser.getSelectedFile().getAbsolutePath()).getImage();
                }
                Graphics g = getGraphics();
                paint(g);
            }
            else if(e.getActionCommand().equals("색상")){
                if(CurrentMode == ERASER)
                    CurrentMode = BeforeMode;
                JColorChooser cd = new JColorChooser();
                CurrentColor = cd.showDialog(this, "색상", Color.BLACK);
                label.setBackground(CurrentColor);
            }
            else if(e.getActionCommand().equals("+")){
                if(CurrentStroke < 25)
                    CurrentStroke++;
                fontsize.setText("Font size: " + CurrentStroke);
            }
            else if(e.getActionCommand().equals("-")){
                if(CurrentStroke > 1)
                    CurrentStroke--;
                fontsize.setText("Font size: " + CurrentStroke);
            }
            else if(e.getActionCommand().equals("Clear")){
                vStart.clear();
                vEnd.clear();
                mode.clear();
                color.clear();
                stroke.clear();
                vUndo.clear();
                repaint();
            }
        }
        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider value = (JSlider)e.getSource();
            CurrentStroke = (float)value.getValue();
        }
    }

    public static void main(String[] args){
        new drawboard();
    }
}