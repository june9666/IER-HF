package PhysicalModell.src;

import jason.asSyntax.Literal;
import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class WorldView extends GridWorldView {

    IntelligentFactory env = null;
    
    public WorldView(WorldModel model) {
        super(model, "Gyár figyelõ", 700);
        setVisible(true);
        repaint();
    }

    public void setEnv(IntelligentFactory env) {
        this.env = env;
        scenarios.setSelectedIndex(env.getSimId()-1);
    }
    
    JLabel    jlMouseLoc;
    JComboBox scenarios;
    JSlider   jSpeed;
    JLabel    jGoldsC;
   
    //Szenzor értékei
    JLabel jlTemp1;
    JLabel jlTemp2;
    JLabel jlTemp3;
    JLabel jlTemp4;
   
    JLabel jlPressure1;
    JLabel jlPressure2;
    JLabel jlPressure3;
    JLabel jlPressure4;
   
    JLabel jlWearlevel1;
    JLabel jlWearlevel2;
    JLabel jlWearlevel3;
    JLabel jlWearlevel4;

    @Override
    public void initComponents(int width) {
        super.initComponents(width);
        scenarios = new JComboBox();
        for (int i=1; i<=1; i++) {
            scenarios.addItem(i);
        }
        JPanel args = new JPanel();
        args.setLayout(new BoxLayout(args, BoxLayout.Y_AXIS));
 
        JPanel sp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sp.setBorder(BorderFactory.createEtchedBorder());
        sp.add(new JLabel("Scenario:"));
        sp.add(scenarios);
       
        jSpeed = new JSlider();
        jSpeed.setMinimum(0);
        jSpeed.setMaximum(400);
        jSpeed.setValue(4000);
        jSpeed.setPaintTicks(true);
        jSpeed.setPaintLabels(true);
        jSpeed.setMajorTickSpacing(100);
        jSpeed.setMinorTickSpacing(20);
        jSpeed.setInverted(true);
        Hashtable<Integer,Component> labelTable = new Hashtable<Integer,Component>();
        labelTable.put( 0, new JLabel("max") );
        labelTable.put( 200, new JLabel("speed") );
        labelTable.put( 400, new JLabel("min") );
        jSpeed.setLabelTable( labelTable );
        JPanel p = new JPanel(new FlowLayout());
        p.setBorder(BorderFactory.createEtchedBorder());
        p.add(jSpeed);
       
        //1. szenzor értékei
        JPanel sensor1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sensor1.setBorder(BorderFactory.createEtchedBorder());
       
        sensor1.add(new JLabel("Wearlevel_1:"));
        jlWearlevel1 = new JLabel("0");
        sensor1.add(jlWearlevel1);
        sensor1.add(new JLabel("Temp_1:"));
        jlTemp1 = new JLabel("0");
        sensor1.add(jlTemp1);
        sensor1.add(new JLabel("Pressure_1:"));
        jlPressure1 = new JLabel("0");
        sensor1.add(jlPressure1);
                   
        //2. szenzor értékei
        JPanel sensor2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sensor2.setBorder(BorderFactory.createEtchedBorder());
        sensor2.add(new JLabel("Wearlevel_2:"));
        jlWearlevel2 = new JLabel("0");
        sensor2.add(jlWearlevel2);
        sensor2.add(new JLabel("Temp_2:"));
        jlTemp2 = new JLabel("0");
        sensor2.add(jlTemp2);
        sensor2.add(new JLabel("Pressure_2:"));
        jlPressure2 = new JLabel("0");
        sensor2.add(jlPressure2);
       
        //3. szenzor értékei
        JPanel sensor3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sensor3.setBorder(BorderFactory.createEtchedBorder());
        sensor3.add(new JLabel("Wearlevel_3:"));
        jlWearlevel3 = new JLabel("0");
        sensor3.add(jlWearlevel3);
        sensor3.add(new JLabel("Temp_3:"));
        jlTemp3 = new JLabel("0");
        sensor3.add(jlTemp3);
        sensor3.add(new JLabel("Pressure_3:"));
        jlPressure3 = new JLabel("0");
        sensor3.add(jlPressure3);
       
        //4. szenzor értékei
        JPanel sensor4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sensor4.setBorder(BorderFactory.createEtchedBorder());
        sensor4.add(new JLabel("Wearlevel_4:"));
        jlWearlevel4 = new JLabel("0");
        sensor4.add(jlWearlevel4);
        sensor4.add(new JLabel("Temp_4:"));
        jlTemp4 = new JLabel("0");
        sensor4.add(jlTemp4);
        sensor4.add(new JLabel("Pressure_4:"));
        jlPressure4 = new JLabel("0");
        sensor4.add(jlPressure4);
       
        args.add(sp);
        args.add(p);
        args.add(sensor1);
        args.add(sensor2);
        args.add(sensor3);
        args.add(sensor4);
 
        JPanel msg = new JPanel();
        msg.setLayout(new BoxLayout(msg, BoxLayout.Y_AXIS));
        msg.setBorder(BorderFactory.createEtchedBorder());
       
        p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.add(new JLabel("Click on the cells to add new fire."));
        msg.add(p);
        p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.add(new JLabel("(mouse at:"));
        jlMouseLoc = new JLabel("0,0)");
        p.add(jlMouseLoc);
        msg.add(p);
        p = new JPanel(new FlowLayout(FlowLayout.CENTER));

        msg.add(p);
 
        JPanel s = new JPanel(new BorderLayout());
        s.add(BorderLayout.WEST, args);
        s.add(BorderLayout.CENTER, msg);
        getContentPane().add(BorderLayout.SOUTH, s);        
 
        // Events handling
        jSpeed.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (env != null) {
                    env.setSleep((int)jSpeed.getValue());
                }
            }
        });
 
        scenarios.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ievt) {
                int w = ((Integer)scenarios.getSelectedItem()).intValue();
                if (env != null && env.getSimId() != w) {
                    env.endSimulation();
                    env.initWorld(w);
                }
            }            
        });
       
        getCanvas().addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                int col = e.getX() / cellSizeW;
                int lin = e.getY() / cellSizeH;
                if (col >= 0 && lin >= 0 && col < getModel().getWidth() && lin < getModel().getHeight()) {
                    WorldModel wm = (WorldModel)model;
                   
                    System.out.println(IntelligentFactory.fireNumber + "firenumber");
                    if(IntelligentFactory.fireNumber == 0 && wm.getAgPos(2).x == 6 &&  wm.getAgPos(2).y == 1) {
                    wm.add(WorldModel.FIRE, col, lin);
                    IntelligentFactory.fireNumber++;
                    IntelligentFactory.fire = new Location(col,lin);
                    }
               
                    update(col, lin);
               
                }
            }
            public void mouseExited(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
        });
 
        getCanvas().addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) { }
            public void mouseMoved(MouseEvent e) {
                int col = e.getX() / cellSizeW;
                int lin = e.getY() / cellSizeH;
                if (col >= 0 && lin >= 0 && col < getModel().getWidth() && lin < getModel().getHeight()) {
                    jlMouseLoc.setText(col+","+lin+")");
                }
            }            
        });
    }
    

    @Override
    public void draw(Graphics g, int x, int y, int object) {
        switch (object) {
        case WorldModel.DEPOT:   drawDepot(g, x, y);  break;
        case WorldModel.SENSOR:   drawSensor(g, x, y);  break;
        case WorldModel.FIRE:   drawFire(g, x, y);  break;
        }
        
        //1. Szenzor
        double w1 = env.press.wearlevel.get(0);
        String w1_1 = Double.toString(w1);
        String tmp1_1 = w1_1.substring(0, 5);
        jlWearlevel1.setText(tmp1_1);
        double t1 = env.press.temp.get(0);
        String t1_1 = Double.toString(t1);
        String tmp2_1 = t1_1.substring(0, 5);
        jlTemp1.setText(tmp2_1);
        double p1 = env.press.pressure.get(0);
        String p1_1 = Double.toString(p1);
        String tmp3_1 = p1_1.substring(0, 5);
        jlPressure1.setText(tmp3_1);
       
        //2.Szernzor
        double w2 = env.press.wearlevel.get(1);
        String w2_1 = Double.toString(w2);
        String tmp1_2 = w2_1.substring(0, 5);
        jlWearlevel2.setText(tmp1_2);
        double t2 = env.press.temp.get(1);
        String t2_1 = Double.toString(t2);
        String tmp2_2 = t2_1.substring(0, 5);
        jlTemp2.setText(tmp2_2);
        double p2 = env.press.pressure.get(1);
        String p2_1 = Double.toString(p2);
        String tmp3_2 = p2_1.substring(0, 5);
        jlPressure2.setText(tmp3_2);
       
        //3.Szenzos
        double w3 = env.press.wearlevel.get(2);
        String w3_1 = Double.toString(w3);
        String tmp1_3 = w3_1.substring(0, 5);
        jlWearlevel3.setText(tmp1_3);  
        double t3 = env.press.temp.get(2);
        String t3_1 = Double.toString(t3);
        String tmp2_3 = t3_1.substring(0, 5);
        jlTemp3.setText(tmp2_3);
        double p3 = env.press.pressure.get(2);
        String p3_1 = Double.toString(p3);
        String tmp3_3 = p3_1.substring(0, 5);
        jlPressure3.setText(tmp3_3);
       
        //4. Szenzor
        double w4 = env.press.wearlevel.get(3);
        String w4_1 = Double.toString(w4);
        String tmp1_4 = w4_1.substring(0, 5);
        jlWearlevel4.setText(tmp1_4);
        double t4 = env.press.temp.get(3);
        String t4_1 = Double.toString(t4);
        String tmp2_4 = t4_1.substring(0, 5);
        jlTemp4.setText(tmp2_4);
        double p4 = env.press.pressure.get(3);
        String p4_1 = Double.toString(p4);
        String tmp3_4 = p4_1.substring(0, 5);
        jlPressure4.setText(tmp3_4);
    
    }

    @Override
    public void drawAgent(Graphics g, int x, int y, Color c, int id) {
        Color idColor = Color.black;

            super.drawAgent(g, x, y, c, -1);
            idColor = Color.white;
        
        g.setColor(idColor);
        if(id == 0)
        	drawString(g, x, y, defaultFont, "Ellenõr");
        if(id == 1) {
       	 BufferedImage img = null;
		try {
			img = ImageIO.read(new File("maint.jpeg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
				g.drawImage(img, (x * cellSizeW ), y * cellSizeH  , null);
       }
        if(id == 2) {
        	 BufferedImage img = null;
		try {
			img = ImageIO.read(new File("firefighter.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
       
        g.drawImage(img, (x * cellSizeW ), y * cellSizeH  , null);
        }
   }
    public void drawDepot(Graphics g, int x, int y) {
        g.setColor(Color.gray);
        g.fillRect(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);
        g.setColor(Color.pink);
        g.drawRect(x * cellSizeW + 2, y * cellSizeH + 2, cellSizeW - 4, cellSizeH - 4);
        g.drawLine(x * cellSizeW + 2, y * cellSizeH + 2, (x + 1) * cellSizeW - 2, (y + 1) * cellSizeH - 2);
        g.drawLine(x * cellSizeW + 2, (y + 1) * cellSizeH - 2, (x + 1) * cellSizeW - 2, y * cellSizeH + 2);
    }

  
    public void drawSensor(Graphics g, int x, int y) {
   	 BufferedImage img = null;
   		try {
   			img = ImageIO.read(new File("sensor.png"));
   		} catch (IOException e) {
   			e.printStackTrace();
   		}
          
           g.drawImage(img, (x * cellSizeW +2), y * cellSizeH+2  , null);
    }
    
    public void drawFire(Graphics g, int x, int y) {

        BufferedImage img = null;
		try {
			img = ImageIO.read(new File("fire.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        
        g.drawImage(img, (x * cellSizeW + 6), y * cellSizeH +3 , null);

    }
    
    public static void main(String[] args) throws Exception {
        IntelligentFactory env = new IntelligentFactory();
        env.init(new String[] {"5","50","yes"});
    }
}
