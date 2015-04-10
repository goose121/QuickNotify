/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package packaged;

import com.apple.eawt.AppEvent.OpenURIEvent;
import com.apple.eawt.Application;
import com.apple.eawt.OpenURIHandler;
import java.awt.event.MouseListener;
import com.sun.jna.Library;
import com.sun.jna.Native;
import java.awt.AWTException;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TextField;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.imageio.ImageIO;
/**
 *
 * @author kiernanhagerhome
 */
interface NsUserNotificationsBridge extends Library{
    NsUserNotificationsBridge instance = (NsUserNotificationsBridge)
            Native.loadLibrary("/usr/local/lib/NsUserNotificationsBridge.dylib", NsUserNotificationsBridge.class);
    public int sendNotification(String title, String subtitle, String text, int timeoffset);
}
public class QuickNotify {
    
    public static void main(String[] args) throws AWTException, IOException{
        Application.getApplication().setDockIconImage(ImageIO.read(QuickNotify.class.getResource("iconcolor.png")));
        Application.getApplication().setOpenURIHandler(
        new OpenURIHandler() {
            @Override
            public void openURI(final OpenURIEvent pEvent) {
                    String uri = pEvent.getURI().toString();
                    String title = uri.substring(uri.indexOf("title:")+6, uri.substring(uri.indexOf("title:")+6).indexOf(","));
                    String stitle = uri.substring(uri.indexOf("subtitle:")+9, uri.substring(uri.indexOf("subtitle:")+9).indexOf(","));
                    String msg = uri.substring(uri.indexOf("message:")+8, uri.substring(uri.indexOf("message:")+8).indexOf(","));
                    String time = uri.substring(uri.indexOf("delay:")+6, uri.substring(uri.indexOf("delay:")+6).indexOf(","));
                    NsUserNotificationsBridge.instance.sendNotification(title, stitle, msg, Integer.parseInt(time));
        }
         });
        PopupMenu pt = new PopupMenu();
        TrayIcon t = new TrayIcon(ImageIO.read(QuickNotify.class.getResource("iconbw.png")), "QuickNotify", pt);
        final Frame f = new Frame();
        f.setUndecorated(true);
        final TextField title = new TextField(), subtitle = new TextField(), msg = new TextField(), sec = new TextField(), min = new TextField(), hrs = new TextField();
        Label titlel = new Label("Title"), subtitlel = new Label("Subtitle"), msgl = new Label("Message"),delay = new Label("Delay"), times = new Label("Seconds"), timem = new Label("Minutes"), timeh = new Label("Hours");
        Button b = new Button("Send");
        b.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                int tempsec=0, tempmin=0, temphrs=0;
                if("".equals(hrs.getText()))
                    hrs.setText("0");
                if("".equals(min.getText()))
                    min.setText("0");
                if("".equals(sec.getText()))
                    sec.setText("0");
        try{
        tempsec = Integer.parseInt(sec.getText());
        tempmin = Integer.parseInt(min.getText());
        temphrs = Integer.parseInt(hrs.getText());
        }catch(NumberFormatException ex){
        }
        int seconds = (temphrs*3600)+(tempmin*60)+tempsec;

                NsUserNotificationsBridge.instance.sendNotification(title.getText(), subtitle.getText(), msg.getText(), seconds);
                f.setVisible(false);
                System.out.println(seconds);
            }
        });
        Button c = new Button("Cancel");
        c.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                f.setVisible(false);
            }
        });
        Panel p = new Panel();
        p.setLayout(new GridLayout(1, 3));
        p.add(times);
        p.add(timem);
        p.add(timeh);
        Panel p2 = new Panel();
        p2.setLayout(new GridLayout(1, 3));
        p2.add(sec);
        p2.add(min);
        p2.add(hrs);
        f.setLayout(new GridLayout(10, 1));
        f.add(titlel);
        f.add(title);
        f.add(subtitlel);
        f.add(subtitle);
        f.add(msgl);
        f.add(msg);
        f.add(p);
        f.add(p2);
        f.add(b);
        f.add(c);
        f.setAlwaysOnTop(true);
        //PopupMenu pt = new PopupMenu();
        //pt.add("test");
        //t.setPopupMenu(pt);
        f.setSize(300, 200);
         t.addMouseListener(new MouseListener() {
             @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                   f.setLocation(e.getX(), 22);
                   f.setVisible(true);
                   
            }

           
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
            }
    });
         f.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }
         
         });
        SystemTray.getSystemTray().add(t);
    }
}
