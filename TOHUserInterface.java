import javax.swing.*;        
import java.awt.*;           // For Graphics, etc.
import java.awt.geom.*;      // For Ellipse2D, etc.
import java.awt.image.*;
import java.awt.event.*;
import java.awt.font.*;

public class TOHUserInterface extends JFrame implements KeyListener, MouseListener
{
  public TOHUserInterface ()
  {
    super (TOHUserInterface.getDefaultGraphicsConf ());
    application = this;
  }
    
  public static int getDisplayRefreshRate ()
  {
    return (GraphicsEnvironment.getLocalGraphicsEnvironment ()
            .getDefaultScreenDevice ().getDisplayMode ()
            .getRefreshRate ());
  }
  
  public static GraphicsConfiguration getDefaultGraphicsConf ()
  {
    return (GraphicsEnvironment.getLocalGraphicsEnvironment ()
            .getDefaultScreenDevice ().getDefaultConfiguration ());
  }

  public static void start ()
  {
    // schedule a job for the event-dispatching thread: creating and
    // showing this application's GUI and starting application timer
    javax.swing.SwingUtilities.invokeLater (
      new Runnable()
      {
        public void run() { initializeGUIAndTimer (); }
      }
      );
  }
  
  private static void initializeGUIAndTimer ()
  {
    // basic elements and settings
    application.setIgnoreRepaint (true);
    application.setResizable (false);
    
    application.canvas = new Canvas ();
    canvas.setIgnoreRepaint (true);
    Container content = application.getContentPane ();
    application.canvas.setPreferredSize (new Dimension (XSIZE, YSIZE));
    content.add (canvas);
    application.pack ();
    application.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    application.addWindowListener (new WindowAdapter ()
      {
        public void windowOpened(WindowEvent e)
        {
          application.initializeApplication ();
          application.applicationUpdateTimer.start ();
        } 
        public void windowClosing(WindowEvent e) {}
        public void windowClosed(WindowEvent e) {}
        public void windowIconified(WindowEvent e) {}
        public void windowDeiconified(WindowEvent e) {}
        public void windowActivated(WindowEvent e) {}
        public void windowDeactivated(WindowEvent e) {}
        public void windowStateChanged(WindowEvent e) {}
        public void windowGainedFocus(WindowEvent e) {}
        public void windowLostFocus(WindowEvent e) {}
      });
        
    application.validate ();
    application.canvas.createBufferStrategy (3);
    application.bufferStrategy = application.canvas.getBufferStrategy ();
    
    // exit if window closed
    application.addWindowListener(new WindowAdapter()
      {
        public void windowClosing(WindowEvent e)
        {
          System.exit (0);
        }
      }
      );

    // get ready for key and mouse input and make window visible
    canvas.setFocusable (true);
    canvas.addKeyListener (application);
    canvas.requestFocus ();
    canvas.addMouseListener (application);
    application.setVisible (true);

    // game update timer
    ActionListener updateTimerListener = new ActionListener ()
      {
        public void actionPerformed (ActionEvent e)
        {
          application.updateApplication ();
        }
      };
    application.applicationUpdateTimer = new Timer (application.updateIntervalMs,
                                                    updateTimerListener);
    application.applicationUpdateTimer.setLogTimers (false);
    application.applicationUpdateTimer.setCoalesce (true);
  }
    
  public static final int XSIZE = 1024;
  public static final int YSIZE = 768;
  public static Canvas canvas;
  public static BufferStrategy bufferStrategy;
  private static Timer applicationUpdateTimer;
  private static TOHUserInterface application;
  
  public final static int updateIntervalMs = 500;
  protected Graphics2D graphics = null;
  private boolean useHardwareAcceleratedImages = false;

  private void updateApplication ()
  {
    decideAndDo ();
    do
    {
      do
      {
        graphics = (Graphics2D) bufferStrategy.getDrawGraphics ();
        drawBackground ();
        drawApplication ();
        graphics.dispose ();
        graphics = null;
      } while (bufferStrategy.contentsRestored ());
      
      updateScreen ();
    } while (bufferStrategy.contentsLost ());
  }
    
  private void drawBackground ()
  {
    assert graphics != null;
    graphics.setColor (Color.black);
    graphics.fillRect (0, 0, XSIZE - 1, YSIZE - 1);
    graphics.setColor (Color.green);
  }

  public void drawString (String str,
                          int x,
                          int y,
                          int fontSize,
                          boolean antiAliasing)
  {
    assert graphics != null;
    if (antiAliasing)
      graphics.setRenderingHint (RenderingHints.KEY_ANTIALIASING,
                                 RenderingHints.VALUE_ANTIALIAS_ON);
    Font font = new Font ("Courier", Font.PLAIN, fontSize);
    graphics.setFont (font);
    graphics.drawString (str, x, y);
  }

  public void drawString (String str,
                          int x,
                          int y,
                          int fontSize)
  {
    assert graphics != null;
    drawString (str, x, y, fontSize, true);
  }
    
  public void drawString (String str,
                          int x,
                          int y)
  {
    assert graphics != null;
    drawString (str, x, y, 16);
  }

  public void initializeApplication ()
  {
  }
  
  public void decideAndDo ()
  {
  }
  
  public void drawApplication ()
  {
  }
  
  private void updateScreen ()
  {
    bufferStrategy.show();
    Toolkit.getDefaultToolkit().sync();	
  }

  public void keyTyped (KeyEvent e)
  {
    if (e.getKeyChar () == 'q')
      System.exit (0);
  }

  public void keyPressed (KeyEvent e)
  {
    if (e.getKeyCode () == KeyEvent.VK_ENTER)
      enterPressed ();
  }
  
  public void enterPressed ()
  {
  }
  
  public void keyReleased (KeyEvent e)
  {
  }

  public void mouseClicked (MouseEvent e)
  {
  }

  public void mouseEntered (MouseEvent e)
  {
  }

  public void mouseExited (MouseEvent e)
  {
  }

  public void mousePressed (MouseEvent e)
  {
  }

  public void mouseReleased (MouseEvent e)
  {
  }

}
