package com.example.demo;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;

import java.awt.*;

public class Controller {

    public static final int size= Game.size;
    public static final int xmax=Game.xmax;
    public static final int ymax=Game.ymax;

    public static void goRight(Block block){
        if(block.a.getX()+size<xmax && block.b.getX()+size<xmax
                && block.c.getX()+size<xmax && block.d.getX()+size<xmax){ //verific daca nu depaseste
            //verific daca patratele unde vreau sa mut blocul sunt libere
            int emptya=Game.Grid[(int)(block.a.getX()/size)+1][(int)(block.a.getY()/size)];
            int emptyb=Game.Grid[(int)(block.b.getX()/size)+1][(int)(block.b.getY()/size)];
            int emptyc=Game.Grid[(int)(block.c.getX()/size)+1][(int)(block.c.getY()/size)];
            int emptyd=Game.Grid[(int)(block.d.getX()/size)+1][(int)(block.d.getY()/size)];
            if((emptya==0)&&(emptyb==0)&&(emptyc==0)&&(emptyd==0)){
                block.a.setX(block.a.getX()+size);
                block.b.setX(block.b.getX()+size);
                block.c.setX(block.c.getX()+size);
                block.d.setX(block.d.getX()+size);
            }
        }
    }
    public static void goLeft(Block block){
        if(block.a.getX()-size>0 && block.b.getX()-size>0
                && block.c.getX()-size>0 && block.d.getX()-size>0){ //verific daca nu depaseste
            //verific daca patratele unde vreau sa mut blocul sunt libere
            int emptya=Game.Grid[(int)(block.a.getX()/size)-1][(int)(block.a.getY()/size)];
            int emptyb=Game.Grid[(int)(block.b.getX()/size)-1][(int)(block.b.getY()/size)];
            int emptyc=Game.Grid[(int)(block.c.getX()/size)-1][(int)(block.c.getY()/size)];
            int emptyd=Game.Grid[(int)(block.d.getX()/size)-1][(int)(block.d.getY()/size)];
            if((emptya==0)&&(emptyb==0)&&(emptyc==0)&&(emptyd==0)){
                block.a.setX(block.a.getX()-size);
                block.b.setX(block.b.getX()-size);
                block.c.setX(block.c.getX()-size);
                block.d.setX(block.d.getX()-size);
            }
        }
    }

    public static Block createBlock(){
        int which=(int)(Math.random()*6);
        Rectangle a=new Rectangle(size,size);
        Rectangle b=new Rectangle(size,size);
        Rectangle c=new Rectangle(size,size);
        Rectangle d=new Rectangle(size,size);
        String name=new String();
        switch(which){
            case 0:
                name="i";
                a.setX(xmax/2-size);
                b.setX(xmax/2);
                c.setX(xmax/2+size);
                d.setX(xmax/2+size*2);
                break;
            case 1:
                name="l";
                a.setX(xmax/2);
                a.setY(size);
                b.setX(xmax/2+size);
                b.setY(size);
                c.setX(xmax/2+size*2);
                c.setY(size);
                d.setX(xmax/2);
                break;
            case 2:
                name="j";
                a.setX(xmax/2);
                a.setY(size);
                b.setX(xmax/2+size);
                b.setY(size);
                c.setX(xmax/2+size*2);
                c.setY(size);
                d.setX(xmax/2+size*2);
                break;
            case 3:
                name="o";
                a.setX(xmax/2);
                a.setY(size);
                b.setX(xmax/2+size);
                b.setY(size);
                c.setX(xmax/2);
                d.setX(xmax/2+size);
                break;
            case 4:
                name="s";
                a.setX(xmax/2-size);
                a.setY(size);
                b.setX(xmax/2);
                b.setY(size);
                c.setX(xmax/2);
                d.setX(xmax/2+size);
                break;
            case 5:
                name="t";
                a.setX(xmax/2-size);
                b.setX(xmax/2);
                c.setX(xmax/2+size);
                d.setX(xmax/2);
                d.setY(size);
                break;
            case 6:
                name="z";
                a.setX(xmax/2);
                a.setY(size);
                b.setX(xmax/2+size);
                b.setY(size);
                c.setX(xmax/2-size);
                d.setX(xmax/2);
                break;
        }
        return new Block(a,b,c,d,name);
    }
}