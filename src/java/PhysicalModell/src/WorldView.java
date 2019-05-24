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
        super(model, "Gyár figyelõ", 600);
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

    @Override
    public void initComponents(int width) {
        super.initComponents(width);
        scenarios = new JComboBox();
        for (int i=1; i<=3; i++) {
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
        jSpeed.setValue(300);
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
       
        
        JPanel sensor1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sensor1.setBorder(BorderFactory.createEtchedBorder());
        sensor1.add(new JLabel("Wearlevel:"));
        sensor1.add(new JLabel("Temp:"));
        sensor1.add(new JLabel("Pressure:"));
        
        args.add(sp);
        args.add(p);
        args.add(sensor1);

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
        p.add(new JLabel("Collected golds:"));
        jGoldsC = new JLabel("0");
        p.add(jGoldsC);
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
    }

    @Override
    public void drawAgent(Graphics g, int x, int y, Color c, int id) {
        Color idColor = Color.black;

            super.drawAgent(g, x, y, c, -1);
            idColor = Color.white;
        
        g.setColor(idColor);
        if(id == 0)
        	drawString(g, x, y, defaultFont, "Ellenõr");
        if(id == 1)
        	drawString(g, x, y, defaultFont, "Karbantartó");
        if(id == 2) {
        	 BufferedImage img = null;
		try {
			img = ImageIO.read(new File("firefighter.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
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

    public void drawGold(Graphics g, int x, int y) {
        g.setColor(Color.yellow);
        g.drawRect(x * cellSizeW + 2, y * cellSizeH + 2, cellSizeW - 4, cellSizeH - 4);
        int[] vx = new int[4];
        int[] vy = new int[4];
        vx[0] = x * cellSizeW + (cellSizeW / 2);
        vy[0] = y * cellSizeH;
        vx[1] = (x + 1) * cellSizeW;
        vy[1] = y * cellSizeH + (cellSizeH / 2);
        vx[2] = x * cellSizeW + (cellSizeW / 2);
        vy[2] = (y + 1) * cellSizeH;
        vx[3] = x * cellSizeW;
        vy[3] = y * cellSizeH + (cellSizeH / 2);
        g.fillPolygon(vx, vy, 4);
    }

    public void drawEnemy(Graphics g, int x, int y) {
        g.setColor(Color.red);
        g.fillOval(x * cellSizeW + 7, y * cellSizeH + 7, cellSizeW - 8, cellSizeH - 8);
    }
    
    public void drawSensor(Graphics g, int x, int y) {
        g.setColor(Color.green);
        g.fillOval(x * cellSizeW + 7, y * cellSizeH + 7, cellSizeW - 8, cellSizeH - 8);
    }
    
    public void drawFire(Graphics g, int x, int y) {

        BufferedImage img = null;
		try {
			img = ImageIO.read(new File("fire.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        
        g.drawImage(img, (x * cellSizeW + 3), y * cellSizeH +3 , null);

    }
    
    public static void main(String[] args) throws Exception {
        IntelligentFactory env = new IntelligentFactory();
        env.init(new String[] {"5","50","yes"});
    }
}
