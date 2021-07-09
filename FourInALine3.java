import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class FourInALine3 extends JFrame implements KeyListener  {
   private GameHandler handler;
   private JTextArea textArea = new JTextArea();

   public FourInALine3() {
      setTitle("공부하기 싫어!!!!");
      setSize(535, 385);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLocationRelativeTo(null);
      textArea.setFont(new Font("Courier New", Font.PLAIN, 30));
      textArea.addKeyListener(this);
      add(textArea);
      textArea.setEditable(false);

      setVisible(true);
      handler = new GameHandler(textArea);
      new Thread(new GameThread()).start();

   }
   class GameThread implements Runnable{

      @Override
      public void run() {
         // TODO Auto-generated method stub
         while(!handler.isGameOver()) {
            handler.gameTiming();


            handler.gameLogic();

            handler.drawAll();
         }
         handler.drawGameOver();
      }

   }
   public void keyPressed(KeyEvent e) {
      switch(e.getKeyCode())
      {
      case KeyEvent.VK_RIGHT:
         handler.moveRightBlock();
         break;

      case KeyEvent.VK_LEFT:
         handler.moveLeftBlock();
         break;

      case KeyEvent.VK_DOWN:
         handler.moveDownBlock();
         break;

      case KeyEvent.VK_Y:
         if (handler.isGameOver())
            restart();
         break;

      case KeyEvent.VK_N:
         if (handler.isGameOver()) {
            BufferedWriter out;
            try {
               out= new BufferedWriter(new FileWriter("FourInALine.txt"));
               out.write(String.valueOf(handler.currentWhite()));
               out.newLine();    
               out.write(String.valueOf(handler.currentBlack()));
               out.close();
            } catch (IOException e1) 
            {
               e1.printStackTrace();
            }
            System.exit(0);
         }
      }
   }
   private void restart() {
      // TODO Auto-generated method stub
      handler.initData();
      new Thread(new GameThread()).start();

   }
   public void keyTyped(KeyEvent e) {

   }
   public void keyReleased(KeyEvent e) {

   }
   public static void main(String[] args) {
      // TODO Auto-generated method stub
      new FourInALine3();
   }

}
class GameHandler{

   private final int SCREEN_WIDTH = 38;
   private final int LEFT_PADDING = 1;
   private final int SCREEN_HEIGHT = 25;
   private final int FIELD_WIDTH = 13;
   private final int FIELD_HEIGHT = 8;
   private JTextArea textArea;
   private char[][] buffer;
   private int field[];
   private boolean isGameOver=false;
   private int currentBlack;
   private int currentWhite;
   private int input;
   private int user;
   private int previousBlack,previousWhite;
   private int temp=7;
   private boolean LetCheckStone;
   private boolean CheckStone = false;
   int checkx,checky;

   public GameHandler(JTextArea ta) {
      // TODO Auto-generated constructor stub
     textArea=ta;
      field= new int[ FIELD_WIDTH*FIELD_HEIGHT];
      buffer=new char[SCREEN_WIDTH][SCREEN_HEIGHT];
      initData();

   }

   public boolean CheckStone(int xc, int cy)
   {
      int count = 0;
      for (int x = xc; x < FIELD_WIDTH; x += 2) {
         if (field[cy * FIELD_WIDTH + x] == user % 2 + 11) {
            count++;
         } else
            break;
      }
      for (int x = xc - 2; x > -1; x -= 2) {
         if (field[cy * FIELD_WIDTH + x] == user % 2 + 11) {
            count++;
         } else
            break;
      }
      if (count > 3) 
         return true;


      count = 0;

      // 세로
      for (int y = cy; y < FIELD_HEIGHT; y++) {//위
         if (field[y * FIELD_WIDTH + xc] == user % 2 + 11) {
            count++;
         } else
            break;
      }
      for (int y = cy - 1; y > -1; y--) {//아래
         if (field[y * FIELD_WIDTH + xc] == user % 2 + 11) {
            count++;
         } else
            break;
      }
      if (count > 3)
         return true;
      count = 0;

      // 대각선 /
      for (int i = 0;; i++) {
         if (cy - i < 0 || xc + i * 2 > FIELD_WIDTH)
            break;
         if (field[(cy - i) * FIELD_WIDTH + xc + i * 2] ==user % 2 + 11) {
            count++;
         } else
            break;
      }

      for (int i = 1;; i++) {
         if (cy + i > FIELD_HEIGHT - 1 || xc - i < 0)
            break;
         if (field[(cy + i) * FIELD_WIDTH + xc - i * 2] ==user % 2 + 11) {
            count++;
         } else
            break;
      }
      if (count > 3)
         return true;
      count = 0;

      // 대각선 \
      for (int i = 0;; i++) {
         if (xc + i * 2 > FIELD_WIDTH - 1 || cy + i >FIELD_HEIGHT - 1)
            break;
         if (field[(cy + i) * FIELD_WIDTH + xc + i * 2] ==user % 2 + 11) {
            count++;
         } else
            break;
      }
      for (int i = 1;; i++) {
         if (cy + i > FIELD_HEIGHT - 1 || xc - i * 2 < 0 ||cy - i < 0)
            break;
         if (field[(cy - i) * FIELD_WIDTH + xc - i * 2] ==user % 2 + 11) {
            count++;
         } else
            break;
      }
      if (count > 3)
         return true;
      else 
         return false;



   }


   public int currentWhite() {
      // TODO Auto-generated method stub
      return currentBlack;
   }

   public int currentBlack() {
      // TODO Auto-generated method stub
      return currentWhite;
   }

