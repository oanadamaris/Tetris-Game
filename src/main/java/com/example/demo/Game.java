package com.example.demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

public class Game extends Application {

    public static final int size=30; //marime patratel din bloc si cat se va misca blocul
    public static final int xmax=330;
    public static final int ymax=600;
    public static final int[][]Grid=new int[xmax/size][ymax/size];
    private static Pane together = new Pane();
    private static Scene scene = new Scene(together,550,600,Color.BLACK);///330 e chef's kiss
    public static int score = 0;
    private static int end = 0;
    private static boolean gameOn = true;
    private static int lines = 0;
    private static Block block;
    private static Block nextB = Controller.createBlock(); //primul bloc ce va fi afisat pe ecran

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //////////settin the scene
        stage.setTitle("TETRIS");
        stage.setResizable(false);
        Text Score = new Text("Score: ");
        Score.setFont(Font.font("Serif", 20));
        Score.setX(xmax + 50);
        Score.setY(50);
        Score.setFill(Color.DARKGRAY);
        Text Lines = new Text("Lines: ");
        Lines.setFont(Font.font("Serif", 20));
        Lines.setX(xmax + 50);
        Lines.setY(100);
        Lines.setFill(Color.DARKGRAY);

        together.getChildren().addAll(Score, Lines);

        ////////game stuff
        for (int i = 0; i < (xmax / size); i++) {
            for (int j = 0; j < (ymax / size); j++) {
                Grid[i][j] = 0;
            }
        }

        Block b = nextB; //creez piesa curenta
        together.getChildren().addAll(b.a, b.b, b.c, b.d);
        keyPress(b); //pun piesa ca argument ca sa o pot controla de la tastatura
        block=b; //salvez piesa curenta pt ca sa o pot folosi mai jos
        nextB=Controller.createBlock(); //pregatesc piesa urmatoare
        stage.setScene(scene);
        stage.show();

        Timer on = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        if (block.a.getY() == 0 || block.b.getY() == 0 || block.c.getY() == 0 || block.d.getY() == 0)
                            end++; //numara cate blocuri sunt la y=0
                        else
                            end = 0;
                        if(score>1000){
                            Text gameOver = new Text("YOU WON");
                            gameOver.setFont(Font.font("Serif", 50));
                            gameOver.setX(xmax/2);
                            gameOver.setY(300);
                            gameOver.setFill(Color.GOLD);
                            together.getChildren().add(gameOver);
                            gameOn=false;

                        }
                        if (end==2 && score<=1000) { //daca au urcat pana sus
                            // GAME OVER
                            Text gameOver = new Text("GAME OVER");
                            gameOver.setFont(Font.font("Serif", 50));
                            gameOver.setX(xmax/2);
                            gameOver.setY(300);
                            gameOver.setFill(Color.DARKGRAY);
                            together.getChildren().add(gameOver);
                            gameOn = false;
                        }
                        if (end == 3) {
                            System.exit(0);
                        }