   public void moveDownBlock() {
      // TODO Auto-generated method stub


      while(true) {
         if(field[temp*FIELD_WIDTH + (input*2)]!=11&&field[temp*FIELD_WIDTH + (input*2)]!=12)
         {   
            field[temp * FIELD_WIDTH + (input * 2)] = user % 2 + 11;
            field[1 * FIELD_WIDTH + (input * 2)] = 0;
            checkx=input*2;
            checky=temp;

            break;


         }
         else 
         {
            temp--;

         }

      }

      isGameOver=CheckStone(checkx,checky);
      temp=7;
      input = 3;
      user++;

   }

   public void moveLeftBlock() {
      // TODO Auto-generated method stub
      input--;
      if(input<0)
         input++;
   }

   public void moveRightBlock() {
      // TODO Auto-generated method stub
      input++;
      if(input>6)
         input--;
   }



   public void initData() {
      // TODO Auto-generated method stub
      try {
         BufferedReader in = new BufferedReader(new FileReader("FourInALine.txt"));
         previousBlack = Integer.parseInt(in.readLine());
         previousWhite = Integer.parseInt(in.readLine());
      } catch(FileNotFoundException e){
         previousBlack = 0;
         previousWhite = 0;

      }catch (IOException e) {
         e.printStackTrace();
      }

      input=3;
      user=0;
      isGameOver=false;

      for(int x=0;x<FIELD_WIDTH;x++)
         for(int y=2;y<FIELD_HEIGHT;y++)
         { switch (x) {
         case 0:

            if (y == 2)
               field[y * FIELD_WIDTH + x] = 2;//r
            else   if (y == FIELD_HEIGHT - 1)
               field[y *  FIELD_WIDTH + x] = 8;//└
            else
               field[y *  FIELD_WIDTH + x] = 5;//├
            break;
         case 2:
         case 4:
         case 6:
         case 8:

         case 10:
            if (y == 2)
               field[y *  FIELD_WIDTH + x] = 3;//┬
            else if (y == FIELD_HEIGHT - 1)
               field[y *  FIELD_WIDTH + x] = 9;//┴
            else
               field[y * FIELD_WIDTH + x] = 6;//┼
            break;

         case 12:
            if (y == 2)
               field[y *  FIELD_WIDTH+ x] = 4;//┐
            else if (y == FIELD_HEIGHT - 1)
               field[y * FIELD_WIDTH + x] = 10;//┘
            else
               field[y * FIELD_WIDTH+ x] = 7;//┤
            break;

         case 1:
         case 3:
         case 5:
         case 7:
         case 9:
         case 11:
            field[y *  FIELD_WIDTH + x] = 1;
            break;//─
         }


         }
      clearBuffer();
   }


   private void clearBuffer() {
      for (int y = 0; y < SCREEN_HEIGHT; y++) {
         for (int x = 0; x < SCREEN_WIDTH; x++) {
            buffer[x][y] = ' ';
         }
      }
   }
   private void drawToBuffer(int px, int py, String c) {
      for (int x = 0; x < c.length(); x++) {
         buffer[px + x + LEFT_PADDING][py] = c.charAt(x);
      }
   }
   private void drawToBuffer(int px, int py, char c) {
      buffer[px + LEFT_PADDING][py] = c;
   }

   public void drawGameOver() {
      // TODO Auto-generated method stub

      drawToBuffer(14, 4, "╔════════════╗");
      drawToBuffer(14, 5, "║  " + " ─┌┬┐├┼┤└┴┘○● ".charAt(user % 2 + 11) + " WINS!   ║");
      drawToBuffer(14, 6, "║            ║");
      drawToBuffer(14, 7, "║AGAIN? (Y/N)║");
      drawToBuffer(14, 8, "╚════════════╝");
      render();
      //점수플러스
      if (user % 2 == 1)
         currentBlack++;
      else
         currentWhite++;

   }

   public void drawAll() {

      for (int x = 0; x < FIELD_WIDTH; x++) {
         for (int y = 0; y < FIELD_HEIGHT; y++)
         { 
            drawToBuffer(x, y, " ─┌┬┐├┼┤└┴┘●○".charAt(field[y * FIELD_WIDTH + x]));
         }

      }

      drawToBuffer(input * 2, 1, " ─┌┬┐├┼┤└┴┘●○ ".charAt(user % 2 + 11));

      String current = "│ ●:" + currentBlack + "  " + "○:" + currentWhite + "  │";
      String previous = "│ ●:" + previousBlack + "  " + "○:" + previousWhite + "  │";

      drawToBuffer(15, 1, "┌──CURRENT──┐");
      drawToBuffer(15, 2, current);
      drawToBuffer(15, 3, "└───────────┘");
      drawToBuffer(15, 5, "┌──PREVIOUS─┐");
      drawToBuffer(15, 6, previous);
      drawToBuffer(15, 7, "└───────────┘");
      drawToBuffer(20, 8, "by D.Lee");
      render();


   }

   private void render() {
      StringBuilder sb = new StringBuilder();
      for (int y = 0; y < SCREEN_HEIGHT; y++) {
         for (int x = 0; x < SCREEN_WIDTH; x++) {
            sb.append(buffer[x][y]);
         }
         sb.append("\n");
      }
      textArea.setText(sb.toString());
   }

   public void gameLogic() { 




   }



   public void gameTiming() {
      // TODO Auto-generated method stub
      try {
         Thread.sleep(50);
      } catch(InterruptedException ex) {
         ex.printStackTrace();
      }

   }

   public boolean isGameOver() {
      // TODO Auto-generated method stub
      return isGameOver;
   }

}