                        if (gameOn) {
                            goDown(block);
                            Score.setText("Score: " + score);
                            Lines.setText("Lines: " + lines);
                        }
                    }
                });
            }
        };
        on.schedule(task, 0, 300);
    }
    private void keyPress(Block blok) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case RIGHT:
                        Controller.goRight(blok);
                        break;
                    case DOWN:
                        goDown(blok);
                        break;
                    case LEFT:
                        Controller.goLeft(blok);
                        break;
                    case UP:
                        Turn(blok);
                        break;
                }
            }
        });
    }

    public void goDown(Block blok){
        //verific daca a ajuns jos sau daca a atins alt bloc
        if(blok.a.getY()==ymax-size || blok.b.getY()==ymax-size || blok.c.getY()==ymax-size || blok.d.getY()==ymax-size
                || (collA(blok)==1) || (collB(blok)==1) || (collC(blok)==1) || (collD(blok)==1)){
            //opresc blocul pe loc
            Grid[(int) (blok.a.getX() / size)][(int) (blok.a.getY() / size)]=1;
            Grid[(int) (blok.b.getX() / size)][(int) (blok.b.getY() / size)]=1;
            Grid[(int) (blok.c.getX() / size)][(int) (blok.c.getY() / size)]=1;
            Grid[(int) (blok.d.getX() / size)][(int) (blok.d.getY() / size)]=1;
            fullRow(together);
            //creez un nou block
            Block block2=nextB;
            nextB=Controller.createBlock();
            block=block2;
            together.getChildren().addAll(block2.a, block2.b, block2.c, block2.d);
            keyPress(block2);
        }

        if(blok.a.getY()+size<ymax && blok.b.getY()+size<ymax && blok.c.getY()+size<ymax && blok.d.getY()+size<ymax) {
            //am verificat daca nu depaseste
            //verific daca patratele unde vreau sa mut blocul sunt libere
            int emptya = Grid[(int) (blok.a.getX() / size)][(int) (blok.a.getY() / size) + 1];
            int emptyb = Grid[(int) (blok.b.getX() / size)][(int) (blok.b.getY() / size) + 1];
            int emptyc = Grid[(int) (blok.c.getX() / size)][(int) (blok.c.getY() / size) + 1];
            int emptyd = Grid[(int) (blok.d.getX() / size)][(int) (blok.d.getY() / size) + 1];
            if ((emptya == 0) && (emptyb == 0) && (emptyc == 0) && (emptyd == 0)) {
                blok.a.setY(blok.a.getY() + size);
                blok.b.setY(blok.b.getY() + size);
                blok.c.setY(blok.c.getY() + size);
                blok.d.setY(blok.d.getY() + size);
            }
        }
    }
    public void fullRow(Pane pane) {
        int i=0;
        while(i<(ymax/size)) { ///i merge in sus si in jos pe deirectia lui y, deci va sta pe loc pana trecem prin
                                ///toaata patratelele din rand
            int full=0;
            for (int j=0; j<(xmax/size); j++) { ///j merge in stanga si dreapta pe directia x, de aia ne jucam cu j
                if (Grid[j][i]==1) {
                    full++;
                }
            }
            if (full == (xmax / size)) { //daca randul e plin, impart sa fie egal cu nr de coloane
                score = score + 33;
                lines++;

                Node[] children = pane.getChildren().toArray(new Node[0]);//punem copiii scenei intr-un sir de noduri ca
                //sa ramana static vectorul
                for (Node n : children) { //aici daca lasam doar pane.getChildren() stergea doar un patratel
                    if (n instanceof Rectangle) { //daca nodul e de tip patratel
                        Rectangle out = (Rectangle) n;
                        if (((int) out.getY() / size) == i) { //daca e patrat de pe randul plin
                            pane.getChildren().remove(n); //il elimin si din grid si din scena
                            Grid[((int) out.getX() / size)][((int) out.getY() / size)] = 0;
                        } else if (((int) out.getY() / size) < i) { //daca e patrat care e mai sus decat ce vreau eu sa elimin
                            Grid[((int) out.getX() / size)][((int) out.getY() / size)] = 0; //sterg ca sa fac loc
                            out.setY(out.getY() + size); //il mut mai jos
                        }
                    }
                }
                for (Node n : pane.getChildren()) { //pt ca doar parcurg lista si nu elimin
                    //ca sa updatam gridul
                    if (n instanceof Rectangle) {
                        Rectangle here = (Rectangle) n;
                        Grid[((int) here.getX() / size)][((int) here.getY() / size)] = 1;
                    }
                }
            }
            else{
                i++;
            }
        }
    }
    //verific coliziunea cu patratelele de jos
    public static int collA(Block block){
        if(Grid[(int)(block.a.getX()/size)][(int)(block.a.getY()/size)+1]==1){
            return 1;
        }
        else{
            return 0;
        }
    }
    public static int collB(Block block){
        if(Grid[(int) (block.b.getX()/size)][(int)(block.b.getY()/size)+1]==1){
            return 1;
        }
        else{
            return 0;
        }
    }
    public static int collC(Block block){
        if(Grid[(int)(block.c.getX()/size)][(int)(block.c.getY()/size)+1]==1){
            return 1;
        }
        else{
            return 0;
        }
    }
    public static int collD(Block block){
        if(Grid[(int)(block.d.getX()/size)][(int)(block.d.getY()/size)+1]==1){
            return 1;
        }
        else{
            return 0;
        }
    }
    public void Turn(Block blok){
        switch(blok.getName()){
            case "i":
                if((block.state==1) && (checkTurn(blok.a,2,-2)==1) && (checkTurn(blok.b,1,-1)==1)
                        && (checkTurn(blok.d,-1,1)==1)){
                    rUP(blok.a);
                    rUP(blok.a);
                    rRight(blok.a);
                    rRight(blok.a);
                    rUP(blok.b);
                    rRight(blok.b);
                    rDown(blok.d);
                    rLeft(blok.d);
                    blok.rotate();
                    break;
                }
                if((block.state==2) && (checkTurn(blok.a,-2,2)==1) && (checkTurn(blok.b,-1,1)==1)
                            && (checkTurn(blok.d,1,-1)==1)){
                    rDown(blok.a);
                    rDown(blok.a);
                    rLeft(blok.a);
                    rLeft(blok.a);
                    rDown(blok.b);
                    rLeft(blok.b);
                    rUP(blok.d);
                    rRight(blok.d);
                    blok.rotate();
                    break;
                }
                if((block.state==3) && (checkTurn(blok.a,2,-2)==1) && (checkTurn(blok.b,1,-1)==1)
                        && (checkTurn(blok.d,-1,1)==1)){
                    rUP(blok.a);
                    rUP(blok.a);
                    rRight(blok.a);
                    rRight(blok.a);
                    rUP(blok.b);
                    rRight(blok.b);
                    rDown(blok.d);
                    rLeft(blok.d);
                    blok.rotate();
                    break;
                }
                if((block.state==4) && (checkTurn(blok.a,-2,2)==1) && (checkTurn(blok.b,1,-1)==1)
                        && (checkTurn(blok.d,-1,1)==1)){
                    rDown(blok.a);
                    rDown(blok.a);
                    rLeft(blok.a);
                    rLeft(blok.a);
                    rDown(blok.b);
                    rLeft(blok.b);
                    rUP(blok.d);
                    rRight(blok.d);
                    blok.rotate();
                    break;
                }
            case "l":
                if((block.state==1) && (checkTurn(blok.a,1,-1)==1) && (checkTurn(blok.c,-1,1)==1)
                        && (checkTurn(blok.d,2,0)==1)){
                    rRight(blok.d);
                    rRight(blok.d);
                    rUP(blok.a);
                    rRight(blok.a);
                    rDown(blok.c);
                    rLeft(blok.c);
                    blok.rotate();
                    break;
                }
                if((block.state==2) && (checkTurn(blok.a,1,1)==1) && (checkTurn(blok.c,-1,-1)==1)
                        && (checkTurn(blok.d,0,2)==1)){
                    rDown(blok.d);
                    rDown(blok.d);
                    rDown(blok.a);
                    rRight(blok.a);
                    rUP(blok.c);
                    rLeft(blok.c);
                    blok.rotate();
                    break;
                }
                if((block.state==3) && (checkTurn(blok.a,-1,1)==1) && (checkTurn(blok.c,1,-1)==1)
                        && (checkTurn(blok.d,-2,0)==1)){
                    rLeft(blok.d);
                    rLeft(blok.d);
                    rLeft(blok.a);
                    rDown(blok.a);
                    rUP(blok.c);
                    rRight(blok.c);
                    blok.rotate();
                    break;
                }
                if((block.state==4) && (checkTurn(blok.a,-1,-1)==1) && (checkTurn(blok.c,1,1)==1)
                        && (checkTurn(blok.d,0,-2)==1)){
                    rUP(blok.d);
                    rUP(blok.d);
                    rLeft(blok.a);
                    rUP(blok.a);
                    rRight(blok.c);
                    rDown(blok.c);
                    blok.rotate();
                    break;
                }
            case "j":
                if((block.state==1) && (checkTurn(blok.a,1,-1)==1) && (checkTurn(blok.c,-1,1)==1)
                        && (checkTurn(blok.d,0,2)==1)){
                    rDown(blok.d);
                    rDown(blok.d);
                    rRight(blok.a);
                    rUP(blok.a);
                    rLeft(blok.c);
                    rDown(blok.c);
                    blok.rotate();
                    break;
                }
                if((block.state==2) && (checkTurn(blok.a,1,1)==1) && (checkTurn(blok.c,-1,-1)==1)
                        && (checkTurn(blok.d,-2,0)==1)){
                    rLeft(blok.d);
                    rLeft(blok.d);
                    rRight(blok.a);
                    rDown(blok.a);
                    rLeft(blok.c);
                    rUP(blok.c);
                    blok.rotate();
                    break;
                }
                if((block.state==3) && (checkTurn(blok.a,-1,1)==1) && (checkTurn(blok.c,1,-1)==1)
                        && (checkTurn(blok.d,0,-2)==1)){
                    rUP(blok.d);
                    rUP(blok.d);
                    rLeft(blok.a);
                    rDown(blok.a);
                    rRight(blok.c);
                    rUP(blok.c);
                    blok.rotate();
                    break;
                }
                if((block.state==4) && (checkTurn(blok.a,-1,-1)==1) && (checkTurn(blok.c,1,1)==1)
                        && (checkTurn(blok.d,2,0)==1)){
                    rRight(blok.d);
                    rRight(blok.d);
                    rLeft(blok.a);
                    rUP(blok.a);
                    rRight(blok.c);
                    rDown(blok.c);
                    blok.rotate();
                    break;
                }
            case "o":
                break;
            case "s":
                if((block.state==1) && (checkTurn(blok.a,1,-1)==1) && (checkTurn(blok.c,1,1)==1)
                        && (checkTurn(blok.d,0,2)==1)){
                    rDown(blok.d);
                    rDown(blok.d);
                    rRight(blok.a);
                    rUP(blok.a);
                    rRight(blok.c);
                    rDown(blok.c);
                    blok.rotate();
                    break;
                }
                if((block.state==2) && (checkTurn(blok.a,1,1)==1) && (checkTurn(blok.c,-1,1)==1)
                        && (checkTurn(blok.d,-2,0)==1)){
                    rLeft(blok.d);
                    rLeft(blok.d);
                    rRight(blok.a);
                    rDown(blok.a);
                    rLeft(blok.c);
                    rDown(blok.c);
                    blok.rotate();
                    break;
                }
                if((block.state==3) && (checkTurn(blok.a,-1,1)==1) && (checkTurn(blok.c,-1,-1)==1)
                        && (checkTurn(blok.d,0,-2)==1)){
                    rUP(blok.d);
                    rUP(blok.d);
                    rLeft(blok.a);
                    rDown(blok.a);
                    rLeft(blok.c);
                    rUP(blok.c);
                    blok.rotate();
                    break;
                }
                if((block.state==4) && (checkTurn(blok.a,-1,-1)==1) && (checkTurn(blok.c,1,-1)==1)
                        && (checkTurn(blok.d,2,0)==1)){
                    rRight(blok.d);
                    rRight(blok.d);
                    rLeft(blok.a);
                    rUP(blok.a);
                    rUP(blok.c);
                    rRight(blok.c);
                    blok.rotate();
                    break;
                }
            case "t":
                if((block.state==1) && (checkTurn(blok.a,1,-1)==1) && (checkTurn(blok.c,-1,1)==1)
                        && (checkTurn(blok.d,-1,-1)==1)){
                    rUP(blok.d);
                    rLeft(blok.d);
                    rRight(blok.a);
                    rUP(blok.a);
                    rLeft(blok.c);
                    rDown(blok.c);
                    blok.rotate();
                    break;
                }
                if((block.state==2) && (checkTurn(blok.a,1,1)==1) && (checkTurn(blok.c,-1,-1)==1)
                        && (checkTurn(blok.d,1,-1)==1)){
                    rUP(blok.d);
                    rRight(blok.d);
                    rRight(blok.a);
                    rDown(blok.a);
                    rLeft(blok.c);
                    rUP(blok.c);
                    blok.rotate();
                    break;
                }
                if((block.state==3) && (checkTurn(blok.a,-1,1)==1) && (checkTurn(blok.c,1,-1)==1)
                        && (checkTurn(blok.d,1,1)==1)){
                    rDown(blok.d);
                    rRight(blok.d);
                    rLeft(blok.a);
                    rDown(blok.a);
                    rRight(blok.c);
                    rUP(blok.c);
                    blok.rotate();
                    break;
                }
                if((block.state==4) && (checkTurn(blok.a,-1,-1)==1) && (checkTurn(blok.c,1,1)==1)
                        && (checkTurn(blok.d,-1,1)==1)){
                    rDown(blok.d);
                    rLeft(blok.d);
                    rLeft(blok.a);
                    rUP(blok.a);
                    rRight(blok.c);
                    rDown(blok.c);
                    blok.rotate();
                    break;
                }
            case "z":
                if((block.state==1) && (checkTurn(blok.b,-1,1)==1) && (checkTurn(blok.c,2,0)==1)
                        && (checkTurn(blok.d,1,1)==1)){
                    rDown(blok.d);
                    rRight(blok.d);
                    rLeft(blok.b);
                    rDown(blok.b);
                    rRight(blok.c);
                    rRight(blok.c);
                    blok.rotate();
                    break;
                }
                if((block.state==2) && (checkTurn(blok.b,-1,-1)==1) && (checkTurn(blok.c,0,2)==1)
                        && (checkTurn(blok.d,-1,1)==1)){
                    rDown(blok.d);
                    rLeft(blok.d);
                    rLeft(blok.b);
                    rUP(blok.b);
                    rDown(blok.c);
                    rDown(blok.c);
                    blok.rotate();
                    break;
                }
                if((block.state==3) && (checkTurn(blok.b,1,-1)==1) && (checkTurn(blok.c,-2,0)==1)
                        && (checkTurn(blok.d,-1,-1)==1)){
                    rLeft(blok.d);
                    rUP(blok.d);
                    rRight(blok.b);
                    rUP(blok.b);
                    rLeft(blok.c);
                    rLeft(blok.c);
                    blok.rotate();
                    break;
                }
                if((block.state==4) && (checkTurn(blok.b,1,1)==1) && (checkTurn(blok.c,0,-2)==1)
                        && (checkTurn(blok.d,1,-1)==1)){
                    rRight(blok.d);
                    rUP(blok.d);
                    rRight(blok.b);
                    rDown(blok.b);
                    rUP(blok.c);
                    rUP(blok.c);
                    blok.rotate();
                    break;
                }
        }

    }
    //mut patrateleeee
    private void rUP(Rectangle r){
        if(r.getY()-size>=0){
            r.setY(r.getY()-size);
        }
    }
    private void rRight(Rectangle r){
        if(r.getX()+size<=xmax){
            r.setX(r.getX()+size);
        }
    }
    private void rDown(Rectangle r){
        if(r.getY()+size<=ymax){
            r.setY(r.getY()+size);
        }
    }
    private void rLeft(Rectangle r){
        if(r.getX()-size>=0){
            r.setX(r.getX()-size);
        }
    }
///verific coliziunea cu alte blocuri pt cand vreau sa invart blocul care pica acuma
    public static int checkTurn(Rectangle r, int x, int y){
        int yesX=0;
        int yesY=0;
        if((x>=0) && ((int)r.getX()+x*size<xmax)){
                yesX=1;
        }
        if((x<0) && ((int)r.getX()+x*size>0)){ //daca x<0 si scad din getX - cu - face plus si mereu va fi >=0, de aia
            //punem +
                yesX=1;
        }
        if((y>=0) && ((int)r.getY()+y*size<ymax)){
                yesY=1;
        }
        if((y<0)&&((int)r.getY()+y*size>0)){ //din ac cauza pastrez + la y, cand y<0 urc, cand y>0 cobor pe Grid
                yesY=1;
        }
        if((yesX==1)&&(yesY==1)&&(Grid[((int)r.getX()/size)+x][((int)r.getY()/size)+y]==0)){
            return 1;
        }
        else{
            return 0;
        }
    }
}